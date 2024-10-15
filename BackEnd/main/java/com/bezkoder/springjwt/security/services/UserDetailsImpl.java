package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;

    private String username;

    @Getter
    private String email;

    private String name;

    private String surname;

    private String phone;

    @JsonIgnore
    private String password;

    private boolean isEmailPublic ;
    private boolean isPositionPublic ;
    private boolean isCompanyPublic ;
    private boolean isCVPublic ;
    private boolean isPhonePublic;

    private boolean isNamePublic;

    private boolean isSurnamePublic;


    private String profilePhotoUrl;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getSurname(),
                user.getPhone(),
                user.getPassword(),
                user.isEmailPublic(),
                user.isPositionPublic(),
                user.isCompanyPublic(),
                user.isCVPublic(),
                user.isPhonePublic(),
                user.isNamePublic(),
                user.isSurnamePublic(),

                user.getProfilePhotoUrl(),

                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }


    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }


    public String getSurname() {
        return surname;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public boolean isCompanyPublic() {
        return isCompanyPublic;
    }

    public boolean isCVPublic() {
        return isCVPublic;
    }

    public boolean isEmailPublic() {
        return isEmailPublic;
    }

    public boolean isNamePublic() {
        return isNamePublic;
    }

    public boolean isPhonePublic() {
        return isPhonePublic;
    }

    public boolean isPositionPublic() {
        return isPositionPublic;
    }

    public boolean isSurnamePublic() {
        return isSurnamePublic;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
