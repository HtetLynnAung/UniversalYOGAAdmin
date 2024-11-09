package com.example.universalyogaadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.activities.CourseDetailActivity;
import com.example.universalyogaadmin.model.YogaCourseVO;

import java.util.ArrayList;

public class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.CourseViewHolder> {
    private Context context;
    private ArrayList<YogaCourseVO> yogaCoursVOS;

    public CourseItemAdapter(Context context, ArrayList<YogaCourseVO> yogaCoursVOS) {
        this.context = context;
        this.yogaCoursVOS = yogaCoursVOS;
    }

    public void updateView( ArrayList<YogaCourseVO> yogaCoursVOS) {
        Log.i("DATA", yogaCoursVOS.size() + "size");
        this.yogaCoursVOS = yogaCoursVOS;
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
        YogaCourseVO yogaCourseVO = yogaCoursVOS.get(position);

        // Bind data to the view holder
        holder.textViewDay.setText(yogaCourseVO.getDay());
        holder.textViewTime.setText(yogaCourseVO.getTime());
        holder.textViewCapacity.setText(yogaCourseVO.getCapacity()+ " Students");
        holder.textViewDuration.setText(yogaCourseVO.getDuration() + " mins");
        holder.textViewPrice.setText(yogaCourseVO.getPrice() + " £");
        holder.textViewType.setText(yogaCourseVO.getType());
        holder.textViewDescription.setText(yogaCourseVO.getDescription() != null ? yogaCourseVO.getDescription() : "No description");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("yoga_course_id", yogaCourseVO.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return yogaCoursVOS.size();
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
    public void updateData(ArrayList<YogaCourseVO> newYogaCoursVOS) {
        yogaCoursVOS.clear();
        yogaCoursVOS.addAll(newYogaCoursVOS);
        notifyDataSetChanged();
    }
}