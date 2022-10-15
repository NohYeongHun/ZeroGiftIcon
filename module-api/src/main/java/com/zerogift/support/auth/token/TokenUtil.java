package com.zerogift.support.auth.token;

import com.zerogift.support.auth.userdetails.AdminInfo;
import com.zerogift.support.auth.userdetails.MemberInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {
    public static String getAdminEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        try {
            email = ((AdminInfo) auth.getPrincipal()).getEmail();
        } catch (Exception e) {
            return email;
        }
        return email;
    }

    public static String getMemberEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        try {
            email = ((MemberInfo) auth.getPrincipal()).getEmail();
        } catch (Exception e) {
            return email;
        }
        return email;
    }

    public static String getAdminOrMemberEmail() {
        String email = getAdminEmail();
        return email == null ? getMemberEmail() : email;
    }
}
