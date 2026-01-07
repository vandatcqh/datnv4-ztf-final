package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.service.AuthService;
import org.example.service.RegisterService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import org.example.service.User2FAService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterService registerService;
    private final User2FAService user2FAService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerRequest(
            @Valid @RequestBody RegisterRequest request) {
        ApiResponse response = registerService.register(request);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/register/verify")
    public ResponseEntity<ApiResponse> registerVerify(
            @Valid @RequestBody RegisterVerifyRequest request) {
        ApiResponse response = registerService.registerVerify(request);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }


    @PostMapping("/register/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestBody ResendOtpRequest request) {
        ApiResponse response = registerService.resendOtp(request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }



    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse response = authService.login(request);

        Map<String, Object> data = response.getData();
        String refreshToken = data != null ? (String) data.get("refreshToken") : null;

        if (refreshToken != null) {
            
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            data.remove("refreshToken");

            return ResponseEntity
                    .status(response.getStatus())
                    .header("Set-Cookie", refreshCookie.toString())
                    .body(response);
        }

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/login_verify_2fa")
    public ResponseEntity<ApiResponse> verify2FA(@RequestBody LoginVerify2FARequest request) {
        ApiResponse response = authService.loginVerify2FA(request);
        Map<String, Object> data = response.getData();

        // Nếu có access token, set cookie nếu cần
        String refreshToken = data != null ? (String) data.get("refreshToken") : null;
        if (refreshToken != null) {
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();
            data.remove("refreshToken");
            return ResponseEntity
                    .status(response.getStatus())
                    .header("Set-Cookie", refreshCookie.toString())
                    .body(response);
        }

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }



    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {

        ApiResponse response = authService.logout(refreshToken);

        // Xóa cookie refresh token
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(response.getStatus())
                .header("Set-Cookie", deleteCookie.toString())
                .body(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(
            @RequestBody RefreshRequest refreshRequest,
            @RequestHeader("Authorization") String authorizationHeader,
            @CookieValue(value = "refresh_token", required = false) String refreshTokenCookie) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(ApiStatus.REFRESH_FAIL.getCode())
                    .body(ApiResponse.of(ApiStatus.REFRESH_FAIL, "Access token missing or invalid"));
        }

        String accessToken = authorizationHeader.substring(7);
        ApiResponse response = authService.refreshTokens(
                refreshRequest.getUserId(),
                accessToken,
                refreshTokenCookie
        );

        // Nếu có refresh token mới → set lại cookie
        if (response.getData() != null && response.getData().get("refreshToken") != null) {
            String newRefreshToken = (String) response.getData().get("refreshToken");

            ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            response.getData().remove("refreshToken"); // không trả ra body

            return ResponseEntity
                    .status(response.getStatus())
                    .header("Set-Cookie", cookie.toString())
                    .body(response);
        }

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // Trong AuthController.java
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ApiResponse response = authService.forgotPassword(request);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/forgot-password/verify")
    public ResponseEntity<ApiResponse> forgotPasswordVerify(@RequestBody ForgotPasswordVerifyRequest request) {
        ApiResponse response = authService.forgotPasswordVerify(request);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/2fa/setup")
    public ResponseEntity<ApiResponse> setup2FA(@RequestBody TwoFARequest request) {
        ApiResponse response = user2FAService.setup2FA(request.getUserId());
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<ApiResponse> verify2FA(@RequestBody TwoFARequest request) {
        ApiResponse response = user2FAService.verifyAndEnable2FAResponse(
                request.getUserId(), request.getOtp()
        );
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

}

