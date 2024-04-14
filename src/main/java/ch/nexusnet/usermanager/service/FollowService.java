package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserInfoToUserSummaryMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.Follow;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.FollowRepository;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.UserInfoRepository;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.UserSummary;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserService userService;

    public Follow followUser(String userId, String followsUserId) throws UserNotFoundException {
        throwExceptionIfUserDoesNotExist(userId);
        throwExceptionIfUserDoesNotExist(followsUserId);
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowsUserId(followsUserId);
        return followRepository.save(follow);
    }

    public void unfollowUser(String userId, String followsUserId) {
        Follow follow = followRepository.findByUserIdAndFollowsUserId(userId, followsUserId);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    public List<UserSummary> getFollows(String userId) {
        List<Follow> followIds = followRepository.findByUserId(userId);
        return getUserSummariesFromFollows(followIds, false);
    }

    public List<UserSummary> getFollowers(String userId) {
        List<Follow> followIds = followRepository.findByFollowsUserId(userId);
        return getUserSummariesFromFollows(followIds, true);
    }

    private List<UserSummary> getUserSummariesFromFollows(List<Follow> follows, boolean followers) {
        // Extract user IDs from follows and fetch UserInfo objects in bulk
        List<String> userIds;
        if (followers) {
            userIds = follows.stream()
                    .map(Follow::getUserId)
                    .collect(Collectors.toList());
        } else {
            userIds = follows.stream()
                    .map(Follow::getFollowsUserId)
                    .collect(Collectors.toList());
        }
        Iterable<UserInfo> usersInfo = userInfoRepository.findAllById(userIds);

        List<UserSummary> userSummaries = new ArrayList<>();
        usersInfo.forEach(element -> userSummaries.add(createUserSummary(element)));
        return userSummaries;
    }

    private UserSummary createUserSummary(UserInfo userInfo) {
        UserSummary userSummary = UserInfoToUserSummaryMapper.map(userInfo);
        URL url = userService.getProfilePicture(userInfo.getId());
        if (url != null) {
            userSummary.setProfilePicture(url.toString());
        }
        return userSummary;
    }

    private void throwExceptionIfUserDoesNotExist(String userId) throws UserNotFoundException {
        if (! userInfoRepository.existsById(userId)) {
            String userInformationMessage = getUserNotFoundByIdMessage(userId);
            log.info(userInformationMessage);
            throw new UserNotFoundException(userInformationMessage);
        }
    }

    private String getUserNotFoundByIdMessage(String userId) {
        return "User with user id " + userId + " was not found.";
    }
}