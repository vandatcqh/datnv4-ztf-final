package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{targetId}")
    public ResponseEntity<Void> sendFriendRequest(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int targetId) {
        String token = authorizationHeader.replace("Bearer ", "");
        friendService.sendFriendRequest(token, targetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{requesterId}")
    public ResponseEntity<Void> acceptFriendRequest(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int requesterId) {
        String token = authorizationHeader.replace("Bearer ", "");
        friendService.acceptFriendRequest(token, requesterId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline/{requesterId}")
    public ResponseEntity<Void> declineFriendRequest(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int requesterId) {
        String token = authorizationHeader.replace("Bearer ", "");
        friendService.declineFriendRequest(token, requesterId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{targetId}")
    public ResponseEntity<Void> cancelFriendRequest(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int targetId) {
        String token = authorizationHeader.replace("Bearer ", "");
        friendService.cancelFriendRequest(token, targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<Void> unfriend(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int friendId) {
        String token = authorizationHeader.replace("Bearer ", "");
        friendService.unfriend(token, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sent-requests")
    public ResponseEntity<List<Map<String, Object>>> getSentFriendRequests(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        List<Map<String, Object>> requests = friendService.getSentFriendRequests(token);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/received-requests")
    public ResponseEntity<List<Map<String, Object>>> getReceivedFriendRequests(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        List<Map<String, Object>> requests = friendService.getReceivedFriendRequests(token);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<Map<String, Object>>> getFriends(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        List<Map<String, Object>> friends = friendService.getFriends(token);
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/block/{targetId}")
    public ResponseEntity<Void> blockUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int targetId) {
        String token = authorizationHeader.replace("Bearer ", "");
        friendService.blockUser(token, targetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock/{targetId}")
    public ResponseEntity<Void> unblockUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int targetId) {
        String token = authorizationHeader.replace("Bearer ", "");
        friendService.unblockUser(token, targetId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/blocked-users")
    public ResponseEntity<List<Map<String, Object>>> getBlockedUsers(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        List<Map<String, Object>> blockedUsers = friendService.getBlockedUsers(token);
        return ResponseEntity.ok(blockedUsers);
    }
}