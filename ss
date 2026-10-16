warning: in the working copy of 'pom.xml', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'src/main/resources/application.properties', LF will be replaced by CRLF the next time Git touches it
[1mdiff --git a/pom.xml b/pom.xml[m
[1mindex 10e3282..16c46ed 100644[m
[1m--- a/pom.xml[m
[1m+++ b/pom.xml[m
[36m@@ -119,6 +119,10 @@[m
 			<artifactId>mapstruct</artifactId>[m
 			<version>1.6.3</version>[m
 		</dependency>[m
[32m+[m		[32m<dependency>[m
[32m+[m			[32m<groupId>org.hibernate.orm</groupId>[m
[32m+[m			[32m<artifactId>hibernate-vector</artifactId>[m
[32m+[m		[32m</dependency>[m
 	</dependencies>[m
 [m
 	<build>[m
[1mdiff --git a/src/main/java/com/example/ezyshop/config/SecurityConfig.java b/src/main/java/com/example/ezyshop/config/SecurityConfig.java[m
[1mindex 66aedb5..0a4870e 100644[m
[1m--- a/src/main/java/com/example/ezyshop/config/SecurityConfig.java[m
[1m+++ b/src/main/java/com/example/ezyshop/config/SecurityConfig.java[m
[36m@@ -58,17 +58,21 @@[m [mpublic class SecurityConfig {[m
     public CorsConfigurationSource corsConfigurationSource() {[m
         CorsConfiguration configuration = new CorsConfiguration();[m
 [m
[31m-        // อนุญาตทุก Origin[m
[31m-        configuration.setAllowedOriginPatterns(List.of("*"));[m
[32m+[m[32m        // อนุญาตเฉพาะ Frontend ของเรา[m
[32m+[m[32m        configuration.setAllowedOrigins([m
[32m+[m[32m                List.of("http://localhost:3000")[m
[32m+[m[32m        );[m
 [m
[31m-        // อนุญาตทุก HTTP Method[m
[31m-        configuration.setAllowedMethods(List.of("*"));[m
[32m+[m[32m        // อนุญาต HTTP Methods[m
[32m+[m[32m        configuration.setAllowedMethods([m
[32m+[m[32m                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")[m
[32m+[m[32m        );[m
 [m
[31m-        // อนุญาตทุก Header[m
[32m+[m[32m        // อนุญาต Request Headers[m
         configuration.setAllowedHeaders(List.of("*"));[m
 [m
[31m-        // ถ้าไม่ได้ใช้ Cookie ไม่ต้องเปิด[m
[31m-        configuration.setAllowCredentials(false);[m
[32m+[m[32m        // อนุญาตให้ส่ง Cookie[m
[32m+[m[32m        configuration.setAllowCredentials(true);[m
 [m
         UrlBasedCorsConfigurationSource source =[m
                 new UrlBasedCorsConfigurationSource();[m
[1mdiff --git a/src/main/java/com/example/ezyshop/controller/AuthController.java b/src/main/java/com/example/ezyshop/controller/AuthController.java[m
[1mindex ff2eada..589d342 100644[m
[1m--- a/src/main/java/com/example/ezyshop/controller/AuthController.java[m
[1m+++ b/src/main/java/com/example/ezyshop/controller/AuthController.java[m
[36m@@ -2,13 +2,17 @@[m [mpackage com.example.ezyshop.controller;[m
 [m
 import com.example.ezyshop.dto.auth.*;[m
 import com.example.ezyshop.service.AuthService;[m
[32m+[m[32mimport jakarta.servlet.http.HttpServletRequest;[m
 import jakarta.servlet.http.HttpServletResponse;[m
 import jakarta.validation.Valid;[m
 import lombok.RequiredArgsConstructor;[m
 import org.springframework.http.HttpHeaders;[m
[32m+[m[32mimport org.springframework.http.HttpStatus;[m
 import org.springframework.http.ResponseCookie;[m
 import org.springframework.http.ResponseEntity;[m
 import org.springframework.web.bind.annotation.*;[m
[32m+[m[32mimport org.springframework.web.server.ResponseStatusException;[m
[32m+[m[32mimport org.springframework.web.util.WebUtils;[m
 [m
 import java.time.Duration;[m
 [m
[36m@@ -29,7 +33,7 @@[m [mpublic class AuthController {[m
     }[m
 [m
     @PostMapping("/login")[m
[31m-    public ResponseEntity<LoginResponse> login([m
[32m+[m[32m    public ResponseEntity<Void> login([m
             @Valid @RequestBody LoginRequest request,[m
             HttpServletResponse response[m
     ){[m
[36m@@ -63,21 +67,30 @@[m [mpublic class AuthController {[m
                 refreshCookie.toString()[m
         );[m
 [m
[31m-        return ResponseEntity.ok(loginResponse);[m
[31m-    }[m
[31m-[m
[31m-    @GetMapping("/testt")[m
[31m-    public ResponseEntity<String> logins(){[m
[31m-        return ResponseEntity.ok("test");[m
[32m+[m[32m        return ResponseEntity.noContent().build();[m
     }[m
 [m
     @PostMapping("/refresh")[m
[31m-    public ResponseEntity<RefreshTokenResponse> refresh([m
[31m-            @RequestBody RefreshTokenRequest request,[m
[32m+[m[32m    public ResponseEntity<Void> refresh([m
[32m+[m[32m            HttpServletRequest request,[m
             HttpServletResponse response[m
     ) {[m
[32m+[m[32m        // อ่าน Refresh Token จาก Cookie[m
[32m+[m[32m        String refreshToken = WebUtils.getCookie([m
[32m+[m[32m                request, "refresh_token"[m
[32m+[m[32m        ) != null[m
[32m+[m[32m                ? WebUtils.getCookie(request, "refresh_token").getValue()[m
[32m+[m[32m                : null;[m
[32m+[m
[32m+[m[32m        if (refreshToken == null || refreshToken.isBlank()) {[m
[32m+[m[32m            throw new ResponseStatusException([m
[32m+[m[32m                    HttpStatus.UNAUTHORIZED,[m
[32m+[m[32m                    "Refresh token is missing"[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
         RefreshTokenResponse refreshTokenResponse =[m
[31m-                authService.refresh(request);[m
[32m+[m[32m                authService.refresh(refreshToken);[m
[32m+[m
         ResponseCookie accessCookie = ResponseCookie[m
                 .from("access_token", refreshTokenResponse.getAccessToken())[m
                 .httpOnly(true)[m
[36m@@ -107,6 +120,40 @@[m [mpublic class AuthController {[m
                 refreshCookie.toString()[m
         );[m
 [m
[31m-        return ResponseEntity.ok(refreshTokenResponse);[m
[32m+[m[32m        return ResponseEntity.noContent().build();[m
[32m+[m[32m    }[m
[32m+[m[32m    @PostMapping("/logout")[m
[32m+[m[32m    public ResponseEntity<Void> logout([m
[32m+[m[32m            HttpServletResponse response[m
[32m+[m[32m    ) {[m
[32m+[m[32m        ResponseCookie accessCookie = ResponseCookie[m
[32m+[m[32m                .from("access_token", "")[m
[32m+[m[32m                .httpOnly(true)[m
[32m+[m[32m                .secure(true)[m
[32m+[m[32m                .path("/")[m
[32m+[m[32m                .maxAge(0)[m
[32m+[m[32m                .sameSite("None")[m
[32m+[m[32m                .build();[m
[32m+[m
[32m+[m[32m        ResponseCookie refreshCookie = ResponseCookie[m
[32m+[m[32m                .from("refresh_token", "")[m
[32m+[m[32m                .httpOnly(true)[m
[32m+[m[32m                .secure(true)[m
[32m+[m[32m                .path("/")[m
[32m+[m[32m                .maxAge(0)[m
[32m+[m[32m                .sameSite("None")[m
[32m+[m[32m                .build();[m
[32m+[m
[32m+[m[32m        response.addHeader([m
[32m+[m[32m                HttpHeaders.SET_COOKIE,[m
[32m+[m[32m                accessCookie.toString()[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        response.addHeader([m
[32m+[m[32m                HttpHeaders.SET_COOKIE,[m
[32m+[m[32m                refreshCookie.toString()[m
[32m+[m[32m        );[m
[32m+[m
[32m+[m[32m        return ResponseEntity.noContent().build();[m
     }[m
 }[m
[1mdiff --git a/src/main/java/com/example/ezyshop/controller/OrderController.java b/src/main/java/com/example/ezyshop/controller/OrderController.java[m
[1mindex b9bff36..3136df2 100644[m
[1m--- a/src/main/java/com/example/ezyshop/controller/OrderController.java[m
[1m+++ b/src/main/java/com/example/ezyshop/controller/OrderController.java[m
[36m@@ -6,6 +6,7 @@[m [mimport com.example.ezyshop.dto.order.OrderResponse;[m
 import com.example.ezyshop.dto.user.UserResponse;[m
 import com.example.ezyshop.entity.Order;[m
 import com.example.ezyshop.service.OrderService;[m
[32m+[m[32mimport jakarta.validation.Valid;[m
 import lombok.RequiredArgsConstructor;[m
 import org.springframework.http.ResponseEntity;[m
 import org.springframework.security.core.Authentication;[m
[36m@@ -20,7 +21,7 @@[m [mpublic class OrderController {[m
     private final OrderService orderService;[m
 [m
     @PostMapping(path = "/create")[m
[31m-    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request, Authentication authentication){[m
[32m+[m[32m    public ResponseEntity<String> createOrder(@Valid @RequestBody CreateOrderRequest request, Authentication authentication){[m
         orderService.createOrder(request,authentication);[m
         return ResponseEntity.ok("Success");[m
     }[m
[1mdiff --git a/src/main/java/com/example/ezyshop/controller/PaymentController.java b/src/main/java/com/example/ezyshop/controller/PaymentController.java[m
[1mindex 5b98c79..8b93801 100644[m
[1m--- a/src/main/java/com/example/ezyshop/controller/PaymentController.java[m
[1m+++ b/src/main/java/com/example/ezyshop/controller/PaymentController.java[m
[36m@@ -3,6 +3,7 @@[m [mpackage com.example.ezyshop.controller;[m
 import com.example.ezyshop.dto.payment.DepositRequest;[m
 import com.example.ezyshop.dto.payment.PaymentResponse;[m
 import com.example.ezyshop.service.PaymentService;[m
[32m+[m[32mimport jakarta.validation.Valid;[m
 import lombok.RequiredArgsConstructor;[m
 import org.springframework.http.ResponseEntity;[m
 import org.springframework.security.core.Authentication;[m
[36m@@ -19,7 +20,7 @@[m [mpublic class PaymentController {[m
     private final PaymentService paymentService;[m
 [m
     @PostMapping("/deposit")[m
[31m-    public ResponseEntity<PaymentResponse> deposit(@RequestBody DepositRequest request, Authentication authentication){[m
[32m+[m[32m    public ResponseEntity<PaymentResponse> deposit(@Valid @RequestBody DepositRequest request, Authentication authentication){[m
         return ResponseEntity.ok(paymentService.deposit(request,authentication));[m
     }[m
 }[m
[1mdiff --git a/src/main/java/com/example/ezyshop/controller/ProductController.java b/src/main/java/com/example/ezyshop/controller/ProductController.java[m
[1mindex 4c39a4f..92da6d6 100644[m
[1m--- a/src/main/java/com/example/ezyshop/controller/ProductController.java[m
[1m+++ b/src/main/java/com/example/ezyshop/controller/ProductController.java[m
[36m@@ -26,6 +26,7 @@[m [mpublic class ProductController {[m
     private final ProductService productService;[m
     private final ProductItemService productItemService;[m
 [m
[32m+[m[32m    @PreAuthorize("hasRole('ADMIN')")[m
     @PostMapping(path = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)[m
     public ResponseEntity<?> createProduct([m
             @Valid @ModelAttribute ProductRequest request,[m
[36m@@ -45,7 +46,8 @@[m [mpublic class ProductController {[m
         return ResponseEntity.ok(productService.getProducts(pageable));[m
     }[m
 [m
[31m-    @PostMapping(path = "items/create")[m
[32m+[m[32m    @PreAuthorize("hasRole('ADMIN')")[m
[32m+[m[32m    @PostMapping(path = "/items/create")[m
     public ResponseEntity<String> createProductItems([m
             @Valid @RequestBody ProductItemRequest request,[m
             Authentication authentication){[m
[1mdiff --git a/src/main/java/com/example/ezyshop/controller/UserController.java b/src/main/java/com/example/ezyshop/controller/UserController.java[m
[1mindex 78051ed..3310d49 100644[m
[1m--- a/src/main/java/com/example/ezyshop/controller/UserController.java[m
[1m+++ b/src/main/java/com/example/ezyshop/controller/UserController.java[m
[36m@@ -1,6 +1,7 @@[m
 package com.example.ezyshop.controller;[m
 [m
 import com.example.ezyshop.dto.user.UserResponse;[m
[32m+[m[32mimport com.example.ezyshop.service.EmbeddingService;[m
 import com.example.ezyshop.service.UserService;[m
 import lombok.RequiredArgsConstructor;[m
 import org.springframework.http.ResponseEntity;[m
[36m@@ -15,7 +16,7 @@[m [mimport java.math.BigDecimal;[m
 public class UserController {[m
 [m
     private final UserService userService;[m
[31m-[m
[32m+[m[32m    private final EmbeddingService embeddingService;[m
 [m
     @GetMapping("/me")[m
     public ResponseEntity<UserResponse> getUser(Authentication authentication){[m
[1mdiff --git a/src/main/java/com/example/ezyshop/dto/auth/LoginRequest.java b/src/main/java/com/example/ezyshop/dto/auth/LoginRequest.java[m
[1mindex 5d2c03a..c06ca13 100644[m
[1m--- a/src/main/java/com/example/ezyshop/dto/auth/LoginRequest.java[m
[1m+++ b/src/main/java/com/example/ezyshop/dto/auth/LoginRequest.java[m
[36m@@ -10,9 +10,9 @@[m [mimport lombok.Setter;[m
 @NoArgsConstructor[m
 public class LoginRequest {[m
 [m
[31m-    @NotBlank[m
[32m+[m[32m    @NotBlank(message = "Username is required")[m
     private String username;[m
 [m
[31m-    @NotBlank[m
[32m+[m[32m    @NotBlank(message = "Password is required")[m
     private String password;[m
 }[m
[1mdiff --git a/src/main/java/com/example/ezyshop/dto/order/CreateOrderRequest.java b/src/main/java/com/example/ezyshop/dto/order/CreateOrderRequest.java[m
[1mindex dad7cdf..238d2e7 100644[m
[1m--- a/src/main/java/com/example/ezyshop/dto/order/CreateOrderRequest.java[m
[1m+++ b/src/main/java/com/example/ezyshop/dto/order/CreateOrderRequest.java[m
[36m@@ -1,5 +1,8 @@[m
 package com.example.ezyshop.dto.order;[m
 [m
[32m+[m[32mimport jakarta.validation.constraints.Min;[m
[32m+[m[32mimport jakarta.validation.constraints.NotBlank;[m
[32m+[m[32mimport jakarta.validation.constraints.NotNull;[m
 import lombok.Getter;[m
 import lombok.NoArgsConstructor;[m
 import lombok.Setter;[m
[36m@@ -9,8 +12,11 @@[m [mimport lombok.Setter;[m
 @NoArgsConstructor[m
 public class CreateOrderRequest {[m
 [m
[32m+[m[32m    @NotBlank(message = "Product is required")[m
     private String product_id;[m
 [m
[32m+[m[32m    @NotNull(message = "Quantity is required")[m
[32m+[m[32m    @Min(value = 1,message = "Quantity must be at least 1")[m
     private long quantity;[m
 [m
 }[m
\ No newline at end of file[m
[1mdiff --git a/src/main/java/com/example/ezyshop/dto/payment/DepositRequest.java b/src/main/java/com/example/ezyshop/dto/payment/DepositRequest.java[m
[1mindex baedb40..8aef773 100644[m
[1m--- a/src/main/java/com/example/ezyshop/dto/payment/DepositRequest.java[m
[1m+++ b/src/main/java/com/example/ezyshop/dto/payment/DepositRequest.java[m
[36m@@ -1,5 +1,7 @@[m
 package com.example.ezyshop.dto.payment;[m
 [m
[32m+[m[32mimport jakarta.validation.constraints.NotBlank;[m
[32m+[m[32mimport jakarta.validation.constraints.Size;[m
 import lombok.AllArgsConstructor;[m
 import lombok.Getter;[m
 import lombok.NoArgsConstructor;[m
[36m@@ -10,5 +12,8 @@[m [mimport lombok.Setter;[m
 @NoArgsConstructor[m
 @AllArgsConstructor[m
 public class DepositRequest {[m
[32m+[m
[32m+[m[32m    @NotBlank(message = "VoucherCode ID is required")[m
[32m+[m[32m    @Size(min = 35, max = 35, message = "Voucher code must be exactly 35 characters")[m
     private String voucherCode;[m
 }[m
[1mdiff --git a/src/main/java/com/example/ezyshop/dto/product/ProductItemRequest.java b/src/main/java/com/example/ezyshop/dto/product/ProductItemRequest.java[m
[1mindex 9eacf9e..335b138 100644[m
[1m--- a/src/main/java/com/example/ezyshop/dto/product/ProductItemRequest.java[m
[1m+++ b/src/main/java/com/example/ezyshop/dto/product/ProductItemRequest.java[m
[36m@@ -1,5 +1,8 @@[m
 package com.example.ezyshop.dto.product;[m
 [m
[32m+[m[32mimport jakarta.validation.constraints.NotBlank;[m
[32m+[m[32mimport jakarta.validation.constraints.NotNull;[m
[32m+[m[32mimport jakarta.validation.constraints.Size;[m
 import lombok.Getter;[m
 import lombok.NoArgsConstructor;[m
 import lombok.Setter;[m
[36m@@ -10,6 +13,11 @@[m [mimport java.util.List;[m
 @Setter[m
 @NoArgsConstructor[m
 public class ProductItemRequest {[m
[32m+[