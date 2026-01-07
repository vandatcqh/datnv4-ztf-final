package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.kafka.OtpProducer;
import org.example.metrics.RegisterMetrics;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final OtpGrpcClientService otpGrpcClientService;
    private final OtpProducer otpProducer;
    private final RedisTemplate<String, String> redisTemplate;
    private final RegisterMetrics registerMetrics; // injected metric component

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private static final long PENDING_EXPIRE_MINUTES = 10;
    private static final int RATE_LIMIT_COUNT = 5;

    // ------------------------
    // REGISTER
    // ------------------------
    public ApiResponse register(RegisterRequest request) {
        try {
            // Check username/email exist
            if (userRepository.existsByUsername(request.getUsername())) {
                return ApiResponse.of(ApiStatus.REGISTER_USERNAME_EXISTS, "Username already exists");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                return ApiResponse.of(ApiStatus.REGISTER_EMAIL_EXISTS, "Email already exists");
            }

            // Generate transactionId + OTP
            String transactionId = UUID.randomUUID().toString();
            long otpStart = System.nanoTime();
            String otp = otpGrpcClientService.generateOtp("register", transactionId);
            long otpEnd = System.nanoTime();
            registerMetrics.recordOtp((otpEnd - otpStart) / 1_000_000); // ms

            // Store pending in Redis
            String pendingKey = "register:data:" + request.getEmail();
            long bcryptStart = System.nanoTime();
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            long bcryptEnd = System.nanoTime();
            registerMetrics.recordBcrypt((bcryptEnd - bcryptStart) / 1_000_000);

            long redisStart = System.nanoTime();
            redisTemplate.opsForHash().put(pendingKey, "transactionId", transactionId);
            redisTemplate.opsForHash().put(pendingKey, "fullname", request.getFullname());
            redisTemplate.opsForHash().put(pendingKey, "username", request.getUsername());
            redisTemplate.opsForHash().put(pendingKey, "password", hashedPassword);
            redisTemplate.expire(pendingKey, PENDING_EXPIRE_MINUTES, TimeUnit.MINUTES);
            long redisEnd = System.nanoTime();
            registerMetrics.recordRedis((redisEnd - redisStart) / 1_000_000);

            // Rate limit
            String rateKey = "register:rate:" + request.getEmail();
            Long current = redisTemplate.opsForValue().increment(rateKey);
            if (current == 1) redisTemplate.expire(rateKey, 1, TimeUnit.MINUTES);
            if (current > RATE_LIMIT_COUNT) {
                return ApiResponse.of(ApiStatus.REGISTER_TOO_MANY_REQUESTS, "Too many requests!!!!!");
            }

            // Send OTP Kafka
            long kafkaStart = System.nanoTime();
            otpProducer.sendOtp(request.getEmail(), otp);
            long kafkaEnd = System.nanoTime();
            registerMetrics.recordKafka((kafkaEnd - kafkaStart) / 1_000_000);

            Map<String, Object> data = new HashMap<>();
            data.put("transactionId", transactionId);
            return ApiResponse.of(ApiStatus.REGISTER_SUCCESS,
                    "OTP sent to email. Please verify to complete registration.", data);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.REGISTER_UNEXPECTED_ERROR, e.getMessage());
        }
    }

    // ------------------------
    // REGISTER VERIFY
    // ------------------------
    public ApiResponse registerVerify(RegisterVerifyRequest request) {
        try {
            String email = request.getEmail();
            String dataKey = "register:data:" + email;

            Map<Object, Object> userData = redisTemplate.opsForHash().entries(dataKey);
            if (userData == null || userData.isEmpty() ||
                    !request.getTransactionId().equals(userData.get("transactionId"))) {
                return ApiResponse.of(ApiStatus.REGISTER_VERIFY_TRANSACTION_EXPIRED, "Transaction expired or not found");
            }

            boolean valid = otpGrpcClientService.validateOtp("register", request.getTransactionId(), request.getOtp());
            if (!valid) {
                return ApiResponse.of(ApiStatus.REGISTER_VERIFY_INVALID_OTP, "Invalid OTP");
            }

            User user = new User();
            user.setFullname((String) userData.get("fullname"));
            user.setUsername((String) userData.get("username"));
            user.setEmail(email);
            user.setPassword((String) userData.get("password"));

            long saveStart = System.nanoTime();
            User savedUser = userRepository.save(user);
            long saveEnd = System.nanoTime();
            registerMetrics.recordDb((saveEnd - saveStart) / 1_000_000);

            redisTemplate.delete(dataKey);

            Map<String, Object> data = new HashMap<>();
            data.put("id", savedUser.getId());
            data.put("fullname", savedUser.getFullname());
            data.put("username", savedUser.getUsername());
            data.put("email", savedUser.getEmail());

            return ApiResponse.of(ApiStatus.REGISTER_VERIFY_SUCCESS, "Registered successfully!!!", data);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.REGISTER_VERIFY_UNEXPECTED_ERROR, e.getMessage());
        }
    }

    // ------------------------
    // RESEND OTP
    // ------------------------
    public ApiResponse resendOtp(ResendOtpRequest request) {
        try {
            String email = request.getEmail();
            String oldTransactionId = request.getTransactionId();
            String pendingKey = "register:data:" + email;

            Map<Object, Object> userData = redisTemplate.opsForHash().entries(pendingKey);
            if (userData == null || userData.isEmpty()) {
                return ApiResponse.of(ApiStatus.RESEND_OTP_NOT_FOUND, "Transaction expired or not found");
            }

            if (!oldTransactionId.equals(userData.get("transactionId"))) {
                return ApiResponse.of(ApiStatus.RESEND_OTP_TRANSACTION_MISMATCH, null);
            }

            // Rate limit
            String rateKey = "register:rate:" + email;
            Long current = redisTemplate.opsForValue().increment(rateKey);
            if (current == 1) redisTemplate.expire(rateKey, 1, TimeUnit.MINUTES);
            if (current > RATE_LIMIT_COUNT) {
                return ApiResponse.of(ApiStatus.RESEND_OTP_TOO_MANY_REQUESTS, null);
            }

            // New transactionId + OTP
            String newTransactionId = UUID.randomUUID().toString();
            redisTemplate.opsForHash().put(pendingKey, "transactionId", newTransactionId);
            redisTemplate.expire(pendingKey, PENDING_EXPIRE_MINUTES, TimeUnit.MINUTES);

            long otpStart = System.nanoTime();
            String otp = otpGrpcClientService.generateOtp("register", newTransactionId);
            long otpEnd = System.nanoTime();
            registerMetrics.recordOtp((otpEnd - otpStart) / 1_000_000);

            long kafkaStart = System.nanoTime();
            otpProducer.sendOtp(email, otp);
            long kafkaEnd = System.nanoTime();
            registerMetrics.recordKafka((kafkaEnd - kafkaStart) / 1_000_000);

            Map<String, Object> data = new HashMap<>();
            data.put("transactionId", newTransactionId);
            return ApiResponse.of(ApiStatus.RESEND_OTP_SUCCESS, ApiStatus.RESEND_OTP_SUCCESS.getDefaultMessage(), data);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.RESEND_OTP_NOT_FOUND, e.getMessage());
        }
    }

}
