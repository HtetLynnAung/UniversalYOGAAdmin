package com.example.universalyogaadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyogaadmin.ClassManageListener;
import com.example.universalyogaadmin.R;
import com.example.universalyogaadmin.model.YogaClassVO;

import java.util.ArrayList;

public class ClassItemAdapter extends RecyclerView.Adapter<ClassItemAdapter.ClassViewHolder> {
    private Context context;
    private ArrayList<YogaClassVO> yogaClassVOS;
    private ClassManageListener listener;

    public ClassItemAdapter(Context context, ClassManageListener listener, ArrayList<YogaClassVO> yogaClassVOS) {
        this.context = context;
        this.listener = listener;
        this.yogaClassVOS = yogaClassVOS;
    }

    public void updateView( ArrayList<YogaClassVO> yogaClassVOS) {
        this.yogaClassVOS = yogaClassVOS;
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
        YogaClassVO yogaClassVO = yogaClassVOS.get(position);

        // Bind data to the view holder
        holder.tvDate.setText(yogaClassVO.getDate());
        holder.tvTeacher.setText("Teacher: " + yogaClassVO.getTeacher());
        holder.tvComment.setText(yogaClassVO.getComment());

        holder.ivEdit.setOnClickListener(v -> {
            listener.upClass(yogaClassVO.getId());
        });

        holder.ivDelete.setOnClickListener(v -> {
           listener.delClass(yogaClassVO.getId());
        });
    }

    @Override
    public int getItemCount() {
        return yogaClassVOS.size();
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
    public void updateData(ArrayList<YogaClassVO> newYogaClassVOS) {
        yogaClassVOS.clear();
        yogaClassVOS.addAll(newYogaClassVOS);
        notifyDataSetChanged();
    }
}
