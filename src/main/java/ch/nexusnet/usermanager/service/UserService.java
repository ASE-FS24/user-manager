package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserInfoToUserMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserToUserInfoMapper;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.UserInfoRepository;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import lombok.AllArgsConstructor;
import org.openapitools.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;


    public User createUser(User newUser) {
        Optional<UserInfo> newUserInfo = userInfoRepository.findById(newUser.getId().toString());

        if (newUserInfo.isEmpty()) {
            UserInfo createdUserInfo = userInfoRepository.save(mapUserToUserInfo(newUser));
            return mapUserInfoToUser(createdUserInfo);
        }
        return mapUserInfoToUser(newUserInfo.get());

    }

    private UserInfo mapUserToUserInfo(User user) {
        return UserToUserInfoMapper.map(user);
    }

    private User mapUserInfoToUser(UserInfo userInfo) {
        return UserInfoToUserMapper.map(userInfo);
    }
}
