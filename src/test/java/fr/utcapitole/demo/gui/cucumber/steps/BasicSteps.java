package fr.utcapitole.demo.gui.cucumber.steps;

import fr.utcapitole.demo.gui.cucumber.Context;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class BasicSteps {
    public static final int DRIVER_WAIT_TIMEOUT_SECONDS = 2;

    private final Context context;

    public BasicSteps(Context context) {
        this.context = context;
    }

    @Given("the categories are")
    public void theCategoriesAre(DataTable categories) {
        categories.asList().forEach(context::createCategory);
    }

    @Then("the user may choose a category among")
    public void theUserMayChooseACategoryAmong(DataTable categories) {
        context.fluentWait().until(
                presenceOfElementLocated(By.xpath("//select[@id='category']"))
                        .andThen(link -> {
                            List<WebElement> options = context.getWebDriver()
                                    .findElements(By.xpath("//select[@id='category']/option"));
                            List<String> actualCategories = options.stream().map(WebElement::getText).toList();
                            System.out.println(actualCategories);
                            String[] expectedCategories = new String[categories.values().size()];
                            categories.values().toArray(expectedCategories);
                            assertThat(actualCategories).containsExactlyInAnyOrder(expectedCategories);
                            return true;
                        }));
    }

    @When("the user navigates to {string}")
    public void theClientNavigatesTo(String path) {
        context.getWebDriver().navigate().to(context.buildFrontendUrl(path));
    }

    @Then("the page contains a link {string} to {string}")
    public void thePageContainsLink(String text, String path) {
        context.fluentWait().until(
                presenceOfElementLocated(By.xpath("//a[contains(text(),'" + text + "')]"))
                        .andThen(link -> {
                            System.out.println("Found link " + text + ": " + link.getAttribute("href"));
                            assertThat(link.getAttribute("href")).isEqualTo(context.buildApplicationUrl(path));
                            return true;
                        }));
    }

    @Then("the page contains {string}")
    public void thePageContains(String text) {
        assertThat(context.getWebDriver().getPageSource()).contains(text);
    }

    @Given("the user logs in as {string} with password {string}")
    public void theUserIs(String username, String password) {
        context.getWebDriver().navigate().to(context.buildFrontendUrl("/login"));
        context.fluentWait().until(
                presenceOfElementLocated(By.xpath("//label[contains(text(), 'UserName:')]"))
                        .andThen(label -> {
                            // Find inputs by their labels since React doesn't have IDs
                            WebElement usernameInput = context.getWebDriver()
                                    .findElement(By
                                            .xpath("//label[contains(text(), 'UserName:')]/following-sibling::input"));
                            WebElement passwordInput = context.getWebDriver()
                                    .findElement(By
                                            .xpath("//label[contains(text(), 'Password:')]/following-sibling::input"));
                            WebElement loginForm = context.getWebDriver()
                                    .findElement(By.xpath("//form[.//button[contains(text(), 'Login')]]"));

                            usernameInput.sendKeys(username);
                            passwordInput.sendKeys(password);
                            loginForm.submit(); // Use form submit instead of button click
                            context.fluentWait().until(
                                    driver -> !driver.getCurrentUrl().endsWith("/login"));
                            return true;
                        }));
    }

    @When("the user enters {string} in {string}")
    public void theUserEnters(String value, String field) {
        context.fluentWait().until(
                presenceOf(elementWithLabel(field))
                        .andThen(input -> {
                            input.clear();
                            input.sendKeys(value);
                            return true;
                        }));
    }

    @And("the user selects {string} in {string}")
    public void theUserSelects(String value, String field) {
        context.fluentWait().until(
                presenceOf(elementWithLabel(field))
                        .andThen(input -> {
                            Select s = new Select(input);
                            s.selectByVisibleText(value);
                            return true;
                        }));
    }

    @Then("the user is on URL {string}")
    public void theUserIsOnURL(String url) {
        assertThat(context.getWebDriver().getCurrentUrl()).isEqualTo(context.buildApplicationUrl(url));
    }

    @Given("the user is on page {string}")
    public void theUserIsOnPage(String path) {
        context.getWebDriver().navigate().to(context.buildFrontendUrl(path));
    }

    @And("the user submits {string}")
    public void theUserSubmits(String formId) {
        // Wait for the modal and target the textarea inside it, using a flexible placeholder match
        context.fluentWait().until(
                presenceOfElementLocated(By.xpath(
                        "//div[contains(@class,'modal')]//textarea[contains(@placeholder,'Write your answer')]"))
                        .andThen(textarea -> {
                            textarea.clear();
                            textarea.sendKeys(formId);
                            return true;
                        }));

        // Click the submit button within the modal only
        context.fluentWait().until(
                presenceOfElementLocated(
                        By.xpath("//div[contains(@class,'modal')]//button[@type='submit'] | //div[contains(@class,'modal')]//input[@type='submit']"))
                        .andThen(submitButton -> {
                            submitButton.click();
                            return true;
                        }));
    }

    @And("the user clicks on the first {string} button")
    public void theUserClicksOnTheFirstButton(String buttonText) {
    // Make the locator resilient to whitespace differences (e.g., "Answer/ View Answers" vs "Answer / View Answers").
    String normalized = buttonText.trim().replaceAll("\\s+", " ");
    String xpath = "(//button[" +
        "contains(normalize-space(.), '" + normalized + "') or " +
        "(contains(., 'Answer') and contains(., 'View Answers'))" +
        "])[1]";

    context.fluentWait().until(
        presenceOfElementLocated(By.xpath(xpath))
            .andThen(input -> {
                input.click();
                return true;
            }));
    }

    public static ExpectedCondition<WebElement> presenceOf(Function<WebDriver, By> locator) {
        return driver -> presenceOfElementLocated(locator.apply(driver)).apply(driver);
    }

    public static Function<WebDriver, By> elementWithLabel(String labelText) {
        return presenceOfElementLocated(labelWithText(labelText))
                .andThen(label -> label.getAttribute("for"))
                .andThen(By::id);
    }

    public static By labelWithText(String text) {
        return elementWithText("label", text);
    }

    public static By elementWithText(String elementName, String text) {
        return By.xpath("//" + elementName + "[normalize-space(.)='" + text + "']");
    }

    @Then("the user should see the {string} page")
    public void theUserShouldSeeTheHomePage(String page) {
        // Wait for navigation to complete and verify we're on the specified page
        if (page.equals("home")) {
            context.fluentWait().until(driver ->
            driver.getCurrentUrl().endsWith("/") ||
            driver.getCurrentUrl().endsWith("/" + page)
        );
        } else {
            context.fluentWait().until(driver ->
            driver.getCurrentUrl().endsWith("/" + page)
        );
    }
}
}
