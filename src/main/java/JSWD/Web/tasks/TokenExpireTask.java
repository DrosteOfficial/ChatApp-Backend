package JSWD.Web.tasks;

import org.slf4j.Logger;
import JSWD.Web.repositories.SecurityAuth.RefreshTokenRepository;
import JSWD.Web.repositories.SecurityAuth.RegularTokenRepository;
import JSWD.Web.service.security.JwtService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@EnableAsync
@Component
public class TokenExpireTask {
    private final Logger log = LoggerFactory.getLogger(TokenExpireTask.class);
    @Autowired
    private RegularTokenRepository regularTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtService jwtService;


    public TokenExpireTask(RegularTokenRepository regularTokenRepository, RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.regularTokenRepository = regularTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Async
    @Scheduled(fixedRate = 1 , timeUnit = TimeUnit.MINUTES )
    public void checkForExpiredTokens(){
        log.info( "Checking for expired tokens");
         var tokens = this.regularTokenRepository.findAllNotExpired();
         if(tokens.isEmpty()){
             log.info("No regular tokens found!");
             return;
         }
        tokens.forEach(token -> {
            if(!token.isRevoked() && jwtService.isTokenExpired(token.getToken())){
                log.info("Token expired: " + token.getToken());
                token.setExpired(true);
                token.setExpiredTime(Instant.now());
                this.regularTokenRepository.save(token);
            }
        });
    }

    @Async
    @Scheduled(fixedRate = 1 , timeUnit = TimeUnit.MINUTES )
    public void checkForRevokedTokens(){
        log.info( "No refresh tokens found");
        var foundTokens = this.refreshTokenRepository.findAllNotRevoked();
        if (foundTokens.isEmpty()){
            log.info("No tokens found!");
            return;
        }
        foundTokens.forEach(token -> {
            if(!token.isExpired() && jwtService.isTokenExpired(token.getToken())){
                log.info("Token revoked: " + token.getToken());
                token.setExpired(true);
                token.setExpiredTime(Instant.now());
                this.refreshTokenRepository.save(token);
            }
        });
    }

}
