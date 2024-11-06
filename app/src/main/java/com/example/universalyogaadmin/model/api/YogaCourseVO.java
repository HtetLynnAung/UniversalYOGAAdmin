package com.example.universalyogaadmin.model.api;

import java.util.List;

public class YogaCourseVO {
    private int id;
    private String day;
    private String time;
    private int capacity;
    private int duration;
    private double price;
    private String type;
    private String level;
    private String description;
    private boolean isPublished;
    private List<YogaClassVO> yogaClasses;

    public YogaCourseVO(int id, String day, String time, int capacity, int duration, double price, String type, String level, String description, boolean isPublished, List<YogaClassVO> yogaClasses) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.type = type;
        this.level = level;
        this.description = description;
        this.isPublished = isPublished;
        this.yogaClasses = yogaClasses;
    }
}