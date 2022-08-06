package com.jumbo.cucumber;

import org.springframework.http.HttpStatus;

public interface ResponseAsserter<T extends ResponseAsserter<?>> {
    T hasHttpStatus(HttpStatus status);

    ElementAsserter<?> hasElement(String jsonPath);

    default ElementAsserter<?> hasResponse() {
        return hasElement(null);
    }

    default T hasOkStatus() {
        return hasHttpStatus(200);
    }

    default T hasHttpStatus(int status) {
        return hasHttpStatus(HttpStatus.valueOf(status));
    }
}
