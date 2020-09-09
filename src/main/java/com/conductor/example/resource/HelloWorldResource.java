package com.conductor.example.resource;

import javax.validation.constraints.NotBlank;

public class HelloWorldResource {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
