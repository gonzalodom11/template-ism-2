package fr.utcapitole.demo.gui.cucumber;

import fr.utcapitole.demo.entities.User;
import fr.utcapitole.demo.repositories.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test-only configuration that seeds users required by Cucumber scenarios.
 * Named differently from the main SecurityConfig to avoid bean name clashes.
 */
@TestConfiguration
public class TestSecuritySeedConfig {

    @Bean
    public User seedJohnDoe(PasswordEncoder encoder, UserRepository userRepository) {
        return userRepository.save(createUser(
                "john_doe",
                encoder.encode("foo"),
                "USER"));
    }

    @Bean
    public User seedApiUser(PasswordEncoder encoder, UserRepository userRepository) {
        return userRepository.save(createUser(
                "api",
                encoder.encode("api"),
                "API"));
    }

    @Bean
    public User seedGonzalo(PasswordEncoder encoder, UserRepository userRepository) {
        return userRepository.save(createUser(
                "gonzalodm10",
                encoder.encode("gonzalodm10"),
                "USER"));
    }

    private User createUser(String username, String password, String roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }
}
