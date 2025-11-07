package fr.utcapitole.demo.service;

import fr.utcapitole.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private MailService mailService;

    public UserService(UserRepository userRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public void lockAccount(String username) {
        userRepository.findById(username).ifPresent(user -> {
            if (user.isAccountNonLocked()) {
                user.setLocked(true);
                userRepository.saveAndFlush(user);
                mailService.sendEmail(user.getEmail(), "Sorry, your account has been locked");
            }
        });
    }

    public void unlockAccount(String username) {
        userRepository.findById(username).ifPresent(user -> {
            if (!user.isAccountNonLocked()) {
                user.setLocked(false);
                userRepository.saveAndFlush(user);
                mailService.sendEmail(user.getEmail(), "Good news, your account has been unlocked!");
            }
        });
    }
}
