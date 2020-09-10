package com.conductor.example.resource;

import javax.validation.constraints.NotBlank;

public class MovieResource {
    @NotBlank
    private String movieType;
    @NotBlank
    private String movieId;

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}