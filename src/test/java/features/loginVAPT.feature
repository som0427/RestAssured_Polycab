#@login
#@loginVAPT B2B-2277
Feature: Validate login functionality VAPT testing. Check Secure Session Management and Authentication Mechanisms


@loginVAPTsc1
  Scenario: Manual Logout and Session Expiration

    Given user submit "loginAPI" with "POST" request for login
    Then verify if user able to submit "assistant" API with same token session
    When add logoutApi payload
    Then user submit "logoutApi" with "POST" request with "token" for logout
    Then validate after a user manually logs out the session token must be immediately expired
    Then validate users should not be able to use the expired token to access the system


@loginVAPTsc2
  Scenario: Inactivity-based Session Expiration

    Given user submit "loginAPI" with "POST" request for login
    Then validate session should expire after 15 minutes of inactivity
    Then validate users should be automatically logged out after the session expires due to inactivity


@loginVAPTsc3
  Scenario: Rate Limiting and Account Lockout

    Given user submit "loginAPI" with "POST" request for login


@loginVAPTsc4
  Scenario: Consistent Error Messaging

    Given user submit "loginAPI" with "POST" request for login
