package net.loyintea.mock.util;

import com.google.gson.Gson;

public class JsonUtils {

    private static final Gson GSON = new Gson();

    public static String toJson(Object object){
        return GSON.toJson(object);
    }
}
