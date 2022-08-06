package com.jumbo.cucumber;

import com.jumbo.security.AuthoritiesConstants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.undertow.security.idm.Account;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationSteps {

    @Autowired
    private MockedSecurityContextRepository contexts;

    private static final Map<String, AuthenticatedUser> USERS = buildAuthentications();

    private static Map<String, AuthenticatedUser> buildAuthentications() {
        Map<String, AuthenticatedUser> users = new HashMap<>();

        users.put("admin", user("admin", AuthoritiesConstants.ADMIN, null));

        return users;
    }

    private static AuthenticatedUser user(String username, String profile, Account account) {
        return new AuthenticatedUser(new TestingAuthenticationToken(username, "N/A", AuthorityUtils.createAuthorityList(profile)));
    }

    @Given("I am logged in as {string}")
    public void authenticateUser(String username) {
        AuthenticatedUser user = USERS.get(username);

        Authentication authentication = user.authentication;
        SecurityContextHolder.getContext().setAuthentication(authentication);

        contexts.authentication(authentication);
    }

    @Given("I logout")
    @Given("I am not logged in")
    public void logout() {
        contexts.authentication(null);
    }

    @Then("I should be forbidden")
    public void shouldGetAuthorizationError() {
        Assertions.assertThat(CucumberTestContext.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private static class AuthenticatedUser {

        private final Authentication authentication;

        private AuthenticatedUser(Authentication authentication) {
            this.authentication = authentication;
        }
    }
}
