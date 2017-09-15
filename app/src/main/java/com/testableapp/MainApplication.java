package com.testableapp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.testableapp.interceptors.BaseHeadersInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {

    private static Context mContext;
    private static boolean testFramework = false;
    private static final String BASE_URL = "https://gapp-server.herokuapp.com/";
    private final static Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
            .client(getClient())
            .build();

    @Override
    public void onCreate() {
        mContext = getContext();
        super.onCreate();
    }

    public static Context getContext() {
        return mContext;
    }

    public static Retrofit getRetrofit() {
        return mRetrofit;
    }

    @NonNull
    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder().addInterceptor(new BaseHeadersInterceptor())
                .addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    }

    public static void initTestFramework() {
        testFramework = true;
    }

    public static boolean isTestFramework() {
        return testFramework;
    }
}
