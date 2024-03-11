package ch.nexusnet.usermanager.aws.dynamodb.model.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Setter;

@DynamoDBTable(tableName = "Follow")
public class Follow {
    @Setter
    private String userId;
    @Setter
    private String followsUserId;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    @DynamoDBAttribute(attributeName = "followsUserId")
    public String getFollowsUserId() {
        return followsUserId;
    }
}
