package com.example.unionsystem.util;

import com.example.unionsystem.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static String getCurrentUserRole() {
        User user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    public static boolean isAdmin() {
        String role = getCurrentUserRole();
        return "ADMIN".equals(role);
    }

    public static boolean isClubManager() {
        String role = getCurrentUserRole();
        return "CLUB_MANAGER".equals(role);
    }
}
