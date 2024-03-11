package ch.nexusnet.usermanager.aws.dynamodb.model.mapper;

import ch.nexusnet.usermanager.aws.dynamodb.model.table.UserInfo;
import org.openapitools.model.User;

public class UserToUserInfoMapper {

    private UserToUserInfoMapper() {}

    /**
     * Maps a User object to a UserInfo object.
     *
     * @param user The source User object.
     * @return A UserInfo object populated with user's data.
     */
    public static UserInfo map(User user) {
        UserInfo userInfo = new UserInfo();

        if (user.getId() != null) {
            userInfo.setId(user.getId().toString());
        }

        userInfo.setUsername(user.getUsername());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());

        // Handle JsonNullable fields and other transformations as necessary
        if (user.getBirthday().isPresent()) {
            // Convert LocalDate to String. You might need to adjust the format as per your requirement.
            userInfo.setBirthday(user.getBirthday().get().toString());
        }

        userInfo.setBio(user.getBio().orElse(null));
        userInfo.setUniversity(user.getUniversity());

        userInfo.setDegreeProgram(user.getDegreeProgram().orElse(null));

        // Since not all properties of User are directly mappable to UserInfo,
        // you should only map those that have a corresponding field in UserInfo.

        return userInfo;
    }
}
