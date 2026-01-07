//package org.example.service;
//
//import org.example.model.RefreshToken;
//import org.example.model.User;
//import org.example.repository.RefreshTokenRepository;
//import org.example.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class RefreshTokenServiceTest {
//
//    private RefreshTokenRepository refreshTokenRepository;
//    private UserRepository userRepository;
//    private RefreshTokenService refreshTokenService;
//
//    @BeforeEach
//    void setUp() {
//        refreshTokenRepository = mock(RefreshTokenRepository.class);
//        userRepository = mock(UserRepository.class);
//        refreshTokenService = new RefreshTokenService(refreshTokenRepository, userRepository);
//    }
//
//    @Test
//    void testCreateRefreshToken() {
//        Integer userId = 1;
//        String token = refreshTokenService.createRefreshToken(userId);
//
//        assertNotNull(token);
//        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
//        verify(refreshTokenRepository).save(captor.capture());
//        RefreshToken savedToken = captor.getValue();
//        assertEquals(userId, savedToken.getUserId());
//        assertEquals(token, savedToken.getToken());
//    }
//
//    @Test
//    void testIsValid_TokenExistsAndNotExpired() {
//        String token = "abc";
//        RefreshToken rt = RefreshToken.builder()
//                .token(token)
//                .userId(1)
//                .validUntil(LocalDateTime.now().plusDays(1))
//                .build();
//
//        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(rt));
//        assertTrue(refreshTokenService.isValid(token));
//    }
//
//    @Test
//    void testIsValid_TokenDoesNotExist() {
//        when(refreshTokenRepository.findByToken("nonexistent")).thenReturn(Optional.empty());
//        assertFalse(refreshTokenService.isValid("nonexistent"));
//    }
//
//    @Test
//    void testIsValid_TokenExpired() {
//        String token = "expired";
//        RefreshToken rt = RefreshToken.builder()
//                .token(token)
//                .userId(1)
//                .validUntil(LocalDateTime.now().minusDays(1))
//                .build();
//
//        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(rt));
//        assertFalse(refreshTokenService.isValid(token));
//    }
//
//    @Test
//    void testRevokeAllTokensOfUser() {
//        Integer userId = 1;
//        RefreshToken t1 = RefreshToken.builder().token("t1").userId(userId).validUntil(LocalDateTime.now().plusDays(1)).build();
//        RefreshToken t2 = RefreshToken.builder().token("t2").userId(userId).validUntil(LocalDateTime.now().plusDays(1)).build();
//
//        when(refreshTokenRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(t1, t2));
//
//        // mock userRepository
//        User user = User.builder().id(userId).jwtVersion(0).build();
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        refreshTokenService.revokeAllTokensOfUser(userId);
//
//        // check refresh token bị set validUntil = now
//        assertTrue(t1.getValidUntil().isBefore(LocalDateTime.now().plusSeconds(1)));
//        assertTrue(t2.getValidUntil().isBefore(LocalDateTime.now().plusSeconds(1)));
//
//        verify(refreshTokenRepository).saveAll(Arrays.asList(t1, t2));
//
//        // check jwtVersion tăng
//        assertEquals(1, user.getJwtVersion());
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void testRevokeToken() {
//        String token = "abc";
//        refreshTokenService.revokeToken(token);
//        verify(refreshTokenRepository).invalidateToken(eq(token), any(LocalDateTime.class));
//    }
//
//    @Test
//    void testRotateRefreshToken() {
//        String oldToken = "old";
//        Integer userId = 1;
//
//        doNothing().when(refreshTokenRepository).invalidateToken(anyString(), any(LocalDateTime.class));
//
//        RefreshTokenService spyService = spy(refreshTokenService);
//        doReturn("new-token").when(spyService).createRefreshToken(userId);
//
//        String newToken = spyService.rotateRefreshToken(oldToken, userId);
//
//        assertEquals("new-token", newToken);
//        verify(spyService).revokeToken(oldToken);
//        verify(spyService).createRefreshToken(userId);
//    }
//}
