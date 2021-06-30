package top.frankyang.pre.util;

import com.google.common.io.Files;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class JsonHelper {
    private static final Gson gson = new Gson();  // Manages a simple Gson object.

    private JsonHelper() {
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> T deserialize(File file, Class<T> clazz) {
        try {
            return deserialize(Files.toString(file, StandardCharsets.UTF_8), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String serialize(Object object) {
        return gson.toJson(object);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void serialize(Object object, File file) {
        try {
            Files.write(serialize(object), file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
