package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.entities.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class RecyclerViewStateAdapter extends RecyclerView.Adapter<RecyclerViewStateAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Task> tasks;
    private final DatabaseAdapter dbAdapter;

    RecyclerViewStateAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
        dbAdapter = new DatabaseAdapter(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tasks.size() != 0) {
            Task task = tasks.get(position);

            holder.textView_title.setText("Задача: " + task.getTitle());
            holder.checkBox_completed.setChecked(task.isCompleted());
            holder.checkBox_tagged.setChecked(task.isTagged());

            if (task.isCompleted()) {
                holder.textView_title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.textView_title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                if (task.getDateWhenDone().isPresent()){
                    holder.textView_endDate.setText(
                            "Выполнено: " + task.getDateWhenDone().get()
                                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
                    );
                    holder.textView_endDate.setTextColor(Color.CYAN);
                }

            }
            else{
                if (task.getEndDate().isPresent())
                    holder.textView_endDate.setText(
                            "Выполнить до: " + task.getEndDate().get()
                                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    );
                else{
                    holder.textView_endDate.setText("Бессрочно");
                    holder.textView_endDate.setTextColor(Color.CYAN);
                }

            }


            holder.checkBox_completed.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    LocalDateTime dateTime = LocalDateTime.now();
                    holder.textView_title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.textView_endDate.setText("Выполнено: " + dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
                    holder.textView_endDate.setTextColor(Color.CYAN);
                    task.setDateWhenDone(Optional.of(dateTime));
                } else {

                    holder.textView_title.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                    if ( task.getEndDate().isPresent()){
                        holder.textView_endDate.setText("Выполнить до: " + task.getEndDate().get().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
                        holder.textView_endDate.setTextColor(Color.WHITE);
                    }
                    else{
                        holder.textView_endDate.setText("Бессрочно");
                        holder.textView_endDate.setTextColor(Color.CYAN);
                    }

                    task.setDateWhenDone(Optional.empty());
                }
                task.setCompleted(isChecked);
                dbAdapter.open();
                dbAdapter.updateTask(task);
                dbAdapter.close();
            });

            holder.checkBox_tagged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setTagged(isChecked);
                    dbAdapter.open();
                    dbAdapter.updateTask(task);
                    dbAdapter.close();
                }
            });


            holder.gridLayout_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, AddEditTaskActivity.class);
                    intent.putExtra("task", task);
                    context.startActivity(intent);
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
        final TextView textView_title, textView_endDate;
        final GridLayout gridLayout_task;

        ViewHolder(View view) {
            super(view);
            checkBox_completed = view.findViewById(R.id.checkBox_completed);
            checkBox_tagged = view.findViewById(R.id.checkBox_tagged);
            textView_title = view.findViewById(R.id.textView_title);
            textView_endDate = view.findViewById(R.id.textView_endDate);
            gridLayout_task = view.findViewById(R.id.gridLayout_task);
        }
    }
}
