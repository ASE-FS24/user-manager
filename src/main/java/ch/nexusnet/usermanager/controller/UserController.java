package ch.nexusnet.usermanager.controller;

import ch.nexusnet.usermanager.aws.s3.exceptions.UnsupportedFileTypeException;
import ch.nexusnet.usermanager.service.UserService;
import ch.nexusnet.usermanager.service.exceptions.UserAlreadyExistsException;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UpdateUser;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


@Controller
@AllArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<User> createUser(User newUser) {
        User user;
        try {
            user = userService.createUser(newUser);
            URI location = URI.create("/users/" + user.getId());
            return ResponseEntity.created(location).body(user);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(String userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<User> getUserById(String userId) {
        User user;
        try {
            user = userService.getUserByUserId(userId);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> updateUser(String userId, UpdateUser updateUser) {
        try {
            userService.updateUser(userId, updateUser);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<String> uploadProfilePicture(String userId, MultipartFile profilePicture) {
        try {
            URL location = userService.uploadProfilePicture(userId, profilePicture);
            return ResponseEntity.created(location.toURI()).build();

        } catch (IOException | URISyntaxException e) {
            return ResponseEntity.internalServerError().body("Service not available.");
        } catch (UnsupportedFileTypeException e) {
            return ResponseEntity.badRequest().body("Unsupported file type.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<String> getProfilePicture(String userId) {
        try {
            URL location = userService.getProfilePicture(userId);
            return ResponseEntity.ok().body(location.toURI().toString());

        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().body("Service not available.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> uploadResume(String userId, MultipartFile resume) {
        return UsersApi.super.uploadResume(userId, resume);
    }

    @Override
    public ResponseEntity<User> getUserByUsername(String username) {
        User user;
        try {
            user = userService.getUserByUsername(username);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
