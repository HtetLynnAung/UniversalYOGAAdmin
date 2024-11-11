package com.example.universalyogaadmin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.universalyogaadmin.adapters.ClassItemAdapter;
import com.example.universalyogaadmin.database.DBHelper;
import com.example.universalyogaadmin.model.YogaClassVO;
import com.example.universalyogaadmin.model.YogaCourseVO;
import com.example.universalyogaadmin.model.api.ResponseBody;
import com.example.universalyogaadmin.model.api.YogaClassRequestBody;
import com.example.universalyogaadmin.model.api.YogaCourseRequestBody;
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
    private DBHelper DBHelper;
    ClassItemAdapter classItemAdapter;

    ArrayList<YogaClassVO> yogaClassVOS;

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

        setupFindViewByIds();

        setUpRecyclerView();

        setOnClickListener();

        listenInternetConnection();

        courseID = getIntent().getIntExtra("yoga_course_id", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClassDetails(courseID);
        updateClassData();
    }

    private void setupFindViewByIds() {
        tvName = findViewById(R.id.tvDayTime);
        tvCapacity = findViewById(R.id.tvPplCount);
        tvPrice = findViewById(R.id.tvCoursePrice);
        tvDuration = findViewById(R.id.tvTime);
        tvYogaType = findViewById(R.id.tvTypeYoga);
        tvYogaLevel = findViewById(R.id.tvYogaDifficulty);
        tvDescription = findViewById(R.id.tvDesc);
        ivAddClass = findViewById(R.id.ivAddClass);
        rvClass = findViewById(R.id.rvClass);
        buttonPublish = findViewById(R.id.buttonPublish);
        DBHelper = new DBHelper(this);
        buttonEdit  = findViewById(R.id.buttonEdit);
        buttonDel = findViewById(R.id.buttonDelete);
    }

    private void listenInternetConnection() {
        networkLiveData = new NetworkLiveData(this);
        networkLiveData.observe(this, isConnected -> {
            isInternetAvailable = isConnected;
        });
    }

    private void checkValidationAndSummitToDB() {
        if (isInternetAvailable) {
            if (DBHelper.updateCourseIsPublished(courseID, true)) {
                ApiRoute yogaApi = ApiClient.getClient().create(ApiRoute.class);
                List<YogaClassRequestBody> yogaClassesVO = new ArrayList<>();
                for (YogaClassVO yogaClassVO : yogaClassVOS) {
                    yogaClassesVO.add(yogaClassVO.changeYogaClassVO());
                }
                YogaCourseRequestBody yogaCourseRequestBody = DBHelper.getYogaCourse(courseID).changYogaCourseVO(yogaClassesVO);
                // Send request
                Call<ResponseBody> call = yogaApi.sendYogaCourse(yogaCourseRequestBody);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(CourseDetailActivity.this, "Yoga course is successfully published!", Toast.LENGTH_SHORT).show();
                            loadClassDetails(courseID);
                        } else {
                            Toast.makeText(CourseDetailActivity.this, "Failed to send yoga course!", Toast.LENGTH_SHORT).show();
                            DBHelper.updateCourseIsPublished(courseID, false);
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
        YogaCourseVO yogaCourseVO = DBHelper.getYogaCourse(id);
        isPublished = yogaCourseVO.getIsPublished();

        buttonPublish.setVisibility(isPublished ? View.GONE : View.VISIBLE);
        tvName.setText(yogaCourseVO.getDay()+" - " + yogaCourseVO.getTime());
        tvCapacity.setText(yogaCourseVO.getCapacity() + "");
        tvDuration.setText(yogaCourseVO.getDuration() + "");
        tvPrice.setText(yogaCourseVO.getPrice()+"");
        tvYogaType.setText(yogaCourseVO.getType());
        tvYogaLevel.setText(yogaCourseVO.getLevel());
        tvDescription.setVisibility(yogaCourseVO.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        tvDescription.setText(yogaCourseVO.getDescription());
    }

    private void setUpRecyclerView() {
        yogaClassVOS = new ArrayList<>();
        classItemAdapter = new ClassItemAdapter(this, this, yogaClassVOS);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( this, 1);
        rvClass.setLayoutManager(layoutManager);
        rvClass.setItemAnimator(new DefaultItemAnimator());
        rvClass.setAdapter(classItemAdapter);
    }

    private void updateClassData() {
        yogaClassVOS = DBHelper.getAllYogaClasses(courseID);
        classItemAdapter.updateView(yogaClassVOS);
    }

    private void setOnClickListener() {
        ivAddClass.setOnClickListener(view -> {
            navigateToAddClass();
        });

        // Set click listener for the submit button
        buttonPublish.setOnClickListener(view -> checkValidationAndSummitToDB());

        // edit button click listener
        buttonEdit.setOnClickListener(view -> navigateToEditActivity());
        // delete button click listener
        buttonDel.setOnClickListener(view -> createDeleteAlertDialog());

    }

    private void navigateToAddClass() {
        Intent myIntent = new Intent(CourseDetailActivity.this, AddClass.class);
        myIntent.putExtra("yoga_course_id", courseID );
        this.startActivity(myIntent);
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
        DBHelper.deleteCourse(courseID);
    }

    private void createAlertDialog(int classID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons.
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User taps OK button.
                DBHelper.deleteClass(classID);
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

    private void navigateToEditActivity(){
        Intent myIntent = new Intent(CourseDetailActivity.this, CourseUpdateActivity.class);
        myIntent.putExtra("yoga_course_id", courseID);
        startActivity(myIntent);
    }

    //Override methods
    @Override
    public void delClass(int classID) {
        createAlertDialog(classID);
    }

    @Override
    public void upClass(int classID) {
        Intent intent = new Intent(CourseDetailActivity.this, ClassUpdateActivity.class);
        intent.putExtra("yoga_course_id", courseID);
        intent.putExtra("yoga_class_id", classID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
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
}

