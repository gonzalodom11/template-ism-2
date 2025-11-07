package fr.utcapitole.demo.gui;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class PrettyDateFormatter {

    public String format(OffsetDateTime target) {
        return "moments ago";
    }

}
