package com.example.swp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String notification = (String) session.getAttribute("notification");
            if (notification != null) {
                model.addAttribute("notification", notification);
                session.removeAttribute("notification");
            }
        }
        return "login";
    }

    @GetMapping("/home")
    public String homePage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        model.addAttribute("active", 1);
        if (session != null) {
            String notification = (String) session.getAttribute("notification");
            if (notification != null) {
                model.addAttribute("notification", notification);
                session.removeAttribute("notification");
            }
        }
        return "home";
    }

    @GetMapping("/")
    public String redirectToHome(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String notification = (String) session.getAttribute("notification");
            if (notification != null) {
                model.addAttribute("notification", notification);
                session.removeAttribute("notification");
            }
        }
        return "redirect:/home"; // Chuyển hướng đến trang home
    }


}
