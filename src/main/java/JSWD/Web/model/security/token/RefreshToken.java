package JSWD.Web.model.security.token;

import JSWD.Web.model.security.user.UserCredentials;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinTable(name = "user_id")
    private UserCredentials userCredentials;


    @NotNull
    @Column(length = 500, unique = true)
    private String token;

    private boolean expired;

    @NotNull
    private Instant expiredTime;

    private boolean revoked;

    @Nullable
    private Instant revokedTime;

    private Instant timestamp;


    public RefreshToken() {
    }

    public RefreshToken(Long id, UserCredentials userCredentials, String token, boolean expired, Instant expiredTime, boolean revoked, @Nullable Instant revokedTime, Instant timestamp) {
        this.id = id;
        this.userCredentials = userCredentials;
        this.token = token;
        this.expired = expired;
        this.expiredTime = expiredTime;
        this.revoked = revoked;
        this.revokedTime = revokedTime;
        this.timestamp = timestamp;
    }

    public RefreshToken(UserCredentials userCredentials, String token, Instant timestamp) {
        this.userCredentials = userCredentials;
        this.token = token;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Instant getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Instant expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    @Nullable
    public Instant getRevokedTime() {
        return revokedTime;
    }

    public void setRevokedTime(@Nullable Instant revokedTime) {
        this.revokedTime = revokedTime;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
