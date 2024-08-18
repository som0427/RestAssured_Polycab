#@login
Feature: This is generic Login api feature which can be used for all other APIs to retrieve token

  Scenario: submit login api
    When user submit "loginAPI" with "POST" request for login
