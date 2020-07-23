package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cast {
    @Expose
    private String character;

    @SerializedName("credit_id")
    private String creditId;

    @Expose
    private String name;

    public String getCharacter() {
        return character;
    }

    public String getCreditId() {
        return creditId;
    }

    public String getName() {
        return name;
    }
}
