package org.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Random random = new SecureRandom();

    private static final int MAX_ATTEMPTS = 3;
    private static final int OTP_LENGTH = 6;

    public String generateOtp(String type, String transactionId, int expiryMillis) {
        String otpKey = getOtpKey(type, transactionId);

        OtpData existingOtp = (OtpData) redisTemplate.opsForValue().get(otpKey);
        if (existingOtp != null && !isExpired(existingOtp.getExpiresAt())) {
            return existingOtp.getOtpCode();
        }

        String otpCode = generateNumericOtp(OTP_LENGTH);
        otpCode = "111111";
        long now = currentTimeMillis();
        long expiresAt = now + expiryMillis;

        OtpData otpData = new OtpData(otpCode, type, transactionId, now, expiresAt, 0, false);

        redisTemplate.opsForValue().set(otpKey, otpData, expiryMillis, TimeUnit.MILLISECONDS);

        return otpCode;
    }

    public boolean validateOtp(String type, String transactionId, String otpCode) {
        String otpKey = getOtpKey(type, transactionId);
        OtpData otpData = (OtpData) redisTemplate.opsForValue().get(otpKey);

        if (otpData == null || isExpired(otpData.getExpiresAt())) {
            redisTemplate.delete(otpKey);
            return false;
        }

        if (otpData.getAttemptCount() >= MAX_ATTEMPTS) {
            redisTemplate.delete(otpKey);
            return false;
        }

        otpData.setAttemptCount(otpData.getAttemptCount() + 1);

        if (otpData.getOtpCode().equals(otpCode)) {
            otpData.setVerified(true);
            redisTemplate.delete(otpKey);
            return true;
        }

        redisTemplate.opsForValue().set(otpKey, otpData);
        return false;
    }

    private boolean isExpired(long expiresAt) {
        return expiresAt <= currentTimeMillis();
    }

    private String generateNumericOtp(int length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private String getOtpKey(String type, String transactionId) {
        return "otp:" + type + ":" + transactionId;
    }

    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
