package com.chunmiao.jsontotxt.entity;

import lombok.Data;

@Data
public class Root {
    public Root(String name, String method, String api,String description) {
        this.name = name;
        this.method = method;
        this.api = api;
        this.description = description;
    }

    private String description;

    private String name;

    private String method;

    private String api;
}

