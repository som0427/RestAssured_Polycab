@login
@AppLoginMobileVAPT
Feature: Implement OTP Functionality with Security Measures for mobile app login. Validate rate limiting, anti-automation checks,
  request replay prevention, OTP timeout, and controlled resend intervals


@AppLoginMobileVAPTsc1
  Scenario: Controlled OTP Resend Interval

    Given user submit "request_login_otp" with "POST" request for App-login
    Then validate response token validity time limit and generated token
    And validate "statusCode" is "200" in responseBody
    And validate "message" is "SUCCESS" in responseBody
    Then validate request otp with incorrect mobile number
    Then validate Resend Interval Control functionality is working properly with sending login request within time limit
    And validate "statusCode" is "400" in responseBody
    And validate "message" is "Resend interval not reached. Please wait before requesting another OTP." in responseBody
    Then validate Resend Interval Control functionality is working properly with sending login request after time limit
    Then validate user_id request rate limiting by sending login_otp as maximum two success request can be called within two Minutes
    Then validate IP request rate limiting by calling login_otp api more than five times within time limit
    Then validate "request_login_otp" is success after user_id request rate limit reached when call from different user


@AppLoginMobileVAPTsc2
  Scenario: Validate OTP