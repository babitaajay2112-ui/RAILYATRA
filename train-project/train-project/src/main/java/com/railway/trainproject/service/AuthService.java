package com.railway.trainproject.service;
import com.railway.trainproject.dto.RegisterRequest;
import com.railway.trainproject.entity.User;
import com.railway.trainproject.repository.UserRepository;
import com.railway.trainproject.exception.DuplicateResourceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole("USER");

        userRepository.save(user);

        return "Registration successful";
    }
}
