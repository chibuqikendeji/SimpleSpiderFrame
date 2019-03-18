package com.jsu.lqy.utils;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 将传入的对象序列化为json串
 * @author lanqiyu
 * @date: 2019年3月7日 下午6:39:09 
 * @Description: 该类的功能描述
 */
public class JsonUtil {
	public static String toJson(Object o) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(o);
    }
	public static String toJsonBeautiful(Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(o);
    }
	public static <T> List<T> toList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }
}
