package com.example.Erp.Project.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.Erp.Project.Entity.Role;
import com.example.Erp.Project.Entity.User;
import com.example.Erp.Project.Repository.UserRepository;
import com.example.Erp.Project.Security.JwtUtil;

import lombok.RequiredArgsConstructor;
import com.example.Erp.Project.DTO.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    // ================= REGISTER =================
    // ================= PUBLIC REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (repo.existsByUsername(request.username())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Username already exists"));
        }

        if (request.role() == Role.ADMIN) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "ADMIN registration not allowed"));
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(request.role());

        repo.save(user);

        return ResponseEntity.ok(
                Map.of("message", "User registered as " + request.role())
        );
    }

    // ================= ADMIN CREATE USER =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-user")
    public ResponseEntity<?> createUserByAdmin(@RequestBody RegisterRequest request) {

        if (repo.existsByUsername(request.username())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Username already exists"));
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(request.role()); // ADMIN can assign ANY role

        repo.save(user);

        return ResponseEntity.ok(
                Map.of("message", "User created with role " + request.role())
        );
    }


    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        User dbUser = repo.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }

        // 🔥 INCLUDE ROLE IN JWT
        String token = jwtUtil.generateToken(
                dbUser.getUsername(),
                dbUser.getRole().name()
        );

        return ResponseEntity.ok(
                Map.of("token", token)
        );
    }
}


