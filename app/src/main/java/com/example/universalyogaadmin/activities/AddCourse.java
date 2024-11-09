package com.example.universalyogaadmin.activities;

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

import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.database.DBHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddCourse extends AppCompatActivity {

    private Spinner spinnerDayOfWeek, spinnerClassType, spinnerDifficultyLevel;

    private TextInputEditText editTextTime, editTextCapacity, editTextDescription, editTextPrice, editTextDuration;

    private Button btnSave;

    private DBHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_course);

        getSupportActionBar().setTitle("Add New Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupFindViewByIds();

        DBHelper = new DBHelper(this);

        setUpTimePicker();
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
        showConfirmationDialog(day, time, capacity, duration, price, classType,level, editTextDescription.getText().toString());
    }

    private void showConfirmationDialog(String day, String time, String capacity, String duration, String price, String classType,String level, String description) {
        // Show confirmation dialog or new activity
        new AlertDialog.Builder(this)
                .setTitle("Confirm Details")
                .setMessage("Day: " + day + "\nTime: " + time + "\nCapacity: " + capacity +
                        "\nDuration: " + duration + " minutes\nPrice: £" + price +
                        "\nClass Type: " + classType + "\nLevel: " + level + "\nDescription: " + description)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // Save data to database
                    saveToDatabase(day, time, Integer.parseInt(capacity), Integer.parseInt(duration), Double.parseDouble(price), classType, level, description);
                })
                .setNegativeButton("Edit", null)
                .show();
    }

    private void saveToDatabase(String day, String time, int capacity, int duration, double price, String classType, String level, String description) {
        // Save class details to the SQLite database
        // Implementation of database insertion goes here
        // Add the course to the database
        boolean isInserted = DBHelper.addCourseToDB(day, time, capacity, duration, price, classType, level, description, false);
        if (isInserted) {
            Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show();
            finish();  // Close activity and go back to the list
        } else {
            Toast.makeText(this, "Failed to add course.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        Log.i("LOG", "search" + item.getItemId());

        if(item.getItemId() == R.id.save) {
            validateAndSubmit();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish(); // or perform any custom action
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}