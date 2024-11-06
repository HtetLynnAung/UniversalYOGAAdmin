package com.example.universalyogaadmin.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.ClassManageListener;
import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.adapter.ClassAdapter;
import com.example.universalyogaadmin.database.DatabaseHelper;
import com.example.universalyogaadmin.model.YogaClass;
import com.example.universalyogaadmin.model.YogaCourse;
import com.example.universalyogaadmin.model.api.ResponseBody;
import com.example.universalyogaadmin.model.api.YogaClassVO;
import com.example.universalyogaadmin.model.api.YogaCourseVO;
import com.example.universalyogaadmin.network.ApiClient;
import com.example.universalyogaadmin.network.ApiRoute;
import com.example.universalyogaadmin.network.NetworkLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailActivity extends AppCompatActivity implements ClassManageListener {

    private TextView tvName, tvCapacity, tvPrice, tvDuration, tvDescription, tvYogaType, tvYogaLevel;
    private Button buttonPublish, buttonEdit, buttonDel;
    Button ivAddClass;
    RecyclerView rvClass;
    private DatabaseHelper databaseHelper;
    ClassAdapter classAdapter;

    ArrayList<YogaClass> yogaClasses;

    private boolean isInternetAvailable = false;
    private boolean isPublished = false;
    private  int courseID = -1;
    private NetworkLiveData networkLiveData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_detail);

        getSupportActionBar().setTitle("Course Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tvNameTime);
        tvCapacity = findViewById(R.id.tvPplCount);
        tvPrice = findViewById(R.id.tvCoursePrice);
        tvDuration = findViewById(R.id.tvTime);
        tvYogaType = findViewById(R.id.tvTypeYoga);
        tvYogaLevel = findViewById(R.id.tvYogaDifficulty);
        tvDescription = findViewById(R.id.tvDesc);
        ivAddClass = findViewById(R.id.ivAddClass);
        rvClass = findViewById(R.id.rvClass);
        buttonPublish = findViewById(R.id.buttonPublish);
        databaseHelper = new DatabaseHelper(this);
        buttonEdit  = findViewById(R.id.buttonEdit);
        buttonDel = findViewById(R.id.buttonDelete);

        setUpRecyclerView();
        setOnClickListener();

        networkLiveData = new NetworkLiveData(this);
        networkLiveData.observe(this, isConnected -> {
            isInternetAvailable = isConnected;
        });

        courseID = getIntent().getIntExtra("yoga_course_id", -1);

        // Set click listener for the submit button
        buttonPublish.setOnClickListener(view -> validateAndSubmit());

        // edit button click listener
        buttonEdit.setOnClickListener(view -> goToEditActivity());
        buttonDel.setOnClickListener(view -> createDeleteAlertDialog());

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClassDetails(courseID);
        updateClassData();
    }

    private void validateAndSubmit() {
        if (isInternetAvailable) {
            if (databaseHelper.updateCourseIsPublished(courseID, true)) {
                ApiRoute yogaApi = ApiClient.getClient().create(ApiRoute.class);
                List<YogaClassVO> yogaClassesVO = new ArrayList<>();
                for (YogaClass yogaClass : yogaClasses) {
                    yogaClassesVO.add(yogaClass.changeYogaClassVO());
                }
                YogaCourseVO yogaCourseVO = databaseHelper.getYogaCourse(courseID).changYogaCourseVO(yogaClassesVO);
                // Send request
                Call<ResponseBody> call = yogaApi.sendYogaCourse(yogaCourseVO);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(CourseDetailActivity.this, "Yoga course is successfully published!", Toast.LENGTH_SHORT).show();
                            loadClassDetails(courseID);
                        } else {
                            Toast.makeText(CourseDetailActivity.this, "Failed to send yoga course!", Toast.LENGTH_SHORT).show();
                            databaseHelper.updateCourseIsPublished(courseID, false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        Toast.makeText(CourseDetailActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        } else {
            Toast.makeText(this, "Please check your internet connection " , Toast.LENGTH_SHORT).show();
        }


    }

    private void loadClassDetails(int id) {
        YogaCourse yogaCourse = databaseHelper.getYogaCourse(id);
        isPublished = yogaCourse.getIsPublished();

        buttonPublish.setVisibility(isPublished ? View.GONE : View.VISIBLE);
        tvName.setText(yogaCourse.getDay()+" - " + yogaCourse.getTime());
        tvCapacity.setText(yogaCourse.getCapacity() + "");
        tvDuration.setText(yogaCourse.getDuration() + "");
        tvPrice.setText(yogaCourse.getPrice()+"");
        tvYogaType.setText(yogaCourse.getType());
        tvYogaLevel.setText(yogaCourse.getLevel());
        tvDescription.setVisibility(yogaCourse.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        tvDescription.setText(yogaCourse.getDescription());
    }

    private void setUpRecyclerView() {
        yogaClasses = new ArrayList<>();
        classAdapter = new ClassAdapter(this, this, yogaClasses);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( this, 1);
        rvClass.setLayoutManager(layoutManager);
        rvClass.setItemAnimator(new DefaultItemAnimator());
        rvClass.setAdapter(classAdapter);
    }

    private void updateClassData() {
        yogaClasses = databaseHelper.getAllYogaClasses(courseID);
        classAdapter.updateView(yogaClasses);
    }

    private void setOnClickListener() {
        ivAddClass.setOnClickListener(view -> {
            navigateToAddClass();
        });
    }

    private void navigateToAddClass() {
        Intent myIntent = new Intent(CourseDetailActivity.this, CreateClass.class);
        myIntent.putExtra("yoga_course_id", courseID );
        this.startActivity(myIntent);
    }


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

    private void createDeleteAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons.
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User taps OK button.
                finish();
                deleteObservation();
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
        dialog.setMessage("Do you want to delete this course?");
        dialog.show();
    }

    private void deleteObservation() {
        databaseHelper.deleteCourse(courseID);
    }

    @Override
    public void delClass(int classID) {

        createAlertDialog(classID);
    }

    private void createAlertDialog(int classID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons.
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User taps OK button.
                databaseHelper.deleteClass(classID);
                updateClassData();
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
        dialog.setMessage("Do you want to delete this class?");
        dialog.show();
    }

    @Override
    public void upClass(int classID) {
        Intent intent = new Intent(CourseDetailActivity.this, EditClassActivity.class);
        intent.putExtra("yoga_course_id", courseID);
        intent.putExtra("yoga_class_id", classID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void goToEditActivity(){
        Intent myIntent = new Intent(CourseDetailActivity.this, EditCourseActivity.class);
        myIntent.putExtra("yoga_course_id", courseID);
        startActivity(myIntent);
    }
}

