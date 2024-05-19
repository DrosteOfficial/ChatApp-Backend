package JSWD.Web.sockets;

import JSWD.Web.repositories.SecurityAuth.UserInformationRepository;
import JSWD.Web.repositories.chatSpecific.IMessageRepository;
import JSWD.Web.repositories.SecurityAuth.UserRepository;
import JSWD.Web.repositories.chatSpecific.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IMessageRepository messageRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserInformationRepository userInformationRepository;


    public WebsocketConfiguration(UserRepository userRepository, IMessageRepository messageRepository, TopicRepository topicRepository, UserInformationRepository userInformationRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.topicRepository = topicRepository;
        this.userInformationRepository = userInformationRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(messageRepository,userRepository,topicRepository,userInformationRepository), "/websocket")
                .setAllowedOrigins("*");
    }
}
