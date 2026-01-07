//package org.example;
//
//import org.example.jwt.JwtUtil;
//import org.example.model.User;
//import org.example.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class JwtUtilTest {
//
//    private JwtUtil jwtUtil;
//    private UserRepository userRepository;
//
//    private final String secret = "01234567890123456789012345678901";
//    private final long expirationTime = 1000 * 60; // 1 phút
//
//    @BeforeEach
//    void setUp() {
//        userRepository = Mockito.mock(UserRepository.class);
//        jwtUtil = new JwtUtil(secret, expirationTime, userRepository);
//    }
//
//    @Test
//    void testGenerateTokenNotNull() {
//        User user = User.builder().id(123).jwtVersion(0).build();
//        when(userRepository.findById(123)).thenReturn(Optional.of(user));
//
//        String token = jwtUtil.generateToken(123);
//        assertNotNull(token);
//    }
//
//    @Test
//    void testValidateTokenFully_ValidToken_ReturnsTrue() {
//        User user = User.builder().id(123).jwtVersion(0).build();
//        when(userRepository.findById(123)).thenReturn(Optional.of(user));
//
//        String token = jwtUtil.generateToken(123);
//        assertTrue(jwtUtil.validateTokenFully(token));
//    }
//
//    @Test
//    void testValidateTokenFully_InvalidToken_ReturnsFalse() {
//        assertFalse(jwtUtil.validateTokenFully("this.is.invalid"));
//    }
//
//    @Test
//    void testExtractUserId_FromValidToken() {
//        User user = User.builder().id(456).jwtVersion(0).build();
//        when(userRepository.findById(456)).thenReturn(Optional.of(user));
//
//        String token = jwtUtil.generateToken(456);
//        Integer extractedUserId = jwtUtil.extractUserId(token);
//        assertEquals(456, extractedUserId);
//    }
//
//    @Test
//    void testValidateTokenFully_ExpiredToken_ReturnsFalse() throws InterruptedException {
//        User user = User.builder().id(123).jwtVersion(0).build();
//        when(userRepository.findById(123)).thenReturn(Optional.of(user));
//
//        JwtUtil shortLived = new JwtUtil(secret, 1, userRepository);
//        String token = shortLived.generateToken(123);
//
//        Thread.sleep(10); // chờ token hết hạn
//        assertFalse(shortLived.validateTokenFully(token));
//    }
//
//    @Test
//    void testValidateTokenFully_ValidVersion() {
//        User user = User.builder().id(123).jwtVersion(0).build();
//        when(userRepository.findById(123)).thenReturn(Optional.of(user));
//
//        String token = jwtUtil.generateToken(123);
//        assertTrue(jwtUtil.validateTokenFully(token));
//    }
//
//    @Test
//    void testValidateTokenFully_WrongVersion() {
//        // Step 1: tạo token với version cũ (0)
//        User userV0 = User.builder().id(123).jwtVersion(0).build();
//        when(userRepository.findById(123)).thenReturn(Optional.of(userV0));
//        String token = jwtUtil.generateToken(123);
//
//        // Step 2: mock DB version mới (VD admin reset session)
//        User userV5 = User.builder().id(123).jwtVersion(5).build();
//        when(userRepository.findById(123)).thenReturn(Optional.of(userV5));
//
//        // Step 3: validate token => phải fail
//        assertFalse(jwtUtil.validateTokenFully(token));
//    }
//}
