package com.realestateprosofia.realestateprosofia.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

public class JsonConfig {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
    }

    public static String serializeAgents(final List<Long> agentsList) {
        try {
            return mapper.writeValueAsString(agentsList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}