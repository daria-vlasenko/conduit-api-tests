package com.conduit.api.specs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;

public final class JsonMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    private JsonMapper() {
    }

    public static RestAssuredConfig config() {
        Jackson2ObjectMapperFactory factory = (type, charset) -> OBJECT_MAPPER;
        return RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2)
                        .jackson2ObjectMapperFactory(factory));
    }
}
