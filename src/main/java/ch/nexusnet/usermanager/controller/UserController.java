package ch.nexusnet.usermanager.controller;

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

import java.net.URI;


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
    public ResponseEntity<Void> uploadProfilePicture(String userId, MultipartFile profilePicture) {
        return UsersApi.super.uploadProfilePicture(userId, profilePicture);
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
