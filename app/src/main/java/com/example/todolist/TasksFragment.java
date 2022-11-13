package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.TaskList;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TasksFragment extends Fragment {
    private Context context;
    private List<Task> tasks = new ArrayList<>();
    private final Optional<TaskList> taskList;

    private RecyclerView recyclerView;
    private LinearLayout linearLayout_noTasks;

    public TasksFragment(TaskList taskList) {
        this.taskList = Optional.of(taskList);
    }

    public TasksFragment() {
        this.taskList = Optional.empty();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();

        View view = inflater.inflate(R.layout.tasks_fragment_page, container, false);
        recyclerView = view.findViewById(R.id.taskList);
        linearLayout_noTasks = view.findViewById(R.id.linearLayout_noTasks);

        ExtendedFloatingActionButton button_addTask = view.findViewById(R.id.button_addTask);

        button_addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (taskList.isPresent()) {
            DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
            dbAdapter.open();
            tasks = dbAdapter.getTasksByListId(taskList.get().getId());
            if (tasks.size() > 0){
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout_noTasks.setVisibility(View.GONE);
            }
            else {
                recyclerView.setVisibility(View.GONE);
                linearLayout_noTasks.setVisibility(View.VISIBLE);
            }

            recyclerView.setAdapter(new RecyclerViewStateAdapter(context, tasks));
            dbAdapter.close();


        }
    }

    public void addTask() {
        Intent intent = new Intent(context, AddEditTaskActivity.class);
        startActivity(intent);
    }
}
