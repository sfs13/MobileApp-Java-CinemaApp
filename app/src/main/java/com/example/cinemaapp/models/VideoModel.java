package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class VideoModel {
    @Expose
    private List<VideoResult> results;

    public List<VideoResult> getResults() {
        return results;
    }
}
