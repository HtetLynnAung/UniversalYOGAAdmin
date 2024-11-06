package com.example.universalyogaadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.activity.CourseDetailActivity;
import com.example.universalyogaadmin.model.YogaCourse;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private Context context;
    private ArrayList<YogaCourse> yogaCourses;

    public CourseAdapter(Context context, ArrayList<YogaCourse> yogaCourses) {
        this.context = context;
        this.yogaCourses = yogaCourses;
    }

    public void updateView( ArrayList<YogaCourse> yogaCourses) {
        Log.i("DATA", yogaCourses.size() + "size");
        this.yogaCourses = yogaCourses;
        notifyDataSetChanged();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        YogaCourse yogaCourse = yogaCourses.get(position);

        // Bind data to the view holder
        holder.textViewDay.setText(yogaCourse.getDay());
        holder.textViewTime.setText(yogaCourse.getTime());
        holder.textViewCapacity.setText(yogaCourse.getCapacity()+ " Students");
        holder.textViewDuration.setText(yogaCourse.getDuration() + " mins");
        holder.textViewPrice.setText(yogaCourse.getPrice() + " £");
        holder.textViewType.setText(yogaCourse.getType());
        holder.textViewDescription.setText(yogaCourse.getDescription() != null ? yogaCourse.getDescription() : "No description");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("yoga_course_id", yogaCourse.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return yogaCourses.size();
    }

    // ViewHolder class to hold the view for each item
    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay, textViewTime, textViewCapacity, textViewDuration, textViewPrice, textViewType, textViewDescription;

        public CourseViewHolder(View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.textViewDay);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewCapacity = itemView.findViewById(R.id.textViewCapacity);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }

    // Optional: Method to update the list if the data changes
    public void updateData(ArrayList<YogaCourse> newYogaCourses) {
        yogaCourses.clear();
        yogaCourses.addAll(newYogaCourses);
        notifyDataSetChanged();
    }
}