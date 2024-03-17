package ch.nexusnet.usermanager.aws.dynamodb.model.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Setter;

@DynamoDBTable(tableName = "UserInfo")
public class UserInfo {
    @Setter
    private String id;
    @Setter
    private String username;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String birthday;
    @Setter
    private String bio;
    @Setter
    private String university;
    @Setter
    private String degreeProgram;


    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return username;
    }

    @DynamoDBAttribute(attributeName = "firstname")
    public String getFirstName() {
        return firstName;
    }

    @DynamoDBAttribute(attributeName = "lastname")
    public String getLastName() {
        return lastName;
    }

    @DynamoDBAttribute(attributeName = "birthday")
    public String getBirthday() {
        return birthday;
    }

    @DynamoDBAttribute(attributeName = "bio")
    public String getBio() {
        return bio;
    }

    @DynamoDBAttribute(attributeName = "university")
    public String getUniversity() {
        return university;
    }

    @DynamoDBAttribute(attributeName = "degreeprogram")
    public String getDegreeProgram() {
        return degreeProgram;
    }
}
