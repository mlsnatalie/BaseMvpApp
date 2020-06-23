package com.libs.core.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Json解析工具类
 *
 * @author zhang.zheng
 * @version 2015-12-17
 */
public class GsonUtils {

    private static Gson mGson = new Gson();

    /**
     * bean->json
     */
    public static <T> String toJson(T bean) {
        // String json = GsonUtils.toJson(vo);
        return mGson.toJson(bean);
    }


    /**
     * json->bean
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        // Person vo = GsonUtils.fromJson(json, Person.class);
        return mGson.fromJson(json, clazz);
    }


    /**
     * json->bean
     */
    public static <T> T fromJson(JsonObject json, Class<T> clazz) {
        // Person vo = GsonUtils.fromJson(json, Person.class);
        return mGson.fromJson(json, clazz);
    }


    /**
     * json->List<bean>
     */
    public static <T> T fromJson(String json, Type type) throws JsonSyntaxException {
        // List<Person> voList = GsonUtils.fromJson(json, new TypeToken<List<Person>>(){}.getType());
        return mGson.fromJson(json, type);
    }

}
