package com.example.universalyogaadmin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.universalyogaadmin.model.YogaClass;
import com.example.universalyogaadmin.model.YogaCourse;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "UniYoga.db";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    private static final String TABLE_COURSE = "yoga_course";
    private static final String TABLE_CLASS = "yoga_class";

    // Table Columns
    private static final String COURSE_COLUMN_ID = "id";
    private static final String COURSE_COLUMN_DAY = "day";
    private static final String COURSE_COLUMN_TIME = "time";
    private static final String COURSE_COLUMN_CAPACITY = "capacity";
    private static final String COURSE_COLUMN_DURATION = "duration";
    private static final String COURSE_COLUMN_PRICE = "price";
    private static final String COURSE_COLUMN_TYPE = "type";
    private static final String COURSE_COLUMN_LEVEL = "level";
    private static final String COURSE_COLUMN_DESCRIPTION = "description";
    private static final String COURSE_COLUMN_IS_PUBLISHED = "is_published";
    private static final String CLASS_COLUMN_ID = "id";
    private static final String CLASS_COLUMN_COURSE_ID = "course_id";
    private static final String CLASS_COLUMN_DATE = "date_class";
    private static final String CLASS_COLUMN_TEACHER = "teacher";
    private static final String CLASS_COLUMN_COMMENT = "comment";

    // SQL Query to Create Table
    private static final String CREATE_TABLE_COURSE =
            "CREATE TABLE " + TABLE_COURSE + " (" +
                    COURSE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_COLUMN_DAY + " TEXT NOT NULL, " +
                    COURSE_COLUMN_TIME + " TEXT NOT NULL, " +
                    COURSE_COLUMN_CAPACITY + " INTEGER NOT NULL, " +
                    COURSE_COLUMN_DURATION + " INTEGER NOT NULL, " +
                    COURSE_COLUMN_PRICE + " REAL NOT NULL, " +
                    COURSE_COLUMN_TYPE + " TEXT NOT NULL, " +
                    COURSE_COLUMN_LEVEL + " TEXT NOT NULL, " +
                    COURSE_COLUMN_IS_PUBLISHED + " INTEGER NOT NULL,"+
                    COURSE_COLUMN_DESCRIPTION + " TEXT)";

    private static final String CREATE_TABLE_CLASS =
            "CREATE TABLE " + TABLE_CLASS + "("
                    + CLASS_COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CLASS_COLUMN_COURSE_ID + " INTEGER NOT NULL,"
                    + CLASS_COLUMN_DATE + " TEXT NOT NULL,"
                    + CLASS_COLUMN_TEACHER + " TEXT NOT NULL,"
                    + COURSE_COLUMN_DAY + " TEXT NOT NULL,"
                    + CLASS_COLUMN_COMMENT + " TEXT"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the classes table
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_CLASS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table if it exists
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_COURSE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CLASS + "'");
        onCreate(db);
    }

    // Method to add a new class
    public boolean addCourse(String day, String time, int capacity, int duration, double price, String type, String level, String description, boolean isPublished) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_COLUMN_DAY, day);
        values.put(COURSE_COLUMN_TIME, time);
        values.put(COURSE_COLUMN_CAPACITY, capacity);
        values.put(COURSE_COLUMN_DURATION, duration);
        values.put(COURSE_COLUMN_PRICE, price);
        values.put(COURSE_COLUMN_TYPE, type);
        values.put(COURSE_COLUMN_LEVEL, level);
        values.put(COURSE_COLUMN_DESCRIPTION, description);
        values.put(COURSE_COLUMN_IS_PUBLISHED, isPublished ? 1 : 0 );

        long result = db.insert(TABLE_COURSE, null, values);
        db.close();
        return result != -1;
    }

    public boolean addClass(int courseID, String date, String teacher, String comment, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_COLUMN_COURSE_ID, courseID);
        values.put(CLASS_COLUMN_DATE, date);
        values.put(CLASS_COLUMN_TEACHER, teacher);
        values.put(CLASS_COLUMN_COMMENT, comment);
        values.put(COURSE_COLUMN_DAY, day);

        long result = db.insert(TABLE_CLASS, null, values);
        db.close();
        return result != -1;
    }

    // Method to get all classes
    public ArrayList<YogaCourse> getAllYogaCourses() {
        ArrayList<YogaCourse> yogaCourses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSE + " ORDER BY " + COURSE_COLUMN_ID + " DESC" , null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_COLUMN_ID));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_DAY));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_TIME));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_COLUMN_CAPACITY));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_COLUMN_DURATION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COURSE_COLUMN_PRICE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_TYPE));
                String level = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_LEVEL));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_DESCRIPTION));
                int isPublishedValue = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_COLUMN_IS_PUBLISHED));

                boolean isPublished = isPublishedValue == 1 ? true : false;
                YogaCourse yogaCourse = new YogaCourse(id, day, time, capacity, duration, price, type, level, description, isPublished );
                yogaCourses.add(yogaCourse);
            }
            cursor.close();
        }
        db.close();
        return yogaCourses;
    }

    public ArrayList<YogaClass> getAllYogaClasses(int courseID) {
        ArrayList<YogaClass> yogaClasses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CLASS + " WHERE " + CLASS_COLUMN_COURSE_ID + " = ? ORDER BY " + CLASS_COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseID)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_DATE));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_TEACHER));
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_COMMENT));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_DAY));

                YogaClass yogaClass = new YogaClass(id, courseID, date, teacher, comment, day);
                yogaClasses.add(yogaClass);
            }
            cursor.close();
        }
        db.close();
        return yogaClasses;
    }

    public boolean updateCourse(int id, YogaCourse yogaCourse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COURSE_COLUMN_DAY, yogaCourse.getDay());
        contentValues.put( COURSE_COLUMN_TIME, yogaCourse.getTime() );
        contentValues.put( COURSE_COLUMN_CAPACITY, yogaCourse.getCapacity());
        contentValues.put(COURSE_COLUMN_DURATION, yogaCourse.getDuration());
        contentValues.put(COURSE_COLUMN_PRICE, yogaCourse.getPrice());
        contentValues.put(COURSE_COLUMN_TYPE, yogaCourse.getType());
        contentValues.put(COURSE_COLUMN_LEVEL, yogaCourse.getLevel());
        contentValues.put(COURSE_COLUMN_DESCRIPTION, yogaCourse.getDescription());
        contentValues.put(COURSE_COLUMN_IS_PUBLISHED, 0 );

        int rowsAffected = db.update( TABLE_COURSE , contentValues, COURSE_COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        // Update was successful if rowsAffected is greater than 0
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateClass(int id, YogaClass yogaClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( CLASS_COLUMN_DATE, yogaClass.getDate());
        contentValues.put( CLASS_COLUMN_TEACHER, yogaClass.getTeacher() );
        contentValues.put( CLASS_COLUMN_COMMENT, yogaClass.getComment());

        int rowsAffected = db.update( TABLE_CLASS , contentValues, CLASS_COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        // Update was successful if rowsAffected is greater than 0
        db.close();
        return rowsAffected > 0;
    }

    public  boolean updateCourseIsPublished(int id, boolean isPublished ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COLUMN_IS_PUBLISHED, isPublished ? 1 : 0 );

        int rowsAffected = db.update( TABLE_COURSE , contentValues, COURSE_COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        // Update was successful if rowsAffected is greater than 0
        db.close();
        return rowsAffected > 0;
    }

    public YogaCourse getYogaCourse(int courseID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_COURSE + " WHERE " + COURSE_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseID)});

        YogaCourse yogaCourse = new YogaCourse(0, "", "",0,0,0, "", "", "", true);

        if (cursor != null) {
            while (cursor.moveToFirst()) {
                Log.i("TAG", "inside");
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String day = cursor.getString(cursor.getColumnIndexOrThrow("day"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow("capacity"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String level = cursor.getString(cursor.getColumnIndexOrThrow("level"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int isPublishedValue = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_COLUMN_IS_PUBLISHED));

                boolean isPublished = isPublishedValue == 1 ? true : false;

                yogaCourse = new YogaCourse(id, day, time, capacity, duration, price, type, level, description, isPublished);

                break;
            }
            cursor.close();
        }
        db.close();
        return yogaCourse;
    }

    public YogaClass getYogaClasses(int classID) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CLASS + " WHERE " + CLASS_COLUMN_ID + " = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classID)});

        YogaClass yogaClass = new YogaClass(-1, -1, "", "", "", "");
        if (cursor != null) {
            while (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_COLUMN_ID));
                int courseID = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_COLUMN_COURSE_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_DATE));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_TEACHER));
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_COMMENT));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_DAY));

                yogaClass = new YogaClass(id, courseID, date, teacher, comment, day);
                break;

            }
            cursor.close();
        }
        db.close();
        return yogaClass;
    }

    // Method to delete a class by ID
    public boolean deleteCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_COURSE, COURSE_COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean deleteClass(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CLASS, CLASS_COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public ArrayList<YogaClass> searchClass(String teacherName, String date, String dayOfWeek) {
        ArrayList<YogaClass> yogaClasses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CLASS + " WHERE " +
                CLASS_COLUMN_TEACHER + " LIKE ? COLLATE NOCASE AND " +
                CLASS_COLUMN_DATE + " LIKE ? COLLATE NOCASE AND " +
                COURSE_COLUMN_DAY + " LIKE ? COLLATE NOCASE";

        String[] args = new String[]{"%" + teacherName + "%", "%" + date + "%", "%" + dayOfWeek + "%"};
        Cursor cursor = db.rawQuery(query, args);

        while (cursor.moveToNext()) {
            // Retrieve data from the cursor
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_COLUMN_ID));
            int courseID = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_COLUMN_COURSE_ID));
            String dateAt = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_DATE));
            String teacher = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_TEACHER));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COLUMN_COMMENT));
            String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_COLUMN_DAY));

            YogaClass yogaClass = new YogaClass(id, courseID, dateAt, teacher, comment, day);
            yogaClasses.add(yogaClass);
        }

        // Close the cursor and the database when you're done
        cursor.close();
        db.close();

        return yogaClasses;

    }

    // Method to reset the database by deleting all rows
    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURSE, null, null);
        db.delete(TABLE_CLASS, null, null);
        db.close();
    }



}