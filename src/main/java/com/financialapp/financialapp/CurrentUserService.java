package com.financialapp.financialapp;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof ApplicationUser user) {
            return user.getUserId();
        }
        throw new RuntimeException("No authenticated user");
    }

    public ApplicationUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof ApplicationUser user) {
            return user;
        }
        throw new RuntimeException("No authenticated user");
    }

}
