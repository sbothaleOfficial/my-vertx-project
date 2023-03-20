package com.sbothale.webserver.bucket;

import com.sbothale.webserver.object.Object;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private final String id;
    private final String name;
    private final String location;
    private final List<Object> objects;

    public Bucket(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.objects = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void addObject(Object object) {
        objects.add(object);
    }
}