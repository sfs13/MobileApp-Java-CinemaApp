package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;

public class VideoResult {
    @Expose
    private String key;

    @Expose
    private String site;

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }
}
