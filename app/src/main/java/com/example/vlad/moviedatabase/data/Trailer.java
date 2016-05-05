package com.example.vlad.moviedatabase.data;

/**
 * This class saves the trailer info after the fetch.
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
