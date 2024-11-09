package com.example.universalyogaadmin.model;

import com.example.universalyogaadmin.model.api.YogaClassRequestBody;
import com.example.universalyogaadmin.model.api.YogaCourseRequestBody;

import java.util.List;

public class YogaCourseVO {
    private int id; // Add ID if needed for database operations
    private String day;
    private String time;
    private int capacity;
    private int duration;
    private double price;
    private String type;
    private String level;
    private String description;
    private boolean isPublished;

    // Constructor
    public YogaCourseVO(int id, String day, String time, int capacity, int duration, double price, String type, String level, String description, boolean isPublished) {
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
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getDay() { return day; }
    public String getTime() { return time; }
    public int getCapacity() { return capacity; }
    public int getDuration() { return duration; }
    public double getPrice() { return price; }
    public String getType() { return type; }
    public String getLevel() { return  level; }
    public String getDescription() { return description; }
    public boolean getIsPublished() { return  isPublished; }

    public YogaCourseRequestBody changYogaCourseVO(List<YogaClassRequestBody> yogaClasses) {
        return new YogaCourseRequestBody(id, day, time, capacity, duration, price, type, level, description, isPublished, yogaClasses);
    }
}
