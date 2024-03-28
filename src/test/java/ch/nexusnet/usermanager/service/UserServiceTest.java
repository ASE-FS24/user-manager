package ch.nexusnet.usermanager.service;

import ch.nexusnet.usermanager.aws.dynamodb.model.mapper.UserToUserInfoMapper;
import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.UserInfoRepository;
import ch.nexusnet.usermanager.service.exceptions.UserAlreadyExistsException;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.UpdateUser;
import org.openapitools.model.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @ParameterizedTest
    @MethodSource("getUpdateUsers")
    void updateUser_expectSuccess(UpdateUser updateUser) {
        // arrange
        User testUser = getUserWithId();

        UserInfo expectedUpdatedUser;
        if (updateUser.getFirstName() != null) {
            expectedUpdatedUser = getExpectedUserInfo(updateUser);
        } else {
            expectedUpdatedUser = new UserInfo();
            expectedUpdatedUser.setFirstName(testUser.getFirstName());
            expectedUpdatedUser.setLastName(testUser.getLastName());
            expectedUpdatedUser.setUsername(testUser.getUsername());
        }

        when(userInfoRepositoryMock.findById(any(String.class))).thenReturn(Optional.of(UserToUserInfoMapper.map(testUser)));

        ArgumentCaptor<UserInfo> captor = ArgumentCaptor.forClass(UserInfo.class);

        // act
        userService.updateUser(testUser.getId().toString(), updateUser);

        // assert
        verify(userInfoRepositoryMock).save(captor.capture());
        assertEquals(expectedUpdatedUser.getFirstName(), captor.getValue().getFirstName());
        assertEquals(expectedUpdatedUser.getLastName(), captor.getValue().getLastName());
        assertEquals(expectedUpdatedUser.getUsername(), captor.getValue().getUsername());
        assertEquals(expectedUpdatedUser.getUniversity(), captor.getValue().getUniversity());
        assertEquals(expectedUpdatedUser.getBio(), captor.getValue().getBio());
        assertEquals(expectedUpdatedUser.getDegreeProgram(), captor.getValue().getDegreeProgram());
        assertEquals(expectedUpdatedUser.getBirthday(), captor.getValue().getBirthday());
    }

    @Test
    void updateNonExistentUser_expectFailure() {
        // arrange
        UpdateUser updateUser = getUpdateUser();
        when(userInfoRepositoryMock.findById(any(String.class))).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser("nonexistent", updateUser));
    }

    @Test
    void deleteUser_expectSuccess() {
        // arrange
        User testUser = getUserWithId();
        when(userInfoRepositoryMock.existsById(any(String.class))).thenReturn(true);

        // act
        userService.deleteUser(testUser.getId().toString());

        // assert
        verify(userInfoRepositoryMock, times(1)).deleteById(testUser.getId().toString());
    }

    @Test
    void deleteNonExistentUser_expectFailure() {
        // arrange
        // arrange
        User testUser = getUserWithId();
        when(userInfoRepositoryMock.existsById(any(String.class))).thenReturn(false);

        // act & assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("nonexistent"));
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

    private static Stream<Arguments> getUpdateUsers() {
        return Stream.of(
                Arguments.of(getUpdateUser()),
                Arguments.of(new UpdateUser())
        );
    }

    private static UpdateUser getUpdateUser() {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Max");
        updateUser.setLastName("Muserman");
        updateUser.setUsername("mamu");
        updateUser.setUniversity("ETH");
        updateUser.setBio("Master Student");
        updateUser.setDegreeProgram("AI");
        updateUser.setBirthday(LocalDate.of(2024, 3, 28));
        return updateUser;
    }

    @NotNull
    private static UserInfo getExpectedUserInfo(UpdateUser updateUser) {
        UserInfo expectedUpdatedUser = new UserInfo();
        expectedUpdatedUser.setFirstName(updateUser.getFirstName());
        expectedUpdatedUser.setLastName(updateUser.getLastName());
        expectedUpdatedUser.setUsername(updateUser.getUsername());
        expectedUpdatedUser.setUniversity(updateUser.getUniversity());
        expectedUpdatedUser.setBio(updateUser.getBio());
        expectedUpdatedUser.setDegreeProgram(updateUser.getDegreeProgram());
        expectedUpdatedUser.setBirthday(updateUser.getBirthday().toString());
        return expectedUpdatedUser;
    }
}