package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class CastModel {
    @Expose
    private List<Cast> cast;

    public CastModel(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Cast> getCast() {
        return cast;
    }
}
