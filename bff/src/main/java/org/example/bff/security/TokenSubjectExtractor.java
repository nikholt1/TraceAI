package org.example.bff.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class TokenSubjectExtractor {

    public String extract(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // or throw an exception if you prefer
        }

        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            // for GitHub, the username is in the "login" attribute
            return oauth2User.getAttribute("login");
        }

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            // User is logged in with username/password
            return userDetails.getUsername();
        }

        return authentication.getName(); // fallback to the default name
    }
}