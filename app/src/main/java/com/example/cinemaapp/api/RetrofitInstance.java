package com.example.cinemaapp.api;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static JsonApi getRetrofitInstance() {
        return new retrofit2.Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JsonApi.class);
    }
}
