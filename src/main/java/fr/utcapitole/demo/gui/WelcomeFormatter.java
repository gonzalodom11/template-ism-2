package fr.utcapitole.demo.gui;

import fr.utcapitole.demo.entities.User;
import org.springframework.stereotype.Service;

@Service
public class WelcomeFormatter {

    public String welcomeMessage(User user) {
        return "Welcome to Q&A " + user.getUsername() + "!";
    }

}
