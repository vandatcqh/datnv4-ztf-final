package org.example.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ApiResponse;
import org.example.dto.ApiStatus;
import org.example.model.User;
import org.example.model.User2FA;
import org.example.repository.User2FARepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class User2FAService {

    private final UserRepository userRepository;
    private final User2FARepository user2FARepository;
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    /**
     * Kiểm tra user đã setup và bật 2FA hay chưa
     */
    public boolean hasEnabled2FA(int userId) {
        return user2FARepository.findByUserId(userId)
                .map(User2FA::getEnabled)
                .orElse(false);
    }

    /**
     * Tạo secret mới cho user và trả về QR code URL
     */
    @Transactional
    public ApiResponse setup2FA(int userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            User2FA user2FA = user2FARepository.findByUserId(userId).orElse(null);

            String secret;
            String qrCodeURL;

            if (user2FA == null) {
                GoogleAuthenticatorKey key = gAuth.createCredentials();
                secret = key.getKey();
                qrCodeURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                        "AMS_APP", "user: " + user.getUsername(), key
                );

                user2FA = User2FA.builder()
                        .user(user)
                        .secretKey(secret)
                        .qrCodeURL(qrCodeURL)
                        .enabled(false)
                        .build();
                user2FARepository.save(user2FA);
            } else {
                secret = user2FA.getSecretKey();
                qrCodeURL = user2FA.getQrCodeURL();
            }

            Map<String, Object> data = new HashMap<>();
            data.put("qrCodeURL", qrCodeURL);
            data.put("secret", secret);
            data.put("enabled", hasEnabled2FA(userId));

            log.info("QR Code URL for user {}: {}", userId, qrCodeURL);

            return ApiResponse.of(ApiStatus.TWO_FA_SETUP_SUCCESS, "2FA setup successful", data);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.TWO_FA_VERIFY_FAIL, e.getMessage());
        }
    }

    /**
     * Verify OTP và bật 2FA nếu hợp lệ
     */
    @Transactional
    public ApiResponse verifyAndEnable2FAResponse(int userId, int otp) {
        try {
            User2FA user2FA = user2FARepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("2FA not setup"));

            boolean isValid = gAuth.authorize(user2FA.getSecretKey(), otp);
            if (isValid) {
                user2FA.setEnabled(true);
                user2FARepository.save(user2FA);
                return ApiResponse.of(ApiStatus.TWO_FA_VERIFY_SUCCESS, "Success");
            } else {
                return ApiResponse.of(ApiStatus.TWO_FA_VERIFY_FAIL, "Invalid OTP");
            }

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.TWO_FA_VERIFY_FAIL, e.getMessage());
        }
    }

    /**
     * Xác thực OTP khi login
     */
    public boolean validateOTP(int userId, int otp) {
        if (!hasEnabled2FA(userId)) return true;

        User2FA user2FA = user2FARepository.findByUserId(userId).orElse(null);
        if (user2FA == null) return true;

        boolean valid = gAuth.authorize(user2FA.getSecretKey(), otp);
        if (valid) {
            user2FA.setLastUsedAt(LocalDateTime.now());
            user2FARepository.save(user2FA);
        }
        return valid;
    }
}
