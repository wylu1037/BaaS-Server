package com.zkjg.baas.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author wylu
 * @date 2022/4/16 上午 09:32
 */
public class GsonUtils {

    private GsonUtils() {
    }

    private static Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
    }

    public static String toJson(Object object) {
        String json = null;
        if (gson != null) {
            json = gson.toJson(object);
        }
        return json;
    }
}
