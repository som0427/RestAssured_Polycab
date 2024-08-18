#@settingsPortal
Feature: Functionality of portal Settings and sub Settings modules for Polycab


  @settingsPortalsc1
  Scenario: validate influencer settings module

    Given user submit "loginAPI" with "POST" request for login
    Then add request for getAllAttributeTypes
    Then user submit "getAllAttributeTypes" with "GET" request for settingsPortal
    Then add request for getFieldVisibilitySettings
    Then user submit "getFieldVisibilitySettings" with "GET" request for settingsPortal
    Then add request for getFieldsInSetting
    Then user submit "getFieldsInSetting" with "GET" request for settingsPortal