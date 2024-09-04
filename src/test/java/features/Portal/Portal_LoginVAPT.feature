@login
@PortalLoginVAPT
#B2B-2277
Feature: Validate login functionality VAPT testing for portal. Check Secure Session Management and Authentication Mechanisms


@PortalLoginVAPTsc1
  Scenario: Manual Logout and Session Expiration

    Given user submit "loginAPI" with "POST" request for login
    Then submit "assistant" API with same token session
    And verify user able to submit API with same token session
    When add logoutApi payload
    Then user submit "logoutApi" with "POST" request for PortalLoginVAPT
    And verify current statusCode for logOutApi
    Then validate after a user manually logs out the session token must be immediately expired
    Then validate users should not be able to use the expired token to access the system


#@PortalLoginVAPTsc2
  Scenario Outline: Inactivity-based Session Expiration

    Given user submit "loginAPI" with "POST" request for login
    Then validate session should expire after "<scheduled-time>" of inactivity
#    Then validate session should expire after 15 minutes of inactivity
    Then validate users should be automatically logged out after the session expires due to inactivity

  Examples:
    |scheduled-time|
    |1020          |


  @PortalLoginVAPTsc3
  Scenario: Rate Limiting and Account Lockout

    Given user submit "loginAPI" with "POST" request for login


@PortalLoginVAPTsc4
  Scenario: Consistent Error Messaging

    Given user submit "loginAPI" with "POST" request for login
