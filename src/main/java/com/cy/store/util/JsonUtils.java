package com.cy.store.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JsonUtils {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static Object JsonToObject(String json, Class cls) throws IOException {
        return objectMapper.readValue(json, cls);
    }
    public static String ObjectToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
