package net.loyintea.mock.common.util;

import com.google.gson.Gson;

public class JsonUtils {

    private static final Gson GSON = new Gson();

    public static String toJson(Object object){
        return GSON.toJson(object);
    }

    public static Object fromJson(String json) {
        return GSON.fromJson(json, Object.class);
    }
}
