package com.robandboo.fq.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class NetworkSingleton {
    private volatile static NetworkSingleton instance;
    private static final String BASE_URL = "https://fastquestion-v1.herokuapp.com";
    private Retrofit retrofit;

    private NetworkSingleton() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public static NetworkSingleton getInstance() {
        if (instance == null) {
            synchronized (NetworkSingleton.class) {
                if (instance == null) {
                    instance = new NetworkSingleton();
                    return instance;
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
