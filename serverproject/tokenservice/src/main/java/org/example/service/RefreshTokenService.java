package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.RefreshToken;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String createRefreshToken(Integer userId) {
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .userId(userId)
                .validUntil(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public boolean isValid(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
        return refreshTokenOpt
                .map(rt -> rt.getValidUntil().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    public void revokeAllTokensOfUser(Integer userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        tokens.forEach(t -> t.setValidUntil(now));

        userRepository.findById(userId).ifPresent(user -> {
            user.setJwtVersion(user.getJwtVersion() + 1);
            userRepository.save(user);
        });
        refreshTokenRepository.saveAll(tokens);
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.invalidateToken(token, LocalDateTime.now());
    }

    @Transactional
    public String rotateRefreshToken(String oldToken, Integer userId) {
        revokeToken(oldToken);
        return createRefreshToken(userId);
    }
}
