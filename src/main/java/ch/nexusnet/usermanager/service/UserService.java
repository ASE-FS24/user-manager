package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserInfoToUserMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserToUserInfoMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.UserInfoRepository;
import ch.nexusnet.usermanager.service.exceptions.UserAlreadyExistsException;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.UpdateUser;
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
        String userInformationMessage = getUserNotFoundByUserNameMessage(userInfo.get().getUsername());
        log.info(userInformationMessage);
        throw new UserAlreadyExistsException(userInformationMessage);
    }

    public User getUserByUserId(String userId) throws UserNotFoundException {
        Optional<UserInfo> userInfo = findUserById(userId);
        if (userInfo.isPresent()) {
            return mapUserInfoToUser(userInfo.get());
        }
        String userInformationMessage = getUserNotFoundByIdMessage(userId);
        log.info(userInformationMessage);
        throw new UserNotFoundException(userInformationMessage);
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<UserInfo> userInfo = findUserByUsername(username);
        if (userInfo.isPresent()) {
            return mapUserInfoToUser(userInfo.get());
        }
        String userInformationMessage = getUserNotFoundByUserNameMessage(username);
        log.info(userInformationMessage);
        throw new UserNotFoundException(userInformationMessage);
    }

    public void updateUser(String userId, UpdateUser updateUser) throws UserNotFoundException {
        Optional<UserInfo> optionalUserInfo = findUserById(userId);
        if (optionalUserInfo.isEmpty()) {
            String userInformationMessage = getUserNotFoundByIdMessage(userId);
            log.info(userInformationMessage);
            throw new UserNotFoundException(userInformationMessage);
        }
        UserInfo userInfo = optionalUserInfo.get();
        updateUserInfo(updateUser, userInfo);
        saveUserToDB(mapUserInfoToUser(userInfo));
    }

    public void deleteUser(String userId) throws UserNotFoundException {
        if (! userInfoRepository.existsById(userId)) {
            String userInformationMessage = getUserNotFoundByIdMessage(userId);
            log.info(userInformationMessage);
            throw new UserNotFoundException(userInformationMessage);
        }
        userInfoRepository.deleteById(userId);
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

    private String getUserNotFoundByIdMessage(String userId) {
        return "User with user id " + userId + " was not found.";
    }

    private String getUserNotFoundByUserNameMessage(String username) {
        return "User with username " + username + " was not found.";
    }

    private void updateUserInfo(UpdateUser updateUser, UserInfo userInfo) {
        if (updateUser.getFirstName() != null) {
            userInfo.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            userInfo.setLastName(updateUser.getLastName());
        }
        if (updateUser.getUsername() != null) {
            userInfo.setUsername(updateUser.getUsername());
        }
        if (updateUser.getUniversity() != null) {
            userInfo.setUniversity(updateUser.getUniversity());
        }
        if (updateUser.getBio() != null) {
            userInfo.setBio(updateUser.getBio());
        }
        if (updateUser.getDegreeProgram() != null) {
            userInfo.setDegreeProgram(updateUser.getDegreeProgram());
        }
        if (updateUser.getBirthday() != null) {
            userInfo.setBirthday(updateUser.getBirthday().toString());
        }
    }
}
