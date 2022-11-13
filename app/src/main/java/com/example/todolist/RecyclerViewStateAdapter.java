package com.example.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.entities.Task;

import java.util.List;

public class RecyclerViewStateAdapter extends RecyclerView.Adapter<RecyclerViewStateAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Task> tasks;

    RecyclerViewStateAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tasks.size() != 0){
            Task task = tasks.get(position);
            if (task.isCompleted()) {

                holder.textView_title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.textView_title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.textView_completed.setVisibility(View.VISIBLE);
                holder.layout_endDate.setVisibility(View.GONE);
            }
            holder.checkBox_completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        holder.textView_title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.textView_completed.setVisibility(View.VISIBLE);
                        holder.layout_endDate.setVisibility(View.GONE);

                    } else{
                        holder.textView_title.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                        holder.textView_completed.setVisibility(View.GONE);
                        holder.layout_endDate.setVisibility(View.VISIBLE);
                    }

                }
            });
            holder.gridLayout_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(
                            v.getContext(),
                            holder.textView_title.getText(),
                            Toast.LENGTH_SHORT
                    );
                    toast.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final CheckBox checkBox_completed, checkBox_tagged;
        final TextView textView_title, textView_endDate, textView_completed;
        final LinearLayout layout_endDate;
        final GridLayout gridLayout_task;
        ViewHolder(View view) {
            super(view);
            checkBox_completed = view.findViewById(R.id.checkBox_completed);
            checkBox_tagged = view.findViewById(R.id.checkBox_tagged);
            textView_title = view.findViewById(R.id.textView_title);
            textView_endDate = view.findViewById(R.id.textView_endDate);
            textView_completed = view.findViewById(R.id.textView_completed);
            layout_endDate = view.findViewById(R.id.layout_endDate);
            gridLayout_task = view.findViewById(R.id.gridLayout_task);
        }
    }
}
