package com.example.universalyogaadmin.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.AdvancedSearchListener;
import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.adapters.SearchClassItemAdapter;
import com.example.universalyogaadmin.bottomsheet.FilterBottomSheet;
import com.example.universalyogaadmin.database.DBHelper;
import com.example.universalyogaadmin.model.YogaClassVO;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements AdvancedSearchListener {

    ImageView ivFilter;

    SearchView svSearch;

    TextView tvFilterBy;

    RecyclerView rvPlanner;

    DBHelper dbHelper;

    ArrayList<YogaClassVO> yogaClassVOS;

    String dayOfWeek = "", date = "", teacherName = "";

    SearchClassItemAdapter classAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        setupFindViewByIds();

        setOnClickListener();

        setUpRecyclerView();
    }

    private void setupFindViewByIds() {
        svSearch = findViewById(R.id.svSearch);
        svSearch.setIconified(false);
        svSearch.requestFocus();

        ivFilter = findViewById(R.id.ivFilter);
        tvFilterBy = findViewById(R.id.tvFilterBy);
        rvPlanner = findViewById(R.id.rvPlanner);
    }

    private void setOnClickListener() {

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterBottomSheet();
            }
        });

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query
                // You can perform a search operation here
                Log.i("Search", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle query text changes

                if(newText.isEmpty()) {
                    classAdapter.updateView(yogaClassVOS);
                } else {
                    teacherName = newText;
                    searchClass();
                }
                return true;
            }
        });

        svSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                classAdapter.updateView(yogaClassVOS);
                return false;
            }
        });

    }

    private void searchClass() {
        classAdapter.updateView(dbHelper.searchClass(teacherName, date, dayOfWeek));
    }

    private void setUpRecyclerView() {
        yogaClassVOS = new ArrayList<>();
        classAdapter = new SearchClassItemAdapter(this, yogaClassVOS);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( this, 1);
        rvPlanner.setLayoutManager(layoutManager);
        rvPlanner.setItemAnimator(new DefaultItemAnimator());
        rvPlanner.setAdapter(classAdapter);
    }

    private void showFilterBottomSheet() {
        FilterBottomSheet bottomSheet = new FilterBottomSheet(this);
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    // Override methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        if (item.getItemId() == android.R.id.home) {
            finish(); // or perform any custom action
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void search(String date, String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        String filteredBy = "Filtered By:";

        if(!date.isEmpty()) {
            filteredBy += " Date: " + date;
        }

        if (!dayOfWeek.isEmpty()) {
            filteredBy += " " + dayOfWeek;
        }

        tvFilterBy.setText(filteredBy);
        searchClass();
    }

}