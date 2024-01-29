package com.example.project_acadeamease1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ScheduleAdapter extends FirestoreRecyclerAdapter<SchedModel, ScheduleAdapter.ScheduleViewHolder> {

    Context context;

    public ScheduleAdapter(@NonNull FirestoreRecyclerOptions<SchedModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position, @NonNull SchedModel schedModel) {
        holder.scheduleSubject.setText(schedModel.schedSubject);
        holder.scheduleTime.setText(schedModel.subjectTime);

    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_schedule_item,parent,false);
        return new ScheduleViewHolder(view);
    }


    class ScheduleViewHolder extends RecyclerView.ViewHolder{
        TextView scheduleSubject, scheduleTime;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            scheduleSubject = itemView.findViewById(R.id.schedule_Subject);
            scheduleTime = itemView.findViewById(R.id.schedule_Time);

        }
    }
}

