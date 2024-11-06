package com.example.universalyogaadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.ClassManageListener;
import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.activity.CourseDetailActivity;
import com.example.universalyogaadmin.model.YogaClass;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private Context context;
    private ArrayList<YogaClass> yogaClasses;
    private ClassManageListener listener;

    public ClassAdapter(Context context,ClassManageListener listener, ArrayList<YogaClass> yogaClasses) {
        this.context = context;
        this.listener = listener;
        this.yogaClasses = yogaClasses;
    }

    public void updateView( ArrayList<YogaClass> yogaClasses) {
        this.yogaClasses = yogaClasses;
        notifyDataSetChanged();
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        YogaClass yogaClass = yogaClasses.get(position);

        // Bind data to the view holder
        holder.tvDate.setText(yogaClass.getDate());
        holder.tvTeacher.setText("Teacher: " + yogaClass.getTeacher());
        holder.tvComment.setText(yogaClass.getComment());

        holder.ivEdit.setOnClickListener(v -> {
            listener.upClass(yogaClass.getId());
        });

        holder.ivDelete.setOnClickListener(v -> {
           listener.delClass(yogaClass.getId());
        });
    }

    @Override
    public int getItemCount() {
        return yogaClasses.size();
    }

    // ViewHolder class to hold the view for each item
    static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTeacher, tvComment;
        ImageView ivEdit, ivDelete;

        public ClassViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTeacher = itemView.findViewById(R.id.tvTeacher);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    // Optional: Method to update the list if the data changes
    public void updateData(ArrayList<YogaClass> newYogaClasses) {
        yogaClasses.clear();
        yogaClasses.addAll(newYogaClasses);
        notifyDataSetChanged();
    }
}
