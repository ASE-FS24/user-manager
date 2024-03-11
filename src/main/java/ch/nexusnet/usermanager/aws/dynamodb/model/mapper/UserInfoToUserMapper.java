package ch.nexusnet.usermanager.aws.dynamodb.model.mapper;

import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.model.User;

import java.time.LocalDate;
import java.util.UUID;

public class UserInfoToUserMapper {

    private UserInfoToUserMapper() {}

    /**
     * Maps a UserInfo Object to a User object
     *
     * @param userInfo The source UserInfo object
     * @return A User object populated with userInfo's data.
     */
    public static User map(UserInfo userInfo) {
        User user = new User();

        // Assuming userInfo.id is a valid UUID string.
        user.setId(UUID.fromString(userInfo.getId()));

        user.setUsername(userInfo.getUsername());
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());

        // Assuming birthday in UserInfo is stored in ISO-8601 format (e.g., "2000-01-01").
        // You should add proper error handling for parsing.
        if (userInfo.getBirthday() != null && !userInfo.getBirthday().isEmpty()) {
            user.setBirthday(JsonNullable.of(LocalDate.parse(userInfo.getBirthday())));
        } else {
            user.setBirthday(JsonNullable.undefined());
        }

        user.setBio(JsonNullable.of(userInfo.getBio()));
        user.setUniversity(userInfo.getUniversity());
        user.setDegreeProgram(JsonNullable.of(userInfo.getDegreeProgram()));

        // Set other properties as needed, handling JsonNullable and format conversions
        // For properties not existing in UserInfo, you can set them to undefined or default values

        return user;
    }
}