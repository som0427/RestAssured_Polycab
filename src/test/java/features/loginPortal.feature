#@login
#@loginPortal
Feature: Validate login API for Portal and assigned webMenus after login

  @loginPortalsc1
  Scenario: submit login api and verify whether login is successful

    Given add loginAPI payload
    When user submit "loginAPI" with "POST" request for loginPortal
    Then API call is success with status code 200
    Then validate ApiResponse execution time
    Then validate "token" is generated
    Then "projectId" is validated
    Then validate count of assigned projects and display project name



  @loginPortalsc2
  Scenario Outline: validate assigned web menus after login is successful

    Given add loginAPI payload
    When user submit "loginAPI" with "POST" request for loginPortal
    Then submit "assignedWebMenus" api with "token" and validate menu items for selected "<projectId>"
    Then validate count and value of submenus for "<selectedMenu>" and "<selectedSubMenu>" as per user-role rules

    Examples:
    |projectId|selectedMenu|selectedSubMenu      |
    |  355    |Analytics   |Daily Activity Report|