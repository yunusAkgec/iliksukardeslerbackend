package com.example.iliksukardesler.service;

import com.example.iliksukardesler.model.Role;
import com.example.iliksukardesler.model.User;
import com.example.iliksukardesler.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String register(User user) {


        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // Şifreyi encode et ve kullanıcıyı kaydet
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole()==null){
            user.setRole(Role.USER);
        }
        userRepository.save(user);

        // JWT token üret ve döndür
        return jwtTokenProvider.generateToken(user.getUsername(), "ROLE_" + user.getRole());
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not Found.."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }
        return jwtTokenProvider.generateToken(user.getUsername(), "ROLE_" + user.getRole());
    }
}
