package JSWD.Web.sockets;

import JSWD.Web.repositories.SecurityAuth.RefreshTokenRepository;
import JSWD.Web.repositories.SecurityAuth.RegularTokenRepository;
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
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private RegularTokenRepository regularTokenRepository;


    public WebsocketConfiguration(UserRepository userRepository, IMessageRepository messageRepository, TopicRepository topicRepository, UserInformationRepository userInformationRepository, RefreshTokenRepository refreshTokenRepository, RegularTokenRepository regularTokenRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.topicRepository = topicRepository;
        this.userInformationRepository = userInformationRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.regularTokenRepository = regularTokenRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(messageRepository, userRepository, topicRepository, userInformationRepository, refreshTokenRepository, regularTokenRepository), "/websocket")
                .setAllowedOrigins("*");
    }
}
