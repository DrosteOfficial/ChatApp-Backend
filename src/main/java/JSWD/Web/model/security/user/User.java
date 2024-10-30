package JSWD.Web.model.security.user;

import JSWD.Web.model.security.Role;
import JSWD.Web.model.security.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 120)
    @Column(unique = true)
    private String username;

    @Size(max = 120)
    private String password;

    @Size(max = 120)
    @Column(unique = true)
    private String email;

    private Instant createDate;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Roles> roles;

    @OneToOne
    @JoinColumn(name = "user_information", referencedColumnName = "id")
    private UserInformation userInformation;

    @OneToOne
    @JoinColumn(name = "user_credentials", referencedColumnName = "id")
    private UserCredentials userCredentials;

    public User() {
    }

    public User(String username, String password, String email, UserInformation userInformation, UserCredentials userCredentials, Roles roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userInformation = userInformation;
        this.userCredentials = userCredentials;
        this.createDate = Instant.now();
        if (this.roles == null) {
            this.roles = List.of(roles);
        } else {
            this.roles.add(roles);
        }
    }

    public User(long id, String username, String email, boolean active, boolean locked, Instant accountExpireDate, Instant credentialsExpireDate, String password, List<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createDate = Instant.now();
        this.password = password;
        this.roles = authorities.stream().map(authority -> new Roles(Role.valueOf(authority.getAuthority()))).collect(Collectors.toList());
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Roles> getRole() {
        return roles;
    }

    public void setRole(List<Roles> role) {
        this.roles = role;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public UserInformation getUser() {
        return userInformation;
    }

    public void setUser(UserInformation user) {
        this.userInformation = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }
}
