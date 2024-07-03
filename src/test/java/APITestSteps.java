import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;


public class APITestSteps {
    private Response response;

    @Given("the API is available")
    public void theAPIIsAvailable() {
        RestAssured.baseURI = "https://open.er-api.com/v6/latest";
    }

    @When("I make a GET request to {string}")
    public void iMakeAGETRequestTo(String endpoint) {
        RequestSpecification request = given();
        response = request.get(endpoint);
    }

    @Then("the API call is successful")
    public void apiCallIsSuccessful() {
        assertNotNull(response);
    }

    @And("the status code is {int}")
    public void statusCodeIs(int statusCode) {
        //this method simply assert the status
        assertEquals(statusCode, response.getStatusCode());
    }

    @And("the USD price against AED is in range {double} - {double}")
    public void usdPriceAgainstAEDIsInRange(double minPrice, double maxPrice) {
        //this method validate USD price against AED with given Range.
        Float usdToAedRate = response.jsonPath().get("rates.AED");
        assertTrue(usdToAedRate >= minPrice && usdToAedRate <= maxPrice);
    }

    @And("the API response contains {int} currency pairs")
    public void apiResponseContainsCurrencyPairs(int expectedPairs) {
        //this metohd takes map size of rates assert that with expected value.
        int actualPairs = response.jsonPath().getMap("rates").size();
        assertEquals(expectedPairs, actualPairs);
    }

    @And("the API response matches the JSON schema")
    public void verifyAPIResponseMatchesJSONSchema() {
        //This method verify API response with JSON Schema.
        String responseBody = response.getBody().asString();

        try {
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
            JsonSchema schema = schemaFactory.getJsonSchema("https://open.er-api.com/v6/latest/USD");
            ProcessingReport report = schema.validate(new ObjectMapper().readTree(responseBody));

            if (report.isSuccess()) {
                System.out.println("API response matches the JSON schema.");
            } else {
                System.out.println("API response does not match the JSON schema:");
                System.out.println(report);
                fail("API response does not match the JSON schema.");
            }
        } catch (ProcessingException | IOException e) {
            e.printStackTrace();
            fail("Unable to validate API response against JSON schema.");
        }
    }
}
