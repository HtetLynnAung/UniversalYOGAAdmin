package com.example.universalyogaadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.activity.CourseDetailActivity;
import com.example.universalyogaadmin.model.YogaClass;

import java.util.ArrayList;

public class SearchClassAdapter extends RecyclerView.Adapter<SearchClassAdapter.ClassViewHolder> {
    private Context context;
    private ArrayList<YogaClass> yogaClasses;

    public SearchClassAdapter(Context context, ArrayList<YogaClass> yogaClasses) {
        this.context = context;
        this.yogaClasses = yogaClasses;
    }

    public void updateView( ArrayList<YogaClass> yogaClasses) {
        this.yogaClasses = yogaClasses;
        notifyDataSetChanged();
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        YogaClass yogaClass = yogaClasses.get(position);

        // Bind data to the view holder
        holder.tvDate.setText(yogaClass.getDate());
        holder.tvTeacher.setText("Teacher: " + yogaClass.getTeacher());
        holder.tvComment.setText(yogaClass.getComment());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("yoga_course_id", yogaClass.getCourseID());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return yogaClasses.size();
    }

    // ViewHolder class to hold the view for each item
    static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTeacher, tvComment;

        public ClassViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTeacher = itemView.findViewById(R.id.tvTeacher);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }

    // Optional: Method to update the list if the data changes
    public void updateData(ArrayList<YogaClass> newYogaClasses) {
        yogaClasses.clear();
        yogaClasses.addAll(newYogaClasses);
        notifyDataSetChanged();
    }
}
