package JSWD.Web.model.security.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 120)
    private String password;
    private boolean active;
    private boolean locked;
    private Instant accountExpireDate;
    private Instant credentialsExpireDate;
    private Instant createTimestamp;


    public UserCredentials() {
    }

    public UserCredentials(String password, Instant createTimestamp) {
        this.password = password;
        this.createTimestamp = createTimestamp;
        this.active = true;
    }

    public UserCredentials(String password, boolean active, boolean locked, Instant accountExpireDate, Instant credentialsExpireDate, Instant createTimestamp) {
        this.password = password;
        this.active = active;
        this.locked = locked;
        this.accountExpireDate = accountExpireDate;
        this.credentialsExpireDate = credentialsExpireDate;
        this.createTimestamp = createTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Instant getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Instant accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Instant getCredentialsExpireDate() {
        return credentialsExpireDate;
    }

    public void setCredentialsExpireDate(Instant credentialsExpireDate) {
        this.credentialsExpireDate = credentialsExpireDate;
    }

    public Instant getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Instant createTimestamp) {
        this.createTimestamp = createTimestamp;
    }


    @Override
    public String toString() {
        return "UserCredentials{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", locked=" + locked +
                ", accountExpireDate=" + accountExpireDate +
                ", credentialsExpireDate=" + credentialsExpireDate +
                ", createTimestamp=" + createTimestamp +
                '}';
    }
}

