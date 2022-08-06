Feature: Authentication information

  Scenario: Get authentication information
    Given I am logged in as "admin"
    When I get authentication information
    Then I should have authentication information
      | Issuer | http://DO_NOT_CALL:9080/auth/realms/jhipster |
