package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserInfoToUserMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserToUserInfoMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.UserInfoRepository;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.openapitools.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;

    public User createUser(User newUser) {
        if (newUser.getId() == null) {
            newUser.setId(UUID.randomUUID());
        }

        Optional<UserInfo> newUserInfo = findUserById(newUser.getId().toString());
        if (newUserInfo.isEmpty()) {
            UserInfo createdUserInfo = saveUserToDB(newUser);
            return mapUserInfoToUser(createdUserInfo);
        }
        return mapUserInfoToUser(newUserInfo.get());
    }

    public User getUserByUserId(String userId) throws UserNotFoundException {
        Optional<UserInfo> userInfo = findUserById(userId);
        if (userInfo.isPresent()) {
            return mapUserInfoToUser(userInfo.get());
        }
        throw new UserNotFoundException("UserNotFound");
    }

    private Optional<UserInfo> findUserById(String userId) {
        return userInfoRepository.findById(userId);
    }
    private UserInfo saveUserToDB(User user) {
        return userInfoRepository.save(mapUserToUserInfo(user));
    }

    private UserInfo mapUserToUserInfo(User user) {
        return UserToUserInfoMapper.map(user);
    }

    private User mapUserInfoToUser(UserInfo userInfo) {
        return UserInfoToUserMapper.map(userInfo);
    }
}
