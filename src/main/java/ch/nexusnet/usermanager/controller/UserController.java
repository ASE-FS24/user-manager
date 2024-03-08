package ch.nexusnet.usermanager.controller;

import org.openapitools.api.UsersApi;
import org.openapitools.model.NewUser;
import org.openapitools.model.UpdateUser;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UserController implements UsersApi {

    @Override
    public ResponseEntity<User> createUser(NewUser newUser) {
        return UsersApi.super.createUser(newUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(String userId) {
        return UsersApi.super.deleteUser(userId);
    }

    @Override
    public ResponseEntity<User> getUserById(String userId) {
        return UsersApi.super.getUserById(userId);
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
