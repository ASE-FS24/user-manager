package ch.nexusnet.usermanager.controller;

import ch.nexusnet.usermanager.aws.s3.exceptions.UnsupportedFileTypeException;
import ch.nexusnet.usermanager.service.UserService;
import ch.nexusnet.usermanager.service.FollowService;
import ch.nexusnet.usermanager.service.exceptions.UserAlreadyExistsException;
import ch.nexusnet.usermanager.service.exceptions.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UpdateUser;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;
    private final FollowService followService;
    private static final String SERVICE_NOT_AVAILABLE = "Service not available.";

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
        return uploadFileAndRetrieveFileLocation(userId, profilePicture);
    }

    @Override
    public ResponseEntity<String> getProfilePicture(String userId) {
        try {
            URL location = userService.getProfilePicture(userId);
            return ResponseEntity.ok().body(location.toURI().toString());

        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().body(SERVICE_NOT_AVAILABLE);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<String> uploadResume(String userId, MultipartFile resume) {
        return uploadFileAndRetrieveFileLocation(userId, resume);
    }

    @Override
    public ResponseEntity<String> getResume(String userId) {
        try {
            URL location = userService.getResume(userId);
            return ResponseEntity.ok().body(location.toURI().toString());

        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().body(SERVICE_NOT_AVAILABLE);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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

    @NotNull
    private ResponseEntity<String> uploadFileAndRetrieveFileLocation(String userId, MultipartFile resume) {
        try {
            URL location = userService.uploadFile(userId, resume);
            return ResponseEntity.created(location.toURI()).build();

        } catch (IOException | URISyntaxException e) {
            return ResponseEntity.internalServerError().body(SERVICE_NOT_AVAILABLE);
        } catch (UnsupportedFileTypeException e) {
            return ResponseEntity.badRequest().body("Unsupported file type.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users/{userId}/follow/{userToFollowId}")
    @NotNull
    public ResponseEntity<Void> followUser(@PathVariable String userId, @PathVariable String userToFollowId) {
        // TODO: Prevent an user from following itself
        // TODO: Check if an user is already following another user
        try {
            followService.followUser(userId, userToFollowId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users/{userId}/followers")
    @NotNull
    public ResponseEntity<List<User>> getFollowers(@PathVariable String userId) {
        try {
            List<String> followerIds = followService.getFollowers(userId);
            List<User> followers = followerIds.stream()
                    .map(userService::getUserByUserId)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(followers);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO remove follower

    //TODO get follows

    //TODO add documentation and tests
}
