package JSWD.Web.sockets;
import JSWD.Web.model.security.user.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import JSWD.Web.model.chatSpecific.Message;
import JSWD.Web.model.chatSpecific.Topic;
import JSWD.Web.model.comunication.JsonPayload;
import JSWD.Web.repositories.SecurityAuth.RefreshTokenRepository;
import JSWD.Web.repositories.SecurityAuth.RegularTokenRepository;
import JSWD.Web.repositories.SecurityAuth.UserInformationRepository;
import JSWD.Web.repositories.chatSpecific.IMessageRepository;
import JSWD.Web.repositories.SecurityAuth.UserRepository;
import JSWD.Web.repositories.chatSpecific.TopicRepository;
import JSWD.Web.service.MessageService;
import JSWD.Web.service.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

public class WebSocketHandler extends AbstractWebSocketHandler {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
    private final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class.getName());
    private final IMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final UserInformationRepository userInformationRepository;
    private final MessageService messageService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RegularTokenRepository regularTokenRepository;

    private Map<Long, User> userCache = new HashMap<>();
    private List<WebSocketSession> activeUsersSesions= new ArrayList<>();


    public WebSocketHandler(IMessageRepository messageRepository, UserRepository userRepository, TopicRepository topicRepository, UserInformationRepository userInformationRepository , RefreshTokenRepository refreshTokenRepository, RegularTokenRepository regularTokenRepository  ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.userInformationRepository = userInformationRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.regularTokenRepository = regularTokenRepository;
        this.messageService = new MessageService(messageRepository, userRepository, topicRepository, userInformationRepository);
        this.jwtService = new JwtService(regularTokenRepository, refreshTokenRepository, userRepository);

    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var payload = convertJsonToPayload(message.getPayload());
        if (jwtService.isUserTokenValid(payload)) {
            messageService.saveMessage(payload, session, activeUsersSesions);
        }
        else {
            activeUsersSesions.remove(session);
            session.close();
        }
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection established");

        URI uri = session.getUri();

        List<NameValuePair> params = URLEncodedUtils.parse(uri, Charset.forName("UTF-8"));

        // Find the "topic" parameter
        String topicName = null;
        for (NameValuePair param : params) {
            if (param.getName().equals("topic")) {
                topicName = param.getValue();
                break;
            }
        }

        // Check if the topic name is present
        if (topicName == null || topicName.isEmpty()) {
            logger.error("No topic name provided in the query parameters");
            session.close();
            return;
        }

        Topic topic =  topicRepository.findByTopic(topicName).get();

        List<Message> messageList = topic.getMessages().stream().toList();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonMessages = objectMapper.writeValueAsString(messageList);

        List<String> rawMessages = new ArrayList<>();
        for (Message message : messageList) {
            User user = userCache.get((long)message.getSenderId());
            if (user == null) {
                user = userRepository.findById((long)message.getSenderId()).get();
                userCache.put(user.getId(), user);
            }
            rawMessages.add(user.getUsername() + ":" + message.getMessage());
        }

        TextMessage textMessage = new TextMessage(rawMessages.toString());
        activeUsersSesions.add(session);
        session.sendMessage(textMessage);
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
