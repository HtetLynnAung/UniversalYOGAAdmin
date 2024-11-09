package com.example.universalyogaadmin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.universalyogaadmin.model.YogaClassVO;
import com.example.universalyogaadmin.model.YogaCourseVO;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "UniYoga.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String COURSE_TABLE = "unit_yoga_course";
    private static final String CLASS_TABLE = "uni_yoga_class";

    // Table Columns
    private static final String COURSE_ID_COLUMN = "id";
    private static final String COURSE_DAY_COLUMN = "day";
    private static final String COURSE_TIME_COLUMN = "time";
    private static final String COURSE_CAPACITY_COLUMN = "capacity";
    private static final String COURSE_DURATION_COLUMN = "duration";
    private static final String COURSE_PRICE_COLUMN = "price";
    private static final String COURSE_TYPE_COLUMN = "type";
    private static final String COURSE_LEVEL_COLUMN = "level";
    private static final String COURSE_DESCRIPTION_COLUMN = "description";
    private static final String COURSE_IS_PUBLISHED_COLUMN = "is_published";
    private static final String CLASS_ID_COLUMN = "id";
    private static final String COURSE_ID_COLUMN_CLASS = "course_id";
    private static final String CLASS_DATE_COLUMN = "date_class";
    private static final String CLASS_TEACHER_COLUMN = "teacher";
    private static final String CLASS_COMMENT_COLUMN = "comment";

    // SQL Query to Create Table
    private static final String CREATE_TABLE_COURSE =
            "CREATE TABLE " + COURSE_TABLE + " (" +
                    COURSE_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_DAY_COLUMN + " TEXT NOT NULL, " +
                    COURSE_TIME_COLUMN + " TEXT NOT NULL, " +
                    COURSE_CAPACITY_COLUMN + " INTEGER NOT NULL, " +
                    COURSE_DURATION_COLUMN + " INTEGER NOT NULL, " +
                    COURSE_PRICE_COLUMN + " REAL NOT NULL, " +
                    COURSE_TYPE_COLUMN + " TEXT NOT NULL, " +
                    COURSE_LEVEL_COLUMN + " TEXT NOT NULL, " +
                    COURSE_IS_PUBLISHED_COLUMN + " INTEGER NOT NULL,"+
                    COURSE_DESCRIPTION_COLUMN + " TEXT)";

    private static final String CREATE_TABLE_CLASS =
            "CREATE TABLE " + CLASS_TABLE + "("
                    + CLASS_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COURSE_ID_COLUMN_CLASS + " INTEGER NOT NULL,"
                    + CLASS_DATE_COLUMN + " TEXT NOT NULL,"
                    + CLASS_TEACHER_COLUMN + " TEXT NOT NULL,"
                    + COURSE_DAY_COLUMN + " TEXT NOT NULL,"
                    + CLASS_COMMENT_COLUMN + " TEXT"
                    + ")";

    public DBHelper(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS '" + COURSE_TABLE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CLASS_TABLE + "'");
        onCreate(db);
    }

    // Method to add a new class
    public boolean addCourseToDB(String day, String time, int capacity, int duration, double price, String type, String level, String description, boolean isPublished) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_DAY_COLUMN, day);
        values.put(COURSE_TIME_COLUMN, time);
        values.put(COURSE_CAPACITY_COLUMN, capacity);
        values.put(COURSE_DURATION_COLUMN, duration);
        values.put(COURSE_PRICE_COLUMN, price);
        values.put(COURSE_TYPE_COLUMN, type);
        values.put(COURSE_LEVEL_COLUMN, level);
        values.put(COURSE_DESCRIPTION_COLUMN, description);
        values.put(COURSE_IS_PUBLISHED_COLUMN, isPublished ? 1 : 0 );

        long result = db.insert(COURSE_TABLE, null, values);
        db.close();
        return result != -1;
    }

    public boolean addClass(int courseID, String date, String teacher, String comment, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_ID_COLUMN_CLASS, courseID);
        values.put(CLASS_DATE_COLUMN, date);
        values.put(CLASS_TEACHER_COLUMN, teacher);
        values.put(CLASS_COMMENT_COLUMN, comment);
        values.put(COURSE_DAY_COLUMN, day);

        long result = db.insert(CLASS_TABLE, null, values);
        db.close();
        return result != -1;
    }

    // Method to get all classes
    public ArrayList<YogaCourseVO> getAllYogaCourses() {
        ArrayList<YogaCourseVO> yogaCoursVOS = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + COURSE_TABLE + " ORDER BY " + COURSE_ID_COLUMN + " DESC" , null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_ID_COLUMN));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_DAY_COLUMN));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_TIME_COLUMN));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_CAPACITY_COLUMN));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_DURATION_COLUMN));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COURSE_PRICE_COLUMN));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_TYPE_COLUMN));
                String level = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_LEVEL_COLUMN));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_DESCRIPTION_COLUMN));
                int isPublishedValue = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_IS_PUBLISHED_COLUMN));

                boolean isPublished = isPublishedValue == 1 ? true : false;
                YogaCourseVO yogaCourseVO = new YogaCourseVO(id, day, time, capacity, duration, price, type, level, description, isPublished );
                yogaCoursVOS.add(yogaCourseVO);
            }
            cursor.close();
        }
        db.close();
        return yogaCoursVOS;
    }

    public ArrayList<YogaClassVO> getAllYogaClasses(int courseID) {
        ArrayList<YogaClassVO> yogaClassVOS = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CLASS_TABLE + " WHERE " + COURSE_ID_COLUMN_CLASS + " = ? ORDER BY " + CLASS_ID_COLUMN + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseID)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_ID_COLUMN));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_DATE_COLUMN));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_TEACHER_COLUMN));
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COMMENT_COLUMN));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_DAY_COLUMN));

                YogaClassVO yogaClassVO = new YogaClassVO(id, courseID, date, teacher, comment, day);
                yogaClassVOS.add(yogaClassVO);
            }
            cursor.close();
        }
        db.close();
        return yogaClassVOS;
    }

    public boolean updateCourse(int id, YogaCourseVO yogaCourseVO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_DAY_COLUMN, yogaCourseVO.getDay());
        contentValues.put(COURSE_TIME_COLUMN, yogaCourseVO.getTime() );
        contentValues.put(COURSE_CAPACITY_COLUMN, yogaCourseVO.getCapacity());
        contentValues.put(COURSE_DURATION_COLUMN, yogaCourseVO.getDuration());
        contentValues.put(COURSE_PRICE_COLUMN, yogaCourseVO.getPrice());
        contentValues.put(COURSE_TYPE_COLUMN, yogaCourseVO.getType());
        contentValues.put(COURSE_LEVEL_COLUMN, yogaCourseVO.getLevel());
        contentValues.put(COURSE_DESCRIPTION_COLUMN, yogaCourseVO.getDescription());
        contentValues.put(COURSE_IS_PUBLISHED_COLUMN, 0 );

        int rowsAffected = db.update(COURSE_TABLE, contentValues, COURSE_ID_COLUMN + " = ?", new String[]{String.valueOf(id)});

        // Update was successful if rowsAffected is greater than 0
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateClass(int id, YogaClassVO yogaClassVO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASS_DATE_COLUMN, yogaClassVO.getDate());
        contentValues.put(CLASS_TEACHER_COLUMN, yogaClassVO.getTeacher() );
        contentValues.put(CLASS_COMMENT_COLUMN, yogaClassVO.getComment());

        int rowsAffected = db.update(CLASS_TABLE, contentValues, CLASS_ID_COLUMN + " = ?", new String[]{String.valueOf(id)});
        // Update was successful if rowsAffected is greater than 0
        db.close();
        return rowsAffected > 0;
    }

    public  boolean updateCourseIsPublished(int id, boolean isPublished ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_IS_PUBLISHED_COLUMN, isPublished ? 1 : 0 );

        int rowsAffected = db.update(COURSE_TABLE, contentValues, COURSE_ID_COLUMN + " = ?", new String[]{String.valueOf(id)});
        // Update was successful if rowsAffected is greater than 0
        db.close();
        return rowsAffected > 0;
    }

    public YogaCourseVO getYogaCourse(int courseID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_ID_COLUMN + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseID)});

        YogaCourseVO yogaCourseVO = new YogaCourseVO(0, "", "",0,0,0, "", "", "", true);

        if (cursor != null) {
            while (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_ID_COLUMN));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_DAY_COLUMN));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_TIME_COLUMN));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_CAPACITY_COLUMN));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_DURATION_COLUMN));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COURSE_PRICE_COLUMN));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_TYPE_COLUMN));
                String level = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_LEVEL_COLUMN));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_DESCRIPTION_COLUMN));
                int isPublishedValue = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_IS_PUBLISHED_COLUMN));

                boolean isPublished = isPublishedValue == 1 ? true : false;

                yogaCourseVO = new YogaCourseVO(id, day, time, capacity, duration, price, type, level, description, isPublished);

                break;
            }
            cursor.close();
        }
        db.close();
        return yogaCourseVO;
    }

    public YogaClassVO getYogaClasses(int classID) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CLASS_TABLE + " WHERE " + CLASS_ID_COLUMN + " = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classID)});

        YogaClassVO yogaClassVO = new YogaClassVO(-1, -1, "", "", "", "");
        if (cursor != null) {
            while (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_ID_COLUMN));
                int courseID = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_ID_COLUMN_CLASS));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_DATE_COLUMN));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_TEACHER_COLUMN));
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COMMENT_COLUMN));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_DAY_COLUMN));
                yogaClassVO = new YogaClassVO(id, courseID, date, teacher, comment, day);
                break;

            }
            cursor.close();
        }
        db.close();
        return yogaClassVO;
    }

    // Method to delete a class by ID
    public boolean deleteCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(COURSE_TABLE, COURSE_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean deleteClass(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(CLASS_TABLE, CLASS_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public ArrayList<YogaClassVO> searchClass(String teacherName, String date, String dayOfWeek) {
        ArrayList<YogaClassVO> yogaClassVOS = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + CLASS_TABLE + " WHERE " +
                CLASS_TEACHER_COLUMN + " LIKE ? COLLATE NOCASE AND " +
                CLASS_DATE_COLUMN + " LIKE ? COLLATE NOCASE AND " +
                COURSE_DAY_COLUMN + " LIKE ? COLLATE NOCASE";

        String[] args = new String[]{"%" + teacherName + "%", "%" + date + "%", "%" + dayOfWeek + "%"};
        Cursor cursor = db.rawQuery(query, args);

        while (cursor.moveToNext()) {
            // Retrieve data from the cursor
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_ID_COLUMN));
            int courseID = cursor.getInt(cursor.getColumnIndexOrThrow(COURSE_ID_COLUMN_CLASS));
            String dateAt = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_DATE_COLUMN));
            String teacher = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_TEACHER_COLUMN));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow(CLASS_COMMENT_COLUMN));
            String day = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_DAY_COLUMN));

            YogaClassVO yogaClassVO = new YogaClassVO(id, courseID, dateAt, teacher, comment, day);
            yogaClassVOS.add(yogaClassVO);
        }

        // Close the cursor and the database when you're done
        cursor.close();
        db.close();

        return yogaClassVOS;

    }

    // Method to reset the database by deleting all rows
    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COURSE_TABLE, null, null);
        db.delete(CLASS_TABLE, null, null);
        db.close();
    }



}