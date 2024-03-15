package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserInfoToUserMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserToUserInfoMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.UserInfoRepository;
import ch.nexusnet.usermanager.service.exceptions.UserAlreadyExistsException;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserInfoRepository userInfoRepository;

    public User createUser(User newUser) throws UserAlreadyExistsException {
        if (newUser.getId() == null) {
            newUser.setId(UUID.randomUUID());
        }

        Optional<UserInfo> userInfo = findUserByUsername(newUser.getUsername());
        if (userInfo.isEmpty()) {
            UserInfo createdUserInfo = saveUserToDB(newUser);
            return mapUserInfoToUser(createdUserInfo);
        }
        String userInformationMessage = "User with username " + userInfo.get().getUsername() + " already exists.";
        log.info(userInformationMessage);
        throw new UserAlreadyExistsException(userInformationMessage);
    }

    public User getUserByUserId(String userId) throws UserNotFoundException {
        Optional<UserInfo> userInfo = findUserById(userId);
        if (userInfo.isPresent()) {
            return mapUserInfoToUser(userInfo.get());
        }
        String userInformationMessage = "User with user id " + userId + " was not found.";
        log.info(userInformationMessage);
        throw new UserNotFoundException(userInformationMessage);
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<UserInfo> userInfo = findUserByUsername(username);
        if (userInfo.isPresent()) {
            return mapUserInfoToUser(userInfo.get());
        }
        String userInformationMessage = "User with username " + username + " was not found.";
        log.info(userInformationMessage);
        throw new UserNotFoundException(userInformationMessage);
    }

    private Optional<UserInfo> findUserById(String userId) {
        return userInfoRepository.findById(userId);
    }

    private Optional<UserInfo> findUserByUsername(String username) {
        return userInfoRepository.findUserInfoByUsername(username);
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
