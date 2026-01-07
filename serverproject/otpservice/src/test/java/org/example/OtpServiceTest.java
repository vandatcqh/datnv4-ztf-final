package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private OtpService otpService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        otpService = new OtpService(redisTemplate);
    }

    @Test
    void testGenerateOtp_NewOtp() {
        String type = "register";
        String transactionId = "txn123";
        int expiryMillis = 300_000;

        String otp = otpService.generateOtp(type, transactionId, expiryMillis);

        assertNotNull(otp);
        assertEquals(6, otp.length());
        verify(valueOperations).set(eq("otp:register:txn123"), any(OtpData.class), eq((long) expiryMillis), eq(java.util.concurrent.TimeUnit.MILLISECONDS));
    }

    @Test
    void testGenerateOtp_ExistingValidOtp() {
        String type = "register";
        String transactionId = "txn123";
        int expiryMillis = 300_000;

        OtpData existingOtp = new OtpData("123456", "register", transactionId,
                System.currentTimeMillis(), System.currentTimeMillis() + 300_000, 0, false);

        when(valueOperations.get("otp:register:txn123")).thenReturn(existingOtp);

        String otp = otpService.generateOtp(type, transactionId, expiryMillis);

        assertEquals("123456", otp);
        verify(valueOperations, never()).set(anyString(), any(), anyLong(), any());
    }

    @Test
    void testValidateOtp_Success() {
        String type = "register";
        String transactionId = "txn123";
        String otpCode = "123456";

        OtpData otpData = new OtpData("123456", "register", transactionId,
                System.currentTimeMillis(), System.currentTimeMillis() + 300_000, 0, false);

        when(valueOperations.get("otp:register:txn123")).thenReturn(otpData);

        boolean isValid = otpService.validateOtp(type, transactionId, otpCode);

        assertTrue(isValid);
        verify(redisTemplate).delete("otp:register:txn123"); // key bị xóa sau khi verify thành công
        verify(valueOperations, never()).set(anyString(), any()); // không reset TTL nữa
    }

    @Test
    void testValidateOtp_WrongCode() {
        String type = "register";
        String transactionId = "txn123";
        String otpCode = "wrong";

        OtpData otpData = new OtpData("123456", "register", transactionId,
                System.currentTimeMillis(), System.currentTimeMillis() + 300_000, 0, false);

        when(valueOperations.get("otp:register:txn123")).thenReturn(otpData);

        boolean isValid = otpService.validateOtp(type, transactionId, otpCode);

        assertFalse(isValid);
        verify(valueOperations).set(eq("otp:register:txn123"), any(OtpData.class)); // chỉ update attemptCount
        verify(redisTemplate, never()).delete("otp:register:txn123"); // chưa delete vì còn hạn
    }

    @Test
    void testValidateOtp_ExpiredOtp() {
        String type = "register";
        String transactionId = "txn123";
        String otpCode = "123456";

        OtpData expiredOtp = new OtpData("123456", "register", transactionId,
                System.currentTimeMillis() - 600_000, System.currentTimeMillis() - 300_000, 0, false);

        when(valueOperations.get("otp:register:txn123")).thenReturn(expiredOtp);

        boolean isValid = otpService.validateOtp(type, transactionId, otpCode);

        assertFalse(isValid);
        verify(redisTemplate).delete("otp:register:txn123"); // xóa key vì hết hạn
        verify(valueOperations, never()).set(anyString(), any()); // không update attemptCount
    }

    @Test
    void testValidateOtp_MaxAttemptsExceeded() {
        String type = "register";
        String transactionId = "txn123";
        String otpCode = "123456";

        OtpData maxAttemptOtp = new OtpData("123456", "register", transactionId,
                System.currentTimeMillis(), System.currentTimeMillis() + 300_000, 3, false);

        when(valueOperations.get("otp:register:txn123")).thenReturn(maxAttemptOtp);

        boolean isValid = otpService.validateOtp(type, transactionId, otpCode);

        assertFalse(isValid);
        verify(redisTemplate).delete("otp:register:txn123"); // xóa key vì vượt max attempts
        verify(valueOperations, never()).set(anyString(), any()); // không update attemptCount
    }

    @Test
    void testValidateOtp_NotFound() {
        String type = "register";
        String transactionId = "txn123";
        String otpCode = "123456";

        when(valueOperations.get("otp:register:txn123")).thenReturn(null);

        boolean isValid = otpService.validateOtp(type, transactionId, otpCode);

        assertFalse(isValid);
        verify(redisTemplate).delete(anyString());
        verify(valueOperations, never()).set(anyString(), any());
    }
}
