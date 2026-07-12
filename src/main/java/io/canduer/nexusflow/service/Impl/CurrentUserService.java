package io.canduer.nexusflow.service.Impl;

import io.canduer.nexusflow.auth.Impl.CustomUserDetails;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.exception.ResourceNotFoundException;
import io.canduer.nexusflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {

        //NOTE:authentication.getPrincipal()-> CustomUserDetails
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails customUserDetails =  (CustomUserDetails) authentication.getPrincipal();

        String email = customUserDetails.getUsername();
        return userRepository
                .findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with this email :"+email));
    }
}
