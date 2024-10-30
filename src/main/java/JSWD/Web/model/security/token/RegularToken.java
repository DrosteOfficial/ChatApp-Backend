package JSWD.Web.model.security.token;

import JSWD.Web.model.security.user.UserCredentials;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;
@Entity
@Table(name = "regular_token")
public class RegularToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserCredentials userCredentials;

    @NotNull
    @Column(length = 500, unique = true)
    private String token;

    private boolean expired;

    @Nullable
    private Instant expiredTime;

    private boolean revoked;

    @Nullable
    private Instant revokedTime;

    private Instant timestamp;

    public RegularToken() {
    }

    public RegularToken(UserCredentials userCredentials, @NotNull String token, Instant timestamp) {
        this.userCredentials = userCredentials;
        this.token = token;
        this.timestamp = timestamp;
    }

    public RegularToken(Long id, UserCredentials userCredentials, @NotNull String token, boolean expired, @Nullable Instant expiredTime, boolean revoked, @Nullable Instant revokedTime, Instant timestamp) {
        this.id = id;
        this.userCredentials = userCredentials;
        this.token = token;
        this.expired = expired;
        this.expiredTime = expiredTime;
        this.revoked = revoked;
        this.revokedTime = revokedTime;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserCredentials getUser() {
        return userCredentials;
    }

    public void setUser(UserCredentials userCredentials) {
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

    @Nullable
    public Instant getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(@Nullable Instant expiredTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegularToken token1)) {
            return false;
        }
        return expired == token1.expired && revoked == token1.revoked && Objects.equals(id, token1.id) && Objects.equals(userCredentials, token1.userCredentials) && Objects.equals(token, token1.token) && Objects.equals(expiredTime, token1.expiredTime) && Objects.equals(revokedTime, token1.revokedTime) && Objects.equals(timestamp, token1.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userCredentials, token, expired, expiredTime, revoked, revokedTime, timestamp);
    }

    @Override
    public String toString() {
        return "Token{"
                + "id=" + id
                + ", user=" + userCredentials
                + ", token='" + token + '\''
                + ", expired=" + expired
                + ", expiredTime=" + expiredTime
                + ", revoked=" + revoked
                + ", revokedTime=" + revokedTime
                + ", timestamp=" + timestamp
                + '}';
    }
}
