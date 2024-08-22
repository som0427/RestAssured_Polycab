#@login
#@loginMobileAppVAPT
Feature: Implement OTP Functionality with Security Measures for mobile app login. Validate rate limiting, anti-automation checks,
  request replay prevention, OTP timeout, and controlled resend intervals


@loginMobileAppVAPTsc1
  Scenario: Controlled OTP Resend Interval

    Given user submit "request_login_otp" with "POST" request for Applogin
    And validate "statusCode" is "200" in responseBody
    And validate "message" is "SUCCESS" in responseBody