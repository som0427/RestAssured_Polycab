#@login
Feature: This is generic Login api feature which can be used for all other APIs to retrieve token


  Scenario: submit login api for portal
    When user submit "loginAPI" with "POST" request for login


  Scenario:
    When user submit "request_login_otp" with "POST" request for Applogin
