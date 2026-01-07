package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.*;
import org.example.model.Relationship;
import org.example.model.User;
import org.example.repository.RelationshipRepository;
import org.example.repository.UserRepository;
import org.example.grpc.ValidateAccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final TokenGrpcClientService tokenGrpcClientService;

    private int[] normalizePair(int userId, int otherId) {
        return (userId < otherId) ? new int[]{userId, otherId} : new int[]{otherId, userId};
    }

    private int validateTokenAndGetUserId(String token) {
        ValidateAccessTokenResponse res = tokenGrpcClientService.validateAccessToken(token);
        if (!res.getValid()) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
        return res.getUserId();
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUsers(int userId, int targetId) {
        if (userId == targetId) {
            throw new FriendException(FriendErrorCode.FRIEND_REQUEST_SELF_NOT_ALLOWED);
        }

        findUserById(userId); // Validate current user exists
        findUserById(targetId); // Validate target user exists
    }

    private Relationship findRelationship(int userId1, int userId2) {
        return relationshipRepository.findByUserId1AndUserId2(userId1, userId2)
                .orElseThrow(() -> new FriendException(FriendErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    private void validateRelationshipState(Relationship rel, int expectedStatus) {
        if (rel.getStatus() != expectedStatus) {
            throw new FriendException(FriendErrorCode.INVALID_RELATIONSHIP_STATE);
        }
    }

    @Transactional
    public void sendFriendRequest(String token, int targetId) {
        int userId = validateTokenAndGetUserId(token);
        validateUsers(userId, targetId);

        int[] normalizedPair = normalizePair(userId, targetId);
        int userId1 = normalizedPair[0];
        int userId2 = normalizedPair[1];

        relationshipRepository.findByUserId1AndUserId2(userId1, userId2)
                .ifPresent(r -> {
                    throw new FriendException(FriendErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
                });

        Relationship relationship = Relationship.builder()
                .userId1(userId1)
                .userId2(userId2)
                .initiatorId(userId)
                .status(0) // pending
                .build();

        relationshipRepository.save(relationship);
    }

    @Transactional
    public void acceptFriendRequest(String token, int requesterId) {
        int userId = validateTokenAndGetUserId(token);
        validateUsers(userId, requesterId);

        int[] pair = normalizePair(userId, requesterId);
        Relationship relationship = findRelationship(pair[0], pair[1]);

        validateRelationshipState(relationship, 0);

        if (relationship.getInitiatorId() == userId) {
            throw new FriendException(FriendErrorCode.FRIEND_REQUEST_INVALID_ACTION);
        }

        relationship.setStatus(1); // accepted
        relationshipRepository.save(relationship);
    }

    @Transactional
    public void declineFriendRequest(String token, int requesterId) {
        int userId = validateTokenAndGetUserId(token);
        validateUsers(userId, requesterId);

        int[] pair = normalizePair(userId, requesterId);
        Relationship relationship = findRelationship(pair[0], pair[1]);

        validateRelationshipState(relationship, 0);

        relationshipRepository.delete(relationship);
    }

    @Transactional
    public void cancelFriendRequest(String token, int targetId) {
        int userId = validateTokenAndGetUserId(token);
        validateUsers(userId, targetId);

        int[] pair = normalizePair(userId, targetId);
        Relationship relationship = findRelationship(pair[0], pair[1]);

        if (relationship.getStatus() != 0) {
            throw new FriendException(FriendErrorCode.INVALID_RELATIONSHIP_STATE);
        }

        if (relationship.getInitiatorId() != userId) {
            throw new FriendException(FriendErrorCode.FRIEND_REQUEST_INVALID_ACTION);
        }

        relationshipRepository.delete(relationship);
    }

    @Transactional
    public void unfriend(String token, int friendId) {
        int userId = validateTokenAndGetUserId(token);
        validateUsers(userId, friendId);

        int[] pair = normalizePair(userId, friendId);
        Relationship relationship = findRelationship(pair[0], pair[1]);

        if (relationship.getStatus() != 1) {
            throw new FriendException(FriendErrorCode.INVALID_RELATIONSHIP_STATE);
        }

        relationshipRepository.delete(relationship);
    }

    @Transactional
    public void blockUser(String token, int targetId) {
        int userId = validateTokenAndGetUserId(token);
        validateUsers(userId, targetId);

        if (userId == targetId) {
            throw new FriendException(FriendErrorCode.BLOCK_SELF_NOT_ALLOWED);
        }

        int[] normalizedPair = normalizePair(userId, targetId);
        int userId1 = normalizedPair[0];
        int userId2 = normalizedPair[1];

        Optional<Relationship> existingRel = relationshipRepository.findByUserId1AndUserId2(userId1, userId2);

        if (existingRel.isPresent()) {
            Relationship rel = existingRel.get();
            if (rel.getStatus() == 2) {
                throw new FriendException(FriendErrorCode.USER_ALREADY_BLOCKED);
            }
            rel.setStatus(2);
            rel.setInitiatorId(userId);
            relationshipRepository.save(rel);
        } else {
            // Create new blocked relationship
            Relationship relationship = Relationship.builder()
                    .userId1(userId1)
                    .userId2(userId2)
                    .initiatorId(userId)
                    .status(2)
                    .build();
            relationshipRepository.save(relationship);
        }
    }

    @Transactional
    public void unblockUser(String token, int targetId) {
        int userId = validateTokenAndGetUserId(token);
        validateUsers(userId, targetId);

        int[] pair = normalizePair(userId, targetId);
        Relationship relationship = findRelationship(pair[0], pair[1]);

        if (relationship.getStatus() != 2) {
            throw new FriendException(FriendErrorCode.USER_NOT_BLOCKED);
        }

        if (relationship.getInitiatorId() != userId) {
            throw new FriendException(FriendErrorCode.FRIEND_REQUEST_INVALID_ACTION);
        }

        relationshipRepository.delete(relationship);
    }



    public List<Map<String, Object>> getSentFriendRequests(String token) {
        int userId = validateTokenAndGetUserId(token);

        return relationshipRepository.findByInitiatorIdAndStatus(userId, 0)
                .stream()
                .map(rel -> mapRelationshipToResponse(rel, userId))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getReceivedFriendRequests(String token) {
        int userId = validateTokenAndGetUserId(token);

        return relationshipRepository.findByUserIdAndStatusAndNotInitiator(userId, 0)
                .stream()
                .map(rel -> mapRelationshipToResponse(rel, userId))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getFriends(String token) {
        int userId = validateTokenAndGetUserId(token);

        return relationshipRepository.findFriendsByUserId(userId)
                .stream()
                .map(rel -> mapRelationshipToResponse(rel, userId))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getBlockedUsers(String token) {
        int userId = validateTokenAndGetUserId(token);

        return relationshipRepository.findBlockedUsersByUserId(userId)
                .stream()
                .map(rel -> mapRelationshipToResponse(rel, userId))
                .collect(Collectors.toList());
    }
    private Map<String, Object> mapRelationshipToResponse(Relationship rel, int currentUserId) {
        int otherUserId = getOtherUserId(rel, currentUserId);
        User otherUser = findUserById(otherUserId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("relationshipId", rel.getId());
        response.put("userId", otherUser.getId());
        response.put("username", otherUser.getUsername());
        response.put("fullname", otherUser.getFullname());
        response.put("initiatorId", rel.getInitiatorId());
        response.put("status", rel.getStatus());
        response.put("createdAt", rel.getCreatedAt());

        return response;
    }


    private int getOtherUserId(Relationship rel, int currentUserId) {
        return rel.getUserId1() == currentUserId ? rel.getUserId2() : rel.getUserId1();
    }

}