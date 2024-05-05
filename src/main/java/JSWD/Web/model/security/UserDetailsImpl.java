package JSWD.Web.model.security;

import JSWD.Web.model.security.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String nickname;
    private final String email;
    private final boolean active;
    private final boolean locked;
    private final Instant accountExpireDate;
    private final Instant credentialsExpireDate;
    private final Collection<? extends GrantedAuthority> authorities;
    @JsonIgnore
    private String password;

    public UserDetailsImpl(Long id, String nickname, String email, boolean active, boolean locked, Instant accountExpireDate, Instant credentialsExpireDate, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.active = active;
        this.locked = locked;
        this.accountExpireDate = accountExpireDate;
        this.credentialsExpireDate = credentialsExpireDate;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpireDate == null || Instant.now().isAfter(accountExpireDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpireDate == null || Instant.now().isAfter(credentialsExpireDate);
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

}
