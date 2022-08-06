package com.jumbo.cucumber;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

public class MockedCsrfTokenRepository implements CsrfTokenRepository {

    private static final CsrfToken TOKEN = buildCsrfToken();

    private static CsrfToken buildCsrfToken() {
        CsrfToken token = mock(CsrfToken.class);

        when(token.getHeaderName()).thenReturn("mocked-csrf-token");
        when(token.getParameterName()).thenReturn("mocked-csrf-token");
        when(token.getToken()).thenReturn("MockedToken");

        return token;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return TOKEN;
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {}

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return TOKEN;
    }
}
