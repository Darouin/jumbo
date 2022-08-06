package com.jumbo.cucumber;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

public class MockedSecurityContextRepository implements SecurityContextRepository {

    private Authentication authentication;

    public void authentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return authentication != null;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return new SecurityContextImpl(authentication);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {}
}
