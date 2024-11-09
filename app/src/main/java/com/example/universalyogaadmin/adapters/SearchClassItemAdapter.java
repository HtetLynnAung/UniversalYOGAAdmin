package com.example.universalyogaadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.activities.CourseDetailActivity;
import com.example.universalyogaadmin.model.YogaClassVO;

import java.util.ArrayList;

public class SearchClassItemAdapter extends RecyclerView.Adapter<SearchClassItemAdapter.ClassViewHolder> {
    private Context context;
    private ArrayList<YogaClassVO> yogaClassVOS;

    public SearchClassItemAdapter(Context context, ArrayList<YogaClassVO> yogaClassVOS) {
        this.context = context;
        this.yogaClassVOS = yogaClassVOS;
    }

    public void updateView( ArrayList<YogaClassVO> yogaClassVOS) {
        this.yogaClassVOS = yogaClassVOS;
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
        YogaClassVO yogaClassVO = yogaClassVOS.get(position);

        // Bind data to the view holder
        holder.tvDate.setText(yogaClassVO.getDate());
        holder.tvTeacher.setText("Teacher: " + yogaClassVO.getTeacher());
        holder.tvComment.setText(yogaClassVO.getComment());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("yoga_course_id", yogaClassVO.getCourseID());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return yogaClassVOS.size();
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
    public void updateData(ArrayList<YogaClassVO> newYogaClassVOS) {
        yogaClassVOS.clear();
        yogaClassVOS.addAll(newYogaClassVOS);
        notifyDataSetChanged();
    }
}
