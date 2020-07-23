package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PictureModel {
    @Expose
    private List<Backdrop> backdrops;

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }
}
