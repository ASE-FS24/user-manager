package ch.nexusnet.usermanager.aws.dynamodb.repositories;

import ch.nexusnet.usermanager.aws.dynamodb.model.table.Follow;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface FollowRepository extends
        CrudRepository<Follow, String> {

    List<Follow> findByUserId(String userId);
}
