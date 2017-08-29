package com.testableapp.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static JsonUtils mInstance = null;
    private final Gson mGson;

    protected JsonUtils() {
        mGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
    }

    public static JsonUtils getInstance() {
        if (mInstance == null) {
            mInstance = new JsonUtils();
        }
        return mInstance;
    }

    public <T> T fromJson(final String json, final Class<T> classOfT) {

        return mGson.fromJson(json, classOfT);
    }

    public <T> T fromJson(final String json, final Type typeOfT) {

        return mGson.fromJson(json, typeOfT);
    }

    @Nullable
    public <Result> List<Result> fromJsonArray(final String string, final Class<Result> clazz) {
        final List<Map> tmpList = mGson.fromJson(string,  TypeToken.get(new ArrayList<Result>().getClass()).getType());
        if (tmpList != null) {
            final List<Result> resultList = new ArrayList<>(tmpList.size());
            for (final Map map : tmpList) {
                final String tmpJson = mGson.toJson(map);
                resultList.add(mGson.fromJson(tmpJson, clazz));
            }
            return resultList;
        }
        return null;
    }

    public String toJson(final Object src) {

        return mGson.toJson(src);
    }

    public Gson getGson() {

        return mGson;
    }

    public String getFileContentsFromResource(final Context ctx, final String resource) {
        final StringBuilder contents = new StringBuilder();
        try {
            // we might pull resources using R.raw.<res_id>, but for some reason compiler fails to
            // generate references sometimes.
            final int resId = ctx.getResources().getIdentifier(resource, "raw", ctx.getPackageName());
            final InputStream is = ctx.getResources().openRawResource(resId);

            final BufferedReader input = new BufferedReader(new InputStreamReader(is), 1024 * 8);
            try {
                String line;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                }
            } finally {
                input.close();
            }
        } catch (final Exception ex) {
            return null;
        }

        return contents.toString();
    }

    public JsonArray toJsonArray(final Object list, final TypeToken type) {
        return mGson.toJsonTree(list, type.getType()).getAsJsonArray();
    }

    public static boolean isNull(final JsonElement jsonElement) {
        return jsonElement == null || jsonElement.isJsonNull();
    }

    public static boolean isNull(final JsonObject jsonObject) {
        return jsonObject == null || jsonObject.isJsonNull();
    }
}
