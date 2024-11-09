package com.example.universalyogaadmin.model.api;

public class YogaClassRequestBody {
    private int id;
    private int courseID;
    private String date;
    private String teacher;
    private String comment;
    private String day;

    public YogaClassRequestBody(int id, int courseID, String date, String teacher, String comment, String day) {
        this.id = id;
        this.courseID = courseID;
        this.date = date;
        this.teacher = teacher;
        this.comment = comment;
        this.day = day;
    }
}
