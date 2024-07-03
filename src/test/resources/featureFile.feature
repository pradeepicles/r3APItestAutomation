Feature: Verify USD currency rates API

  Scenario: Validate API response
    Given the API is available
    When I make a GET request to "https://open.er-api.com/v6/latest/USD"
    Then the API call is successful
    And the status code is 200
    And the USD price against AED is in range 3.6 - 3.7
    And the API response contains 162 currency pairs
    And the API response matches the JSON schema