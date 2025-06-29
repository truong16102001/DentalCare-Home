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
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("fullName", user.getFullName());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("roleId", user.getRole().getRoleId());
            session.setAttribute("roleName", user.getRole().getRoleName());
            session.setAttribute("dob", user.getDob());
            session.setAttribute("avatar", user.getAvatar());
            session.setAttribute("address", user.getAddress());
            session.setAttribute("password", user.getPassword()); // nên cân nhắc không lưu
            session.setAttribute("gender", user.getGender());
            session.setAttribute("phoneNumber", user.getPhoneNumber());
            session.setAttribute("isActive", user.getIsActive());
        }
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ADMIN")) {
            response.sendRedirect("/admin-manage");
        } else if(roles.contains("MANAGER")) {
            response.sendRedirect("/manager-manage");
        }
        else if(roles.contains("DOCTOR")) {
            response.sendRedirect("/doctor-manage");
        }
        else if(roles.contains("RECEPTIONIST")) {
            response.sendRedirect("/receptionist-manage");
        }
        else{
            response.sendRedirect("/home");
        }
    }

}
