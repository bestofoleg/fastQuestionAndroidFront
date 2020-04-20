package com.robandboo.fq.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkSingleton {
    private volatile static NetworkSingleton instance;
    private static final String BASE_URL = "http://10.0.2.2:8090";
    private Retrofit retrofit;

    private NetworkSingleton() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
