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

    private static final Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
    }

    /**
     * 对象转json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        String json = null;
        if (gson != null) {
            json = gson.toJson(object);
        }
        return json;
    }

    /**
     * json字符串转对象
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(jsonStr, clazz);
        }
        return t;
    }
}
