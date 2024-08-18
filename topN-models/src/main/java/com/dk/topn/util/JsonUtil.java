package com.dk.topn.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static byte[] covertFormToByteArray(Object object){
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (Exception e){
            throw new RuntimeException("Unable to convert object to byte array", e);
        }
    }

    public static  <T> T covertFormToByteArray(byte[] object, Class<T> classType){
        try {
            return objectMapper.readValue(object, classType);
        } catch (Exception e){
            throw new RuntimeException("Unable to convert object to byte array", e);
        }
    }
}
