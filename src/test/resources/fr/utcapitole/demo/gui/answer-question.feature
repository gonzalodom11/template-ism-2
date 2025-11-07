Feature: Answer Question

  Background:
    Given the user logs in as 'gonzalodm10' with password 'gonzalodm10'

  Scenario: User can submit an answer to a question
    When the user navigates to "/questions"
    And the user clicks on the first "Answer/ View Answers" button
    And the user submits "Hola"
    Then the page contains "Hola"



