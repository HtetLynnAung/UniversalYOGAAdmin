package com.example.universalyogaadmin.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateCourseActivity extends AppCompatActivity {

    private Spinner spinnerDayOfWeek, spinnerClassType, spinnerDifficultyLevel;

    private TextInputEditText editTextDescription;

    private Button buttonSave;

    private DBHelper DBHelper;

    private EditText editTextCapacity, editTextDuration, editTextPrice, editTextTime;

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

        buttonSave.setOnClickListener(view -> validateAndSubmit());
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
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void setUpTimePicker() {
        // Disable direct input for time EditText
        editTextTime.setInputType(0);
        editTextTime.setFocusable(false);

        // Show TimePickerDialog when editTextTime is clicked
        editTextTime.setOnClickListener(v -> showTimePickerDialog());
    }

    private void showTimePickerDialog() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {

                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    editTextTime.setText(formattedTime);
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }


    private void validateAndSubmit() {

        String day = spinnerDayOfWeek.getSelectedItem().toString();
        String time = editTextTime.getText().toString().trim();
        String capacity = editTextCapacity.getText().toString().trim();
        String duration = editTextDuration.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String classType = spinnerClassType.getSelectedItem().toString();
        String level = spinnerDifficultyLevel.getSelectedItem().toString();


        if (day.isEmpty() || time.isEmpty() || capacity.isEmpty() || duration.isEmpty() || price.isEmpty() || classType.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }


        showConfirmationDialog(day, time, capacity, duration, price, classType,level, editTextDescription.getText().toString());
    }

    private void showConfirmationDialog(String day, String time, String capacity, String duration, String price, String classType,String level, String description) {

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

        boolean isInserted = DBHelper.addCourseToDB(day, time, capacity, duration, price, classType, level, description, false);
        if (isInserted) {
            Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show();
            finish();  // Close activity and go back to the list
        } else {
            Toast.makeText(this, "Failed to add course.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}