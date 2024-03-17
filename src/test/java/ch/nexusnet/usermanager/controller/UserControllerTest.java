package ch.nexusnet.usermanager.controller;

import ch.nexusnet.usermanager.service.UserService;
import ch.nexusnet.usermanager.service.exceptions.UserAlreadyExistsException;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenNewUserIsCreated_expectStatusCodeCreated() {
        // arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        when(userService.createUser(any(User.class))).thenReturn(user);

        // act
        ResponseEntity<User> response = userController.createUser(new User());

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void whenUserAlreadyExists_expectStatusCodeBadRequest() {
        // arrange
        when(userService.createUser(any(User.class))).thenThrow(new UserAlreadyExistsException("User Already Exists"));

        // act
        ResponseEntity<User> response = userController.createUser(new User());

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void retrieveUser_expectOk() {
        // arrange
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John", "Doe", "jhnd");
        when(userService.getUserByUserId(userId.toString())).thenReturn(user);

        // act
        ResponseEntity<User> response = userController.getUserById(userId.toString());

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void whenUserIdIsNotRegistered_expectNotFound() {
        // arrange
        String wrongUserId = UUID.randomUUID().toString();
        when(userService.getUserByUserId(wrongUserId)).thenThrow(new UserNotFoundException("User Not Found"));

        // act
        ResponseEntity<User> response = userController.getUserById(wrongUserId);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}