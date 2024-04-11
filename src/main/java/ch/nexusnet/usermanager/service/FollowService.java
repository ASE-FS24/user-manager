package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.table.Follow;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.FollowRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepository;

    public void followUser(String userId, String followsUserId) {
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowsUserId(followsUserId);
        followRepository.save(follow);
    }

    public void unfollowUser(String userId, String followsUserId) {
        Follow follow = followRepository.findByUserIdAndFollowsUserId(userId, followsUserId);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    public List<String> getFollowers(String followsUserId) {
        return followRepository.findByFollowsUserId(followsUserId)
                .stream()
                .map(Follow::getUserId)
                .collect(Collectors.toList());
    }

    public List<String> getFollowing(String userId) {
        return followRepository.findByUserId(userId)
                .stream()
                .map(Follow::getFollowsUserId)
                .collect(Collectors.toList());
    }
}