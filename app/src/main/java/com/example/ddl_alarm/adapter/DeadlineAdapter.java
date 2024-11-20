package com.example.ddl_alarm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ddl_alarm.R;
import com.example.ddl_alarm.model.Deadline;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DeadlineAdapter extends RecyclerView.Adapter<DeadlineAdapter.ViewHolder> {
    private List<Deadline> deadlines;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView descriptionView;
        public TextView dueDateView;
        public TextView remainingTimeView;

        public ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.deadline_title);
            descriptionView = view.findViewById(R.id.deadline_description);
            dueDateView = view.findViewById(R.id.deadline_due_date);
            remainingTimeView = view.findViewById(R.id.deadline_remaining);
        }
    }

    public DeadlineAdapter(List<Deadline> deadlines) {
        this.deadlines = deadlines;
        sortDeadlinesByRemainingTime();
    }

    private void sortDeadlinesByRemainingTime() {
        Collections.sort(deadlines, (d1, d2) -> {
            long diff1 = d1.getDueDate().getTime() - System.currentTimeMillis();
            long diff2 = d2.getDueDate().getTime() - System.currentTimeMillis();
            return Long.compare(diff1, diff2);
        });
    }

    public void updateDeadlines(List<Deadline> newDeadlines) {
        this.deadlines = newDeadlines;
        sortDeadlinesByRemainingTime();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.deadline_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Deadline deadline = deadlines.get(position);
        holder.titleView.setText(deadline.getTitle());
        holder.descriptionView.setText(deadline.getDescription());
        holder.dueDateView.setText(dateFormat.format(deadline.getDueDate()));
        holder.remainingTimeView.setText(calculateRemainingTime(deadline));
    }

    @Override
    public int getItemCount() {
        return deadlines.size();
    }

    private String calculateRemainingTime(Deadline deadline) {
        long diff = deadline.getDueDate().getTime() - System.currentTimeMillis();
        long days = diff / (24 * 60 * 60 * 1000);
        long hours = (diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        return String.format("剩余 %d 天 %d 小时", days, hours);
    }
} 