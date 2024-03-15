package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserToUserInfoMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.UserInfoRepository;
import ch.nexusnet.usermanager.service.exceptions.UserAlreadyExistsException;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.User;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserInfoRepository userInfoRepositoryMock;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("getUsers")
    void createNewUser_expectSuccess(User testUser) {
        // arrange
        when(userInfoRepositoryMock.findUserInfoByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userInfoRepositoryMock.save(any(UserInfo.class))).thenReturn(UserToUserInfoMapper.map(getUserWithId()));

        // act
        User createdUser = userService.createUser(testUser);

        // assert
        assertNotNull(createdUser.getId());
    }

    @ParameterizedTest
    @MethodSource("getUsers")
    void createNewUser_expectFailure(User testUser) {
        // arrange
        when(userInfoRepositoryMock.findUserInfoByUsername(any(String.class))).thenReturn(Optional.of(UserToUserInfoMapper.map(testUser)));

        // act & assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(testUser));
    }

    @Test
    void getUserByUserId_expectSuccess() {
        // arrange
        User testUser = getUserWithId();
        when(userInfoRepositoryMock.findById(any(String.class))).thenReturn(Optional.of(UserToUserInfoMapper.map(testUser)));

        // act
        User foundUser = userService.getUserByUserId(testUser.getId().toString());

        // assert
        assertEquals(testUser.getId(), foundUser.getId());
    }

    @Test
    void getUserByUserId_expectFailure() {
        // arrange
        when(userInfoRepositoryMock.findById(any(String.class))).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUserId(UUID.randomUUID().toString()));
    }

    @Test
    void getUserByUsername_expectSuccess() {
        // arrange
        User testUser = getUserWithId();
        when(userInfoRepositoryMock.findUserInfoByUsername(any(String.class))).thenReturn(Optional.of(UserToUserInfoMapper.map(testUser)));

        // act
        User foundUser = userService.getUserByUsername(testUser.getUsername());

        // assert
        assertEquals(testUser.getId(), foundUser.getId());
    }

    @Test
    void getUserByUsername_expectFailure() {
        // arrange
        when(userInfoRepositoryMock.findUserInfoByUsername(any(String.class))).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nonexistent"));
    }

    private static Stream<Arguments> getUsers() {
        return Stream.of(
            Arguments.of(getUserWithId()),
            Arguments.of(getUserWithoutId())
        );
    }

    public static User getUserWithoutId() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("jhnd");
        return user;
    }
    private static User getUserWithId() {
        User user = getUserWithoutId();
        user.setId(UUID.randomUUID());
        return user;
    }
}