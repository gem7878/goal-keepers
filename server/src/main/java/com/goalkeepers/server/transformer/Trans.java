package com.goalkeepers.server.transformer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Trans {

    public static String token(String rtn) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(rtn);
        return jsonNode.get("access_token").asText();
    }
}
