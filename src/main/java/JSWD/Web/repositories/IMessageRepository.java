package JSWD.Web.repositories;

import JSWD.Web.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMessageRepository extends JpaRepository<Message, Long> {

}
