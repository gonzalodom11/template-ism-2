package fr.utcapitole.demo.batch;

import fr.utcapitole.demo.entities.Category;
import fr.utcapitole.demo.entities.Question;

import java.util.Arrays;
import java.util.List;

class BatchTest {

    public static void main(String[] args) {
        Batch.run(questions());
    }

    private static List<Question> questions() {
        Category java = new Category("java");
        Category spring = new Category("spring");
        Category tdd = new Category("tdd");
        Category springBoot = new Category("spring-boot");
        return Arrays.asList(
                question("user-1", java),
                question("user-1", tdd),
                question("user-1", spring),
                question("user-2", java),
                question("user-2", java),
                question("user-3", spring),
                question("user-3", tdd),
                question("user-3", java),
                question("user-3", java),
                question("user-3", tdd),
                question("user-4", spring),
                question("user-4", springBoot),
                question("user-4", springBoot),
                question("user-4", java),
                question("user-4", java)
        );
    }

    private static Question question(String author, Category category) {
        Question question = new Question();
        question.setAuthor(author);
        question.setCategory(category);
        return question;
    }
}
