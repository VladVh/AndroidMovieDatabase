package com.example.vlad.moviedatabase.data;

/**
 * Created by Vlad on 21.04.2016.
 */
public class Trailer {
    private String name;
    private String key;
    private String type;

    public Trailer() {

    }

    public Trailer(String id) {
        this.key = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
