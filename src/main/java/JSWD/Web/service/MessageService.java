package JSWD.Web.service;

import JSWD.Web.model.chatSpecific.Topic;
import JSWD.Web.model.comunication.JsonPayload;
import JSWD.Web.model.security.user.User;
import JSWD.Web.repositories.SecurityAuth.UserInformationRepository;
import JSWD.Web.repositories.SecurityAuth.UserRepository;
import JSWD.Web.repositories.chatSpecific.IMessageRepository;
import JSWD.Web.model.chatSpecific.Message;
import JSWD.Web.repositories.chatSpecific.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private final IMessageRepository messageRepository;
    @Autowired
    private final TopicRepository topicRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserInformationRepository userInformationRepository;


    public MessageService(IMessageRepository messageRepository, UserRepository userRepository, TopicRepository topicRepository, UserInformationRepository userInformationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.userInformationRepository = userInformationRepository;
    }
    @Transactional
    public Optional<List<Message>> getMessages() {
        var Output = messageRepository.findAll().stream().limit(30).toList();
        if (Output.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Output);
    }

    @Transactional
    public Optional<Message> getMessageById(int messageId) {
        if (messageRepository.findById((long) messageId).isEmpty()) {
            return Optional.empty();
        }
        return messageRepository.findById((long) messageId);
    }


    public void saveMessage(JsonPayload payload, WebSocketSession session) throws Exception {
        if (payload == null) {
            log.error("Payload is null");
            return;
        }

        if (payload.getTopic() == null) {
            log.error("Topic is null");
            return;
        }

        Message msg = payload.getMessage();
        messageRepository.save(msg);

        User user;
        try {
            user = userRepository.findById((long) msg.getSenderId()).orElseThrow(() -> new Exception("User not found"));
        } catch (Exception e) {
            log.error("Error fetching user", e);
            return;
        }

        if (topicRepository.findByTopic(payload.getTopic()).isEmpty()){
            topicRepository.save(new Topic(payload.getTopic()));
        }

        Topic topic =  topicRepository.findByTopic(payload.getTopic()).get();
        topic.getMessages().add(msg);
        topicRepository.save(topic);
        user.getUserInformation().getTopics().add(topic);

        userInformationRepository.save(user.getUserInformation());
        userRepository.save(user);
        session.sendMessage(new TextMessage(payload.getMessage().getMessage()));
    }

    public String saveMess(JsonPayload payload)  {
        if (payload == null) {
            log.error("Payload is null");
            return "Payload is null";
        }

        if (payload.getTopic() == null) {
            log.error("Topic is null");
            return "Topic is null";
        }

        Message msg = payload.getMessage();
        messageRepository.save(msg);

        User user;
        try {
            user = userRepository.findById((long) msg.getSenderId()).orElseThrow(() -> new Exception("User not found"));
        } catch (Exception e) {
            log.error("Error fetching user", e);
            return "Error fetching user";
        }

        if (topicRepository.findByTopic(payload.getTopic()).isEmpty()){
            topicRepository.save(new Topic(payload.getTopic()));
        }

        Topic topic =  topicRepository.findByTopic(payload.getTopic()).get();
        topic.getMessages().add(msg);
        topicRepository.save(topic);
        user.getUserInformation().getTopics().add(topic);

        userInformationRepository.save(user.getUserInformation());
        userRepository.save(user);
        return payload.toString();
    }

}
