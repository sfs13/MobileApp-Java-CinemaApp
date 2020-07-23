package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;

public class Genre {
    @Expose
    private Long id;
    @Expose
    private String name;

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
