package com.example.cinemaapp.models;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("id")
    private Double id;

    public String getProfilePath() {
        return profilePath;
    }

    public Double getId() {
        return id;
    }
}
