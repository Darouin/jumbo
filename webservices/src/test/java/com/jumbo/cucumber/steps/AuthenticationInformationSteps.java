package com.jumbo.cucumber.steps;

import static com.jumbo.cucumber.CucumberAssertions.*;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class AuthenticationInformationSteps {

    @Autowired
    private TestRestTemplate rest;

    @When("I get authentication information")
    public void getAuthenticatedUserAccount() {
        rest.getForEntity("/api/auth-info", Void.class);
    }

    @Then("I should have authentication information")
    public void shouldHaveAuthenticationInformation(Map<String, String> authenticationInformation) {
        assertThatLastResponse().hasOkStatus().hasResponse().containing(authenticationInformation);
    }
}
