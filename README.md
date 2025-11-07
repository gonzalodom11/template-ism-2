# Q&A Web Application Template

Template to bootstrap the Q&A application development.

## Prerequisites

- Java 16+
- Maven
- Chrome/Chromium
- A [chromedriver](https://chromedriver.storage.googleapis.com/index.html) suitable for your Chrome/Chromium version

## Architecture

- Java/SpringBoot backend.
- Basic web interface with Thymeleaf.
- In-memory database.

## Acceptance tests

The provided acceptance tests are run in the `verify` Maven phase. The property `webdriver.chrome.driver` shall point to
your chrome driver executable.

```console
$ mvn verify -Dwebdriver.chrome.driver=/Applications/chromedriver
...
```
