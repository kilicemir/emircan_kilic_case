package com.insider.petstore.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createMapper();

    private JsonUtils() {
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        return mapper;
    }

    public static <T> T fromResponse(Response response, Class<T> clazz) {
        if (response == null) {
            return null;
        }
        return toObject(response.body().asString(), clazz);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }

    public static <T> List<T> fromResponseList(Response response, Class<T> clazz) {
        if (response == null) {
            return Collections.emptyList();
        }
        return toObjectList(response.body().asString(), clazz);
    }

    public static <T> List<T> toObjectList(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            CollectionType collectionType = OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(ArrayList.class, clazz);
            return OBJECT_MAPPER.readValue(json, collectionType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON array to List<" + clazz.getSimpleName() + ">", e);
        }
    }
}
