package ch.nexusnet.usermanager.aws.dynamodb.model.table;

import ch.nexusnet.usermanager.UsermanagerApplication;
import ch.nexusnet.usermanager.aws.dynamodb.repositories.FollowRepository;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 *      This class is used to test the functionality of DynamoDb-related repositories. It uses a local instance of
 *      DynamoDB to run the tests on. It uses a Junit4 runner, since it is recommended in the documentation.
 * </p>
 * <p>
 *      !! DOCKER NEEDS TO BE RUNNING ON YOUR MACHINE FOR THE TESTS TO WORK !!
 * </p>
 * <p>
 *      This test is disabled so that the automated CI/CD pipeline is not using up too much time. To run it locally,
 *      remove the '@Disabled' annotation bellow.
 * </p>
 * @see <a href=https://www.baeldung.com/spring-data-dynamodb>Baeldung Documentation for DynamoDB and Spring Boot</a>
 */
@Disabled
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UsermanagerApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=test1",
        "amazon.aws.secretkey=test231" })
public class FollowIntegrationTest {
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    FollowRepository followRepository;


    @ClassRule
    public static GenericContainer<?> dynamoDB =
            new GenericContainer<>(DockerImageName.parse("amazon/dynamodb-local:latest"))
                    .withExposedPorts(8000);

    @Before
    public void setup() throws Exception {
        // Configure the AmazonDynamoDB client
        String endpoint = String.format("http://%s:%d",
                dynamoDB.getHost(),
                dynamoDB.getFirstMappedPort());

        amazonDynamoDB.setEndpoint(endpoint);

        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(Follow.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

        dynamoDBMapper.batchDelete(
                (List<Follow>)followRepository.findAll());
    }

    @Test
    public void basicEntryCreation_expectSuccess() {
        String uuid = "de005d7c-f36f-4342-9c2c-380b1815b499";
        String followId = "11111";
        Follow follow = new Follow();
        follow.setUserId(uuid);
        follow.setFollowsUserId(followId);

        followRepository.save(follow);

        List<Follow> result = followRepository.findByUserId(uuid);

        assertEquals(1, result.size());
        assertEquals(result.get(0).getFollowsUserId(), followId);
    }

}