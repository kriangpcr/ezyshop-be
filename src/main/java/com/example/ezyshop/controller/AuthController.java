package com.example.ezyshop.controller;

import com.example.ezyshop.dto.auth.*;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ){
        LoginResponse loginResponse = authService.login(request);
        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", loginResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        // Refresh Token
        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                accessCookie.toString()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    )  {
        // อ่าน Refresh Token จาก Cookie
        String refreshToken = WebUtils.getCookie(
                request, "refresh_token"
        ) != null
                ? WebUtils.getCookie(request, "refresh_token").getValue()
                : null;

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException(
                    "Refresh token is missing"
            );
        }
        RefreshTokenResponse refreshTokenResponse =
                authService.refresh(refreshToken);

        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", refreshTokenResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        // Refresh Token
        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", refreshTokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                accessCookie.toString()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response
    ) {
        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/auth")
                .maxAge(0)
                .sameSite("None")
                .build();

        ResponseCookie jsessionid = ResponseCookie
                .from("JSESSIONID", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                accessCookie.toString()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                jsessionid.toString()
        );

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/login/google")
    public ResponseEntity<Void> loginGoogle(
            HttpServletResponse response
    ) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/loginSuccess")
    public void handleGoogleSuccess(OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpServletResponse response) throws IOException {
        String frontendUrl = "http://localhost:3000";
        LoginResponse loginResponse = authService.LoginRegisterByGoogleOAuth(oAuth2AuthenticationToken);
        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", loginResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        // Refresh Token
        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                accessCookie.toString()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );
        response.sendRedirect(frontendUrl);
    }

}
