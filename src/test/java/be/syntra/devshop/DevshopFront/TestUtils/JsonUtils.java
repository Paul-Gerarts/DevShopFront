package be.syntra.devshop.DevshopFront.TestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static String returnObjectAsJsonString(Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }
}
