#@settingsPortal
Feature: Functionality of portal Settings and sub Settings modules for Polycab


  @settingsPortalsc1
  Scenario Outline: validate influencer settings module

    Given user submit "loginAPI" with "POST" request for login
    Then add request for getAllAttributeTypes
    Then user submit "getAllAttributeTypes" with "GET" request for settingsPortal
    Then validate "<count>" of getAllAttributeTypes
    And validate "<FieldTypes>" are present
    Then add request for getFieldVisibilitySettings
    Then user submit "getFieldVisibilitySettings" with "GET" request for settingsPortal
    Then add request for getFieldsInSetting
    Then user submit "getFieldsInSetting" with "GET" request for settingsPortal
    Then validate statusCode and message for getFieldsInSetting

    Examples:
      | count |FieldTypes|
      |  20   |Calculated Field, Data List, Audio/Video, OTP Validation, QR Code Scanner, Time Duration|