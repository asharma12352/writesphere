package com.example.writesphere.service;

import com.example.writesphere.model.User;
import com.example.writesphere.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void loginSucceedsWithCorrectPassword() {
        User stored = new User();
        stored.setUsername("anjany");
        stored.setPassword(passwordEncoder.encode("secret123"));
        when(userRepository.findByUsername("anjany")).thenReturn(stored);

        assertTrue(userService.loginUser("anjany", "secret123"));
    }

    @Test
    void loginFailsWithWrongPassword() {
        User stored = new User();
        stored.setUsername("anjany");
        stored.setPassword(passwordEncoder.encode("secret123"));
        when(userRepository.findByUsername("anjany")).thenReturn(stored);

        assertFalse(userService.loginUser("anjany", "wrongpassword"));
    }

    @Test
    void loginFailsWhenUserDoesNotExist() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertFalse(userService.loginUser("ghost", "anything"));
    }

    @Test
    void registerHashesThePasswordBeforeSaving() {
        User input = new User();
        input.setUsername("anjany");
        input.setPassword("secret123");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.registerUser(input);

        assertNotEquals("secret123", saved.getPassword());
        assertTrue(passwordEncoder.matches("secret123", saved.getPassword()));
    }
}