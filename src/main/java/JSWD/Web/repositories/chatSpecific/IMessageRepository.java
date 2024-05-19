package JSWD.Web.repositories.chatSpecific;

import JSWD.Web.model.chatSpecific.Message;
import JSWD.Web.model.chatSpecific.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface IMessageRepository extends JpaRepository<Message, Long> {
}
