package com.jumbo.cucumber;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class ElementAsserter<T extends ResponseAsserter<?>> {

    private final T responseAsserter;
    private final String jsonPath;

    ElementAsserter(T responseAsserter, String jsonPath) {
        this.responseAsserter = responseAsserter;
        this.jsonPath = buildJsonPath(jsonPath);
    }

    private String buildJsonPath(String jsonPath) {
        if (jsonPath == null) {
            return "$";
        }

        return jsonPath;
    }

    public ElementAsserter<T> withValue(Object value) {
        assertPathValue(jsonPath, value);

        return this;
    }

    public <DATA> ElementAsserter<T> containingExactly(List<Map<String, DATA>> responses) {
        withElementsCount(responses.size());
        containing(responses);

        return this;
    }

    public <DATA> ElementAsserter<T> containing(Map<String, DATA> response) {
        assertThat(response).as("Can't check object agains a null response").isNotNull();

        response.entrySet().forEach(entry -> assertPathValue(jsonPath + "." + JsonHelper.toCamelCase(entry.getKey()), entry.getValue()));

        return this;
    }

    public ElementAsserter<T> withElementsCount(int count) {
        int elementsCount = CucumberTestContext.countEntries(jsonPath);

        assertThat(elementsCount)
            .as("Expecting " + count + " element(s) in " + jsonPath + " but got " + elementsCount + CucumberAssertions.callContext())
            .isEqualTo(count);

        return this;
    }

    public <DATA> ElementAsserter<T> containing(List<Map<String, DATA>> responses) {
        assertThat(responses).as("Can't check object agains a null responses").isNotNull();

        for (int line = 0; line < responses.size(); line++) {
            for (Map.Entry<String, DATA> entry : responses.get(line).entrySet()) {
                String path = jsonPath + "[" + line + "]." + JsonHelper.toCamelCase(entry.getKey());

                assertPathValue(path, entry.getValue());
            }
        }

        return this;
    }

    protected void assertPathValue(String jsonPath, Object value) {
        Object responseValue = CucumberTestContext.getElement(jsonPath);

        if (responseValue != null && value instanceof String && !(responseValue instanceof String)) {
            responseValue = responseValue.toString();
        }

        assertThat(responseValue)
            .as("Expecting " + jsonPath + " to contain " + value + " but got " + responseValue + CucumberAssertions.callContext())
            .isEqualTo(value);
    }

    public T and() {
        return responseAsserter;
    }
}
