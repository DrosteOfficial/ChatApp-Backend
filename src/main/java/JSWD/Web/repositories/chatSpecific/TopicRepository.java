package JSWD.Web.repositories.chatSpecific;

import JSWD.Web.model.chatSpecific.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTopic(String name);
}
