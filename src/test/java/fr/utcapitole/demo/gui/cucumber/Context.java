package fr.utcapitole.demo.gui.cucumber;

import fr.utcapitole.demo.jpa.DbUpdates;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.tomcat.util.codec.binary.Base64;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Import(TestSecuritySeedConfig.class)
public class Context {
    private final ServletWebServerApplicationContext webServerApplicationContext;
    private final WebDriver driver;
    private final RestTemplate restTemplate;
    private final DbUpdates dbUpdates;
    private static final String FRONTEND_URL = "http://localhost:3000";

    public Context(ServletWebServerApplicationContext webServerApplicationContext, DbUpdates dbUpdates) {
        this.webServerApplicationContext = webServerApplicationContext;
        this.dbUpdates = dbUpdates;
        this.restTemplate = new RestTemplate();
        this.driver = new ChromeDriver(chromeOptions());
    }

    @After
    public void quit() {
        System.out.println("Quitting web driver");
        driver.quit();
    }

    @Before
    public void before(Scenario scenario) {
        System.out.println("Before scenario '" + scenario.getName() + "'");
        clear();
        seedDefaultQuestion();
    }

    @After
    public void after(Scenario scenario) {
        System.out.println("After scenario '" + scenario.getName() + "'");
    }

    public String buildApplicationUrl(String path) {
        return "http://localhost:" + webServerApplicationContext.getWebServer().getPort() + path;
    }

    public String buildFrontendUrl(String path) {
        return FRONTEND_URL + path;
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    private ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        if (isHeadless()) {
            options.addArguments("--headless");
        }
        options.addArguments("--remote-allow-origins=*");
        return options;
    }

    private boolean isHeadless() {
        return "true".equals(System.getProperty("chrome.headless"));
    }

    public FluentWait<WebDriver> fluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
    }

    public void createCategory(String id) {
        restTemplate.exchange(
                categoryApiUrl() + "/" + id,
                HttpMethod.PUT,
                authorizedHeaders(),
                Void.class);
    }

    public void clear() {
        restTemplate.exchange(
                questionApiUrl() + "/",
                HttpMethod.DELETE,
                authorizedHeaders(),
                Void.class);
        restTemplate.exchange(
                categoryApiUrl() + "/",
                HttpMethod.DELETE,
                authorizedHeaders(),
                Void.class);
        dbUpdates.reset();
    }

    public HttpHeaders createAuthorizationHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    private String categoryApiUrl() {
        return buildApplicationUrl("/api/category");
    }

    private String questionApiUrl() {
        return buildApplicationUrl("/api/question");
    }

    private HttpEntity<Object> authorizedHeaders() {
        return new HttpEntity<>(createAuthorizationHeaders("api", "api"));
    }

    private HttpEntity<Object> authorizedEntity(Object body) {
        return new HttpEntity<>(body, createAuthorizationHeaders("api", "api"));
    }

    private void seedDefaultQuestion() {
        try {
            // Ensure category exists
            createCategory("general");

            Map<String, Object> payload = new HashMap<>();
            payload.put("author", "john_doe");
            payload.put("title", "Seeded test question");
            payload.put("content", "This is a seeded question for automated tests.");
            Map<String, Object> category = new HashMap<>();
            category.put("id", "general");
            payload.put("category", category);

            restTemplate.exchange(
                    questionApiUrl(),
                    HttpMethod.POST,
                    authorizedEntity(payload),
                    Map.class);
        } catch (Exception e) {
            System.out.println("[TEST] Failed to seed default question: " + e.getMessage());
        }
    }
}
