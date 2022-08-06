package com.jumbo.cucumber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

final class JsonHelper {

    private static final ObjectMapper jsonMapper = jsonMapper();

    private JsonHelper() {}

    public static ObjectMapper jsonMapper() {
        return new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    static String pretty(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }

        try {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMapper.readValue(json, Object.class));
        } catch (IOException e) {
            return json;
        }
    }

    static String toCamelCase(String value) {
        if (value == null) {
            return null;
        }

        return Arrays.stream(value.split("\\.")).map(JsonHelper::camelCaseField).collect(Collectors.joining("."));
    }

    private static String camelCaseField(String value) {
        return StringUtils.uncapitalize(Arrays.stream(value.split(" ")).map(StringUtils::capitalize).collect(Collectors.joining()));
    }
}
