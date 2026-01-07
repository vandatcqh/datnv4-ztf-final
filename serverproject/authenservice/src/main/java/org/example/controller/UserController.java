//package org.example.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.var;
//import org.example.dto.*;
//import org.example.service.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/user")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//    private final TokenGrpcClientService tokenService;
//
//
//    // ========================== GET PROFILE ==========================
//    @GetMapping("/profile")
//    public ResponseEntity<ApiResponse> getProfile(
//            @RequestHeader("Authorization") String authorizationHeader) {
//
//        String token = authorizationHeader.replace("Bearer ", "");
//        var validation = tokenService.validateAccessToken(token);
//        if (!validation.isValid()) {
//            return ResponseEntity.status(401)
//                    .body(ApiResponse.of(ApiStatus.UNAUTHORIZED, "Invalid or expired token"));
//        }
//
//        int userId = validation.getUserId();
//
//
//        ApiResponse response = userService.getProfile(userId);
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }
//
//    // ========================== UPDATE FULLNAME ==========================
//    @PutMapping("/update_fullname")
//    public ResponseEntity<ApiResponse> updateFullname(
//            @RequestHeader("Authorization") String authorizationHeader,
//            @RequestBody UpdateFullnameRequest request) {
//
//        String token = authorizationHeader.replace("Bearer ", "");
//        var validation = tokenService.validateAccessToken(token);
//        if (!validation.isValid()) {
//            return ResponseEntity.status(401)
//                    .body(ApiResponse.of(ApiStatus.UNAUTHORIZED, "Invalid or expired token"));
//        }
//
//        int userId = validation.getUserId();
//
//        // không cho update fullname của người khác
//        if (request.getUserId() != userId) {
//            return ResponseEntity.status(403)
//                    .body(ApiResponse.of(ApiStatus.FORBIDDEN, "Cannot modify another user's data"));
//        }
//
//
//        ApiResponse response = userService.updateFullname(request);
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }
//
//}
