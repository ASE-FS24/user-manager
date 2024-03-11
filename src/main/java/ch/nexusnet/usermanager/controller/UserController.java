package ch.nexusnet.usermanager.controller;

import ch.nexusnet.usermanager.service.UserService;
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
        User user = userService.createUser(newUser);
        URI location = URI.create("/users/" + user.getId());
        return ResponseEntity.created(location).body(user);
    }

    @Override
    public ResponseEntity<Void> deleteUser(String userId) {
        return UsersApi.super.deleteUser(userId);
    }

    @Override
    public ResponseEntity<User> getUserById(String userId) {
        User user;
        try {
            user = userService.getUserByUserId(userId);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @Override
    public ResponseEntity<Void> updateUser(String userId, UpdateUser updateUser) {
        return UsersApi.super.updateUser(userId, updateUser);
    }

    @Override
    public ResponseEntity<Void> uploadProfilePicture(String userId, MultipartFile profilePicture) {
        return UsersApi.super.uploadProfilePicture(userId, profilePicture);
    }

    @Override
    public ResponseEntity<Void> uploadResume(String userId, MultipartFile resume) {
        return UsersApi.super.uploadResume(userId, resume);
    }
}
