package com.example.universalyogaadmin.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.database.DatabaseHelper;
import com.example.universalyogaadmin.model.YogaCourse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class EditCourseActivity extends AppCompatActivity {

    private Spinner spinnerDayOfWeek, spinnerClassType, spinnerDifficultyLevel;

    private TextInputEditText editTextTime, editTextCapacity, editTextDescription, editTextPrice, editTextDuration;

    private DatabaseHelper databaseHelper;
    private Button BtnUpdate;
    private int courseID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_course);

        getSupportActionBar().setTitle("Edit Class");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        spinnerClassType = findViewById(R.id.spinnerClassType);
        spinnerDifficultyLevel = findViewById(R.id.spinnerDifficultyLevel);
        editTextTime = findViewById(R.id.editTextTime);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        databaseHelper = new DatabaseHelper(this);
        BtnUpdate = findViewById(R.id.buttonUpdate);

        courseID = getIntent().getIntExtra("yoga_course_id", -1);
        loadClassDetails(courseID);
        setUpTimePicker();
        BtnUpdate.setOnClickListener(view -> validateAndSubmit());
    }



    private void loadClassDetails(int id) {
        YogaCourse yogaCourse = databaseHelper.getYogaCourse(id);

        editTextTime.setText(yogaCourse.getTime());
        editTextCapacity.setText(yogaCourse.getCapacity()+"");
        editTextDuration.setText(yogaCourse.getDuration() +"");
        editTextPrice.setText(yogaCourse.getPrice()+"");
        editTextDescription.setText(yogaCourse.getDescription());
        spinnerDayOfWeek.setSelection(getSelectedPositionForWeek(yogaCourse.getDay()));
        spinnerClassType.setSelection(getSelectedPositionForType(yogaCourse.getType()));
        spinnerDifficultyLevel.setSelection(getSelectedPositionForLevel(yogaCourse.getLevel()));
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
        YogaCourse yogaCourse = new YogaCourse(courseID, day, time, capacity, duration, price, classType, level, description, false);
        boolean isUpdateded = databaseHelper.updateCourse(courseID, yogaCourse);
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