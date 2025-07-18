package com.example.swp.controller;

import com.example.swp.DTO.UserRegisterDTO;
import com.example.swp.entity.Role;
import com.example.swp.entity.Token;
import com.example.swp.entity.User;
import com.example.swp.repository.RoleRepository;
import com.example.swp.repository.TokenRepository;
import com.example.swp.repository.UserRepository;
import com.example.swp.service.EmailService;
import com.example.swp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class UserController {
    UserService userService;
    RoleRepository roleRepository;
    EmailService emailService;
    TokenRepository tokenRepository;
    UserRepository userRepository;

    @Autowired
    public  UserController(UserService userService, EmailService emailService, RoleRepository roleRepository, TokenRepository tokenRepository, UserRepository userRepository){
        this.userService = userService;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new UserRegisterDTO());
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // lấy session hiện tại, không tạo mới nếu chưa có
        if (session != null) {
            session.invalidate(); // xóa toàn bộ session
        }
        return "redirect:/home"; // nên redirect thay vì trả về tên view trực tiếp
    }

    @PostMapping("/editprofile")
    public String editprofile( @RequestParam("userId") int userId,
                               @RequestParam("fullName") String fullName,
                               @RequestParam("phone") String phone,
                               @RequestParam("address") String address,
                               @RequestParam("email") String email,
                               @RequestParam("gender") String gender,
                               @RequestParam("dob") String dobStr,
                               @RequestParam("password") String password,
                               @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                               HttpSession session) {

        User existingUser = userService.findByEmail(email);
        if (existingUser != null && existingUser.getUserId() != userId) {
            session.setAttribute("notification", "Email đã tồn tại");
            return "redirect:/home";
        }
        String base64Avatar = null;
        try {
            User user = userService.findByUserId(userId);

            if (avatar != null && !avatar.isEmpty()) {
                byte[] bytes = avatar.getBytes();
                base64Avatar = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            } else {
                // Nếu không upload ảnh mới thì lấy ảnh cũ
                base64Avatar = user.getAvatar();
            }
            if (dobStr != null && !dobStr.isEmpty()) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date dob = formatter.parse(dobStr);
                    user.setDob(dob);
                } catch (ParseException e) {
                    user.setDob(user.getDob());
                }
            }
            user.setFullName(fullName);
            user.setPhoneNumber(phone);
            user.setEmail(email);
            user.setPassword(password);
            user.setGender(gender);
            user.setAddress(address);
            user.setAvatar(base64Avatar);
            userService.save(user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("fullName", user.getFullName());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("roleId", user.getRole().getRoleId());
            session.setAttribute("roleName", user.getRole().getRoleName());
            session.setAttribute("dob", user.getDob());
            session.setAttribute("avatar", user.getAvatar());
            session.setAttribute("address", user.getAddress());
            session.setAttribute("password", user.getPassword());
            session.setAttribute("gender", user.getGender());
            session.setAttribute("phoneNumber", user.getPhoneNumber());
            session.setAttribute("isActive", user.getIsActive());

            session.setAttribute("notification", "Cập nhật thông tin thành công !");
        }
        catch  (IOException e) {
            e.printStackTrace();
            session.setAttribute("notification", "Có lỗi xảy ra khi xử lý ảnh.");
        }
        return "redirect:/home"; // nên redirect thay vì trả về tên view trực tiếp
    }

    @PostMapping("/changepassword")
    public String changepassword( @RequestParam("userId") int userId,
                                  @RequestParam("old_pass") String old_pass,
                                  @RequestParam("new_pass1") String new_pass1,
                                  @RequestParam("new_pass2") String new_pass2,
                                  HttpSession session) {

        User user = userService.findByUserId(userId);
        if(!user.getPassword().equals(old_pass)){
            session.setAttribute("notification", "Mật khẩu cũ sai");
            return "redirect:/home"; // nên redirect thay vì trả về tên view trực tiếp
        }
        else{
            user.setPassword(new_pass1);
            userService.save(user);
            session.setAttribute("notification", "Thay đổi mật khẩu thành công! Hãy đăng nhập lại");
            // Xóa tất cả session attributes ngoại trừ 'notification'
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attrName = attributeNames.nextElement();
                if (!"notification".equals(attrName)) {
                    session.removeAttribute(attrName);
                }
            }            return "redirect:/login";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerDTO") UserRegisterDTO userRegisterDTO,  HttpSession session, Model model) {

        LocalDate today = LocalDate.now();
        LocalDate dob = userRegisterDTO.getDob().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        Period age = Period.between(dob, today);
        if (age.getYears() < 3) {
            model.addAttribute("dobError", "Người dùng phải từ 3 tuổi trở lên.");
            return "register";
        }

        // Kiểm tra email và số điện thoại tồn tại
        if (userService.existsByEmail(userRegisterDTO.getEmail())) {
            model.addAttribute("emailError", "Email này đã tồn tại.");
            return "register";
        }
        if (userService.existsByPhoneNumber(userRegisterDTO.getPhoneNumber())) {
            model.addAttribute("phoneError", "Số điện thoại đã tồn tại.");
            return "register";
        }

        Optional<Role> defaultRole = roleRepository.findById(5);

        // Sinh OTP
        String randomPassword = emailService.generateRandom();
        emailService.sendEmail(userRegisterDTO.getEmail(), randomPassword);

        User user = new User();
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(randomPassword); // nên mã hóa nếu không dùng NoOp
        user.setFullName(userRegisterDTO.getFullName());
        user.setPhoneNumber(userRegisterDTO.getPhoneNumber());
        user.setAddress(userRegisterDTO.getAddress());
        user.setDob(userRegisterDTO.getDob());
        user.setIsActive(true);
        user.setRole(defaultRole.get());
        userService.save(user);
        session.setAttribute("notification", "Đăng ký thành công. Chúng tôi đã gửi mật khẩu đăng nhập tới email: " + user.getEmail());
        return  "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Email không tồn tại!");
            return "forgot-password";
        }

        // Tạo token đặt lại mật khẩu
        String token = userService.createPasswordResetToken(user);
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;

        emailService.sendSimpleMail(email, "Reset Password", "Click the link to reset password: " + resetUrl);
        model.addAttribute("notification", "Link đặt lại mật khẩu đã được gửi đến email của bạn thành công.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || token.isEmpty()) {
            model.addAttribute("notification", "Missing or invalid token!");
            return "forgot-password";
        }
        Optional<Token> resetToken = tokenRepository.findByToken(token);
        if (resetToken.isEmpty() || resetToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("notification", "Token không hợp lệ hoặc đã hết hạn!");
            return "forgot-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam(name = "token", required = true) String token,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (token == null) {
            model.addAttribute("notification", "Missing required token.");
            return "forgot-password";
        }
        Token resetToken = tokenRepository.findByToken(token).orElse(null);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("notification", "Token không hợp lệ hoặc đã hết hạn!");
            return "forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("notification", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("token", token);
            return "reset-password";
        }

        User user = resetToken.getUser();
        user.setPassword(newPassword);
        userService.save(user);
        model.addAttribute("notification", "Mật khẩu đã được thay đổi thành công!");
        tokenRepository.delete(resetToken); // Xóa token sau khi sử dụng
        return "login";
    }

    @GetMapping("/manage-user")
    public String getManageUserPage(@RequestParam(name = "keyword", required = false) String keyword,
                                    @RequestParam(name = "roleId", required = false) Integer roleId,Model model) {

        List<User> users;

        if ((keyword == null || keyword.trim().isEmpty()) && roleId == null) {
            // Trường hợp: Không có keyword và roleId → lấy tất cả trừ roleId = 1
            users = userRepository.findByRole_RoleIdNot(1);
        } else if ((keyword == null || keyword.trim().isEmpty())) {
            // Có roleId, không có keyword
            users = roleId == 1
                    ? new ArrayList<>()
                    : userRepository.findByRole_RoleId(roleId);
        } else if (roleId == null) {
            // Có keyword, không có roleId → search email, trừ roleId = 1
            users = userRepository.findByEmailContainingAndRole_RoleIdNot(keyword.trim(), 1);
        } else {
            // Có cả keyword và roleId
            users = userRepository.findByEmailContainingAndRole_RoleId(keyword.trim(), roleId);
        }

        List<Role> roles = roleRepository.findAll()
                .stream()
                .filter(role -> role.getRoleId() != 1)
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("roleId", roleId);
        model.addAttribute("roles", roles);
        return "admin/manage-user";
    }

    @PostMapping("/toggle-user")
    public String toggleUserStatus(@RequestParam("userId") Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsActive(!Boolean.TRUE.equals(user.getIsActive())); // đảo trạng thái
            userRepository.save(user);
        }
        return "redirect:/manage-user";
    }

    @PostMapping("/create-user")
    public String createUser(@RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String phoneNumber,
                             @RequestParam String password,
                             @RequestParam Integer roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setRole(role);
        user.setIsActive(true);
        userRepository.save(user);
        return "redirect:/manage-user";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute User updatedUser) {
        Optional<User> optional = userRepository.findById(updatedUser.getUserId());
        if (optional.isPresent()) {
            User user = optional.get();
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setDob(updatedUser.getDob());
            user.setGender(updatedUser.getGender());
            user.setAddress(updatedUser.getAddress());
            userRepository.save(user);
        }
        return "redirect:/manage-user";
    }
}
