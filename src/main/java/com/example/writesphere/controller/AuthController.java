package com.example.writesphere.controller;

import com.example.writesphere.model.User;
import com.example.writesphere.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Handle login form
    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpServletRequest request,
                            HttpSession session,
                            Model model) {
        boolean success = userService.loginUser(username, password);
        if (success) {
            User user = userService.findByUsername(username);
            // Invalidate old session first then create a new one
            session.invalidate();
            session = request.getSession(true);
            session.setAttribute("loggedInUser", user);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid username or password!");
            return "login";
        }
    }

    // Show register page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Handle register form
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        // Check if username already exists
        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Username already taken!");
            return "register";
        }
        // Check if email already exists
        if (userService.findByEmail(email) != null) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userService.registerUser(user);
        return "redirect:/login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}