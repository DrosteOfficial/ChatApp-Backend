package JSWD.Web.model.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 120)
    @Column(unique = true)
    private String username;
    private String password;
    @Size(max = 120)
    @Column(unique = true)
    private String email;
    private boolean Active;
    private Instant CreateDate;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Roles> roles;
    @OneToOne
    @JoinColumn(name = "User_Details", referencedColumnName = "id")
    private JSWD.Web.model.UserDetails userDetails;
    @OneToOne
    @JoinColumn(name = "User_Credentials", referencedColumnName = "id")
    private UserCredentials userCredentials;

    public User() {
    }
    public User(String username, String password, String email, JSWD.Web.model.UserDetails userDetails, UserCredentials userCredentials, Roles roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userDetails = userDetails;
        this.userCredentials = userCredentials;
        this.CreateDate = Instant.now();
        this.Active = true;
        if (this.roles == null) {
            this.roles = List.of(roles);
        } else {
            this.roles.add(roles);
        }
    }





    public User(long id, String username, String email, boolean active, boolean locked, Instant accountExpireDate, Instant credentialsExpireDate, String password, List<GrantedAuthority> authorities) {
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

    @Override
    public boolean isAccountNonExpired() {
        return userCredentials.getAccountExpireDate().isAfter(Instant.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return userCredentials.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userCredentials.getCredentialsExpireDate().isAfter(Instant.now());
    }

    @Override
    public boolean isEnabled() {
        return Active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
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

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public Instant getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Instant createDate) {
        CreateDate = createDate;
    }

    public JSWD.Web.model.UserDetails getUser() {
        return userDetails;
    }

    public void setUser(JSWD.Web.model.UserDetails user) {
        this.userDetails = user;
    }

    public static UserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserCredentials().isActive(),
                user.getUserCredentials().isLocked(),
                user.getUserCredentials().getAccountExpireDate(),
                user.getUserCredentials().getCredentialsExpireDate(),
                user.getUserCredentials().getPassword(),
                authorities);
    }

}