package com.example.universalyogaadmin.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.*;
import com.example.universalyogaadmin.adapter.CourseAdapter;
import com.example.universalyogaadmin.database.DatabaseHelper;
import com.example.universalyogaadmin.model.YogaCourse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewClasses;
    private CourseAdapter adapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAddCourse;


    // Retrieve the data as an ArrayList
    ArrayList<YogaCourse> yogaCourses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Initialize components
        recyclerViewClasses = findViewById(R.id.recyclerViewClasses);
        fabAddCourse = findViewById(R.id.fabAddCourse);
        databaseHelper = new DatabaseHelper(this);

       setUpRecyclerView();
        fabAddCourse.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        // Handle Floating Action Button click to add new course
        fabAddCourse.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateCourse.class);
            startActivity(intent);
        });
    }


    private void setUpRecyclerView() {
        yogaCourses = new ArrayList<>();
        adapter = new CourseAdapter(this, yogaCourses);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( this, 1);
        recyclerViewClasses.setLayoutManager(layoutManager);
        recyclerViewClasses.setItemAnimator(new DefaultItemAnimator());
        recyclerViewClasses.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when coming back from AddCourseActivity
        updateCourseData();
    }

    private void updateCourseData() {
        yogaCourses = databaseHelper.getAllYogaCourses();
        adapter.updateView(yogaCourses);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    private void resetAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons.
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User taps OK button.
                databaseHelper.resetDatabase();
                updateCourseData();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancels the dialog.
                dialog.dismiss();
            }
        });

        // Create the AlertDialog.
        AlertDialog dialog = builder.create();
        dialog.setTitle("Delete");
        dialog.setMessage("Do you want to reset all courses?");
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        if(item.getItemId() == R.id.search) {
            Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        } else if(item.getItemId() == R.id.reset) {
            resetAlertDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}