package io.canduer.nexusflow.auth.Impl;

import io.canduer.nexusflow.entity.User;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<Permission> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).toList();
//        return permissions.stream().map(p-> new SimpleGrantedAuthority(p.getName().name())).toList();
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(role -> {

            authorities.add(
                    new SimpleGrantedAuthority(
                            role.getRoleName().name()));

            role.getPermissions().forEach(permission ->
                    authorities.add(
                            new SimpleGrantedAuthority(
                                    permission.getName().name())));
        });

        /*
        ROLE_USER
        CUSTOMER_READ
        INVOICE_READ
        */
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
