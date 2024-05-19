package JSWD.Web.sockets;
import JSWD.Web.model.comunication.JsonPayload;
import JSWD.Web.repositories.SecurityAuth.UserInformationRepository;
import JSWD.Web.repositories.chatSpecific.IMessageRepository;
import JSWD.Web.repositories.SecurityAuth.UserRepository;
import JSWD.Web.repositories.chatSpecific.TopicRepository;
import JSWD.Web.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;


public class WebSocketHandler extends AbstractWebSocketHandler {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
    private final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class.getName());
    private final IMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final UserInformationRepository userInformationRepository;
    private final MessageService messageService;

    public WebSocketHandler(IMessageRepository messageRepository, UserRepository userRepository, TopicRepository topicRepository, UserInformationRepository userInformationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.userInformationRepository = userInformationRepository;
        this.messageService = new MessageService(messageRepository, userRepository, topicRepository, userInformationRepository);

    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var payload = convertJsonToPayload(message.getPayload());
        messageService.saveMessage(payload, session);

    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection established");
    }




    public JsonPayload convertJsonToPayload(String jsonInput) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonPayload jsonPayload = null;
        try {
            jsonPayload = objectMapper.readValue(jsonInput, JsonPayload.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonPayload;
    }

}
