package com.testableapp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.testableapp.interceptors.AuthInterceptor;
import com.testableapp.interceptors.BaseHeadersInterceptor;
import com.testableapp.interceptors.CacheInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {

    private static long CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    private static boolean testFramework = false;
    private static final String BASE_URL = "https://gapp-server.herokuapp.com/";
    //private static final String BASE_URL = "http://192.168.0.12:9052/";
    private static Retrofit mRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
                .client(getClient(this))
                .build();
    }

    public static Retrofit getRetrofit() {
        return mRetrofit;
    }

    @NonNull
    public static OkHttpClient getClient(@NonNull final Context context) {
        return new OkHttpClient.Builder().addInterceptor(new BaseHeadersInterceptor(context))
                .addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .cache(new Cache(context.getCacheDir(), CACHE_SIZE))
                .addInterceptor(new AuthInterceptor(context))
                .addInterceptor(new CacheInterceptor(context))
                .build();
    }

    public static void initTestFramework() {
        testFramework = true;
    }

    public static boolean isTestFramework() {
        return testFramework;
    }
}
