package com.example.universalyogaadmin.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.database.DBHelper;
import com.example.universalyogaadmin.model.YogaCourseVO;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class CourseUpdateActivity extends AppCompatActivity {

    private Spinner spinnerDayOfWeek, spinnerClassType, spinnerDifficultyLevel;

    private TextInputEditText  editTextDescription;

    private DBHelper DBHelper;

    private Button btnUpdate;

    private int courseID = -1;

    private EditText editTextTime, editTextCapacity, editTextPrice, editTextDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_update);

        getSupportActionBar().setTitle("Edit Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupFindViewByIds();

        DBHelper = new DBHelper(this);

        courseID = getIntent().getIntExtra("yoga_course_id", -1);
        loadClassDetails(courseID);
        setUpTimePicker();
        btnUpdate.setOnClickListener(view -> validateAndSubmit());
    }

    private void setupFindViewByIds() {
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        spinnerClassType = findViewById(R.id.spinnerClassType);
        spinnerDifficultyLevel = findViewById(R.id.spinnerDifficultyLevel);
        editTextTime = findViewById(R.id.editTextTime);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        btnUpdate = findViewById(R.id.buttonUpdate);
    }

    private void loadClassDetails(int id) {
        YogaCourseVO yogaCourseVO = DBHelper.getYogaCourse(id);

        editTextTime.setText(yogaCourseVO.getTime());
        editTextCapacity.setText(yogaCourseVO.getCapacity()+"");
        editTextDuration.setText(yogaCourseVO.getDuration() +"");
        editTextPrice.setText(yogaCourseVO.getPrice()+"");
        editTextDescription.setText(yogaCourseVO.getDescription());
        spinnerDayOfWeek.setSelection(getSelectedPositionForWeek(yogaCourseVO.getDay()));
        spinnerClassType.setSelection(getSelectedPositionForType(yogaCourseVO.getType()));
        spinnerDifficultyLevel.setSelection(getSelectedPositionForLevel(yogaCourseVO.getLevel()));
    }

    private void setUpTimePicker() {
        // Disable direct input for time EditText
        editTextTime.setInputType(0);
        editTextTime.setFocusable(false);

        // Show TimePickerDialog when editTextTime is clicked
        editTextTime.setOnClickListener(v -> showTimePickerDialog());
    }

    private void showTimePickerDialog() {
        // Initialize TimePickerDialog with current time as default
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    // Format selected time and set to EditText
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    editTextTime.setText(formattedTime);
                },
                hour,
                minute,
                true // Use 24-hour format, set to false if 12-hour format is needed
        );

        timePickerDialog.show();
    }

    private void validateAndSubmit() {
        // Validate required fields
        String day = spinnerDayOfWeek.getSelectedItem().toString();
        String time = editTextTime.getText().toString().trim();
        String capacity = editTextCapacity.getText().toString().trim();
        String duration = editTextDuration.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String classType = spinnerClassType.getSelectedItem().toString();
        String level = spinnerDifficultyLevel.getSelectedItem().toString();

        // Check for empty fields and show errors
        if (day.isEmpty() || time.isEmpty() || capacity.isEmpty() || duration.isEmpty() || price.isEmpty() || classType.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Display entered details for confirmation
        saveToDatabase(day, time, Integer.parseInt(capacity), Integer.parseInt(duration), Double.parseDouble(price), classType, level, editTextDescription.getText().toString());
    }

    private void saveToDatabase(String day, String time, int capacity, int duration, double price, String classType, String level, String description) {
        // Save class details to the SQLite database
        // Implementation of database insertion goes here
        // Add the course to the database
        YogaCourseVO yogaCourseVO = new YogaCourseVO(courseID, day, time, capacity, duration, price, classType, level, description, false);
        boolean isUpdateded = DBHelper.updateCourse(courseID, yogaCourseVO);
        if (isUpdateded) {
            Toast.makeText(this, "Course updated successfully!", Toast.LENGTH_SHORT).show();
            finish();  // Close activity and go back to the list
        } else {
            Toast.makeText(this, "Failed to update course.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        Log.i("LOG", "search" + item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            finish(); // or perform any custom action
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private int getSelectedPositionForWeek(String week) {
        if (week.equals("Monday")) {
            return 0;
        } else if (week.equals("Tuesday")) {
            return 1;
        }else if (week.equals("Wednesday")) {
            return 2;
        }else if (week.equals("Thursday")) {
            return 3;
        }else if (week.equals("Friday")) {
            return 4;
        }else if (week.equals("Saturday")) {
            return 5;
        }else {
            return 6;
        }
    }

    private int getSelectedPositionForType(String type) {
        if (type.equals("Flow Yoga")) {
            return 0;
        }else if (type.equals("Aerial Yoga")) {
            return 1;
        }else {
            return 2;
        }
    }

    private int getSelectedPositionForLevel(String level) {
        if (level.equals("Beginner")) {
            return 0;
        }else if (level.equals("Intermediate")) {
            return 1;
        }else {
            return 2;
        }
    }
}