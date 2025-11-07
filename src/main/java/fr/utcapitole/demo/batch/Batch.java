package fr.utcapitole.demo.batch;

import fr.utcapitole.demo.entities.Category;
import fr.utcapitole.demo.entities.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Batch {

    public static Map<String, String> run(List<Question> questions) {
        Map<Category, List<Question>> byCategory = new HashMap<>();
        for (Question question :
                questions) {
            if (!byCategory.containsKey(question.getCategory())) {
                byCategory.put(question.getCategory(), new ArrayList<>());
            }
        }
        for (Question question :
                questions) {
            byCategory.get(question.getCategory()).add(question);
        }
        Map<String, String> result = new HashMap<>();
        for (Category c : byCategory.keySet()) {
            Map<String, List<Question>> byAuthor = new HashMap<>();
            for (Question question :
                    byCategory.get(c)) {
                if (!byAuthor.containsKey(question.getAuthor())) {
                    byAuthor.put(question.getAuthor(), new ArrayList<>());
                }
            }
            for (Question question :
                    byCategory.get(c)) {
                byAuthor.get(question.getAuthor()).add(question);
            }
            String contributor = null;
            int contributions = 0;
            for (String author : byAuthor.keySet()) {
                if (byAuthor.get(author).size() > contributions) {
                    contributions = byAuthor.get(author).size();
                    contributor = author;
                }
            }
            result.put(c.getId(), contributor);
        }
        return result;
    }
}
