package com.jumbo.cucumber;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonHelperUnitTest {

    @Test
    void shouldPrintRawForUnreadableJson() {
        assertThat(JsonHelper.pretty("dummy")).isEqualTo("dummy");
    }

    @Test
    void shouldPrettyPrintJson() {
        assertThat(JsonHelper.pretty("{\"element\":\"value\",\"empty\":null}"))
            .isEqualTo("{" + System.lineSeparator() + "  \"element\" : \"value\"" + System.lineSeparator() + "}");
    }

    @Test
    void shouldNotCamelCaseNullValue() {
        assertThat(JsonHelper.toCamelCase(null)).isNull();
    }

    @Test
    void shouldCamelCaseValue() {
        assertThat(JsonHelper.toCamelCase("This is. a value")).isEqualTo("thisIs.aValue");
    }
}
