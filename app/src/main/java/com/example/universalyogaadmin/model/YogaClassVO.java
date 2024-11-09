package com.example.universalyogaadmin.model;

import com.example.universalyogaadmin.model.api.YogaClassRequestBody;

public class YogaClassVO {
    private int id; // Add ID if needed for database operations
    private int courseID;
    private String date;
    private String teacher;
    private String comment;
    private String day;

    public YogaClassVO(int id, int courseID, String date, String teacher, String comment, String day) {
        this.id = id;
        this.courseID = courseID;
        this.date = date;
        this.teacher = teacher;
        this.comment = comment;
        this.day = day;
    }

    // Getters and Setters
    public int getId() { return id; }
    public int getCourseID() { return courseID; }
    public String getDate() { return date; }
    public String getTeacher() { return teacher; }
    public String getComment() { return comment; }
    public String getDay() { return  day; }

    public YogaClassRequestBody changeYogaClassVO() {
        return new YogaClassRequestBody(id, courseID, date, teacher, comment, day);
    }
}
