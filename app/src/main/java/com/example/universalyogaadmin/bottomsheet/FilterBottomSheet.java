package com.example.universalyogaadmin.bottomsheet;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.universalyogaadmin.FilterListener;
import com.example.universalyogaadmin.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    View v;

    ImageView ivBack;

    FilterListener listener;

    Button tvChoose;

    TextInputEditText etDate;

    private Spinner spinnerDayOfWeek;

    public FilterBottomSheet(FilterListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.filter_bottom_sheet, container, false);

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW);

        defineLayout();

        setOnClickListener();

        return v;
    }

    private void defineLayout() {
        ivBack = v.findViewById(R.id.ivBack);
        tvChoose = v.findViewById(R.id.tvChoose);

        spinnerDayOfWeek = v.findViewById(R.id.spinnerDayOfWeek);
        etDate = v.findViewById(R.id.etDate);
    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(view -> {
            dismiss();
        });

        tvChoose.setOnClickListener(view -> {
            String dayOfWeek = spinnerDayOfWeek.getSelectedItem().toString();
            String date = etDate.getText().toString();
            listener.search(date, dayOfWeek);
            dismiss();
        });

        etDate.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

    }

    private void showDatePickerDialog() {
        // Get the current date to display in the picker as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayOfWeekString = spinnerDayOfWeek.getSelectedItem().toString();
        // Convert day string to corresponding Calendar constant
        int dayOfWeek = getDayOfWeekFromString(dayOfWeekString);
        if (dayOfWeek == -1) {
            Toast.makeText( getContext(), "Invalid day of week", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    int selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

                    // Check if the selected day matches the specified day (e.g., Monday)
                    if (selectedDayOfWeek == dayOfWeek) {
                        // Format the selected date and set it in the EditText
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etDate.setText(selectedDate);
                    } else {
                        Toast.makeText(getContext(), "Please select a " + dayOfWeekString, Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, day);

        // Add a listener to filter the dates based on the day of the week
        datePickerDialog.getDatePicker().init(year, month, day, (view, year1, monthOfYear, dayOfMonth) -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year1, monthOfYear, dayOfMonth);
            int selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

            // If the selected day is not the desired day, automatically move to the next matching day
            if (selectedDayOfWeek != dayOfWeek) {
                while (selectedDayOfWeek != dayOfWeek) {
                    selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);
                }

                view.updateDate(
                        selectedCalendar.get(Calendar.YEAR),
                        selectedCalendar.get(Calendar.MONTH),
                        selectedCalendar.get(Calendar.DAY_OF_MONTH)
                );
            }
        });
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Helper function to map day string to Calendar constant
    private int getDayOfWeekFromString(String day) {
        Map<String, Integer> dayMap = new HashMap<>();
        dayMap.put("Sunday", Calendar.SUNDAY);
        dayMap.put("Monday", Calendar.MONDAY);
        dayMap.put("Tuesday", Calendar.TUESDAY);
        dayMap.put("Wednesday", Calendar.WEDNESDAY);
        dayMap.put("Thursday", Calendar.THURSDAY);
        dayMap.put("Friday", Calendar.FRIDAY);
        dayMap.put("Saturday", Calendar.SATURDAY);

        return dayMap.getOrDefault(day, -1); // Return -1 if the day is invalid
    }
}