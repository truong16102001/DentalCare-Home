package com.example.swp.security;

import com.example.swp.entity.User;
import com.example.swp.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Lưu thông tin người dùng vào session
        HttpSession session = request.getSession();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            session.setAttribute("us", user);
        }
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_MANAGER")) {
            response.sendRedirect("/manager");
        } else if (roles.contains("ROLE_DOCTOR")) {
            response.sendRedirect("/doctor-care");
        } else {
            response.sendRedirect("/home");
        }
    }


}
