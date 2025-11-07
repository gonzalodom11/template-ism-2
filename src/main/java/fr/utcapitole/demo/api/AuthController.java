package fr.utcapitole.demo.api;

import fr.utcapitole.demo.api.dto.RegisterRequest;
import fr.utcapitole.demo.entities.User;
import fr.utcapitole.demo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}, allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/register", produces = "text/plain")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (userRepository.existsById(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        // store an encoded password when possible
        if (request.getPassword() != null) {
            try {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            } catch (Exception e) {
                // fallback to plain if encoder not available
                user.setPassword(request.getPassword());
            }
        }
        user.setRoles(request.getRoles());
        user.setEmail(request.getEmail());
        if (request.getBirthday() != null && !request.getBirthday().trim().isEmpty()) {
            try {
                LocalDate ld = LocalDate.parse(request.getBirthday());
                user.setBirthday(ld.atStartOfDay().atOffset(ZoneOffset.UTC));
            } catch (Exception e) {
                // ignore invalid date format, keep birthday null
            }
        }

        userRepository.saveAndFlush(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping(value = "/usernames", produces = "application/json")
    public List<String> getUsernames() {
        return userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());
    }

    @PostMapping(value = "/login", produces = "text/plain")
    public ResponseEntity<String> login(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required");
        }
        return userRepository.findById(request.getUsername())
                .map(user -> {
                    String stored = user.getPassword();
                    boolean matches = false;
                    try {
                        matches = passwordEncoder.matches(request.getPassword(), stored);
                    } catch (Exception ignored) {
                        // ignore encoder issues
                    }
                    if (!matches) {
                        matches = stored != null && stored.equals(request.getPassword());
                    }
                    if (matches && user.isAccountNonLocked()) {
                        return ResponseEntity.ok("Login successful");
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }
}