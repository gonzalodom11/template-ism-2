package fr.utcapitole.demo.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_details")
public class User implements UserDetails {
    @Id
    private String username;
    private String password;
    private String roles;
    private OffsetDateTime birthday;
    private Boolean locked;
    private String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (null != roles) {
            return Arrays.stream(roles.split(","))
                    .filter(role -> role.length() > 0)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked==null?true:!locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Optional<OffsetDateTime> getBirthday() {
        return null==birthday?Optional.empty():Optional.of(birthday);
    }

    public String getEmail() {
        return email;
    }

    public void setBirthday(OffsetDateTime birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
