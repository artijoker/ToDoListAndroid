package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.TaskList;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TasksFragment extends Fragment implements UpdateFragment {

    private final static String KEY_TASK_LIST = "keyTaskList";

    private Context context;
    private List<Task> tasks = new ArrayList<>();
    private Optional<TaskList> taskList = Optional.empty();

    private RecyclerView recyclerView;
    private LinearLayout linearLayout_noTasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = getArguments() != null ? Optional.of(getArguments().getParcelable("taskList")) : Optional.empty();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();
        View view = inflater.inflate(R.layout.tasks_fragment_page, container, false);
        recyclerView = view.findViewById(R.id.taskList);
        linearLayout_noTasks = view.findViewById(R.id.linearLayout_noTasks);

        ExtendedFloatingActionButton button_addTask = view.findViewById(R.id.button_addTask);

        button_addTask.setOnClickListener(v -> addTask());
        if (savedInstanceState != null) {
            taskList = Optional.of(savedInstanceState.getParcelable(KEY_TASK_LIST));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (taskList.isPresent()) {
            DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
            dbAdapter.open();
            tasks = dbAdapter.getTasksByListId(taskList.get().getId());
            if (tasks.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout_noTasks.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                linearLayout_noTasks.setVisibility(View.VISIBLE);
            }

            recyclerView.setAdapter(new RecyclerViewStateAdapter(context, tasks));
            dbAdapter.close();
        } else {
            recyclerView.setVisibility(View.GONE);
            linearLayout_noTasks.setVisibility(View.VISIBLE);
        }
    }

    public void addTask() {
        if (taskList.isPresent()) {
            Intent intent = new Intent(context, AddEditTaskActivity.class);
            intent.putExtra("taskList", taskList.get());
            startActivity(intent);
        }

    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        taskList.ifPresent(list -> savedInstanceState.putParcelable(KEY_TASK_LIST, list));
    }


    @Override
    public void sortToAbc() {
        tasks.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
        recyclerView.setAdapter(new RecyclerViewStateAdapter(context, tasks));
    }

    @Override
    public void sortToDate() {
        tasks.sort((o1, o2) -> {
            if (o1.getEndDate().isPresent() && o2.getEndDate().isPresent())
                return o1.getEndDate().get().compareTo(o2.getEndDate().get());
            else if (o1.getEndDate().isPresent() && !o2.getEndDate().isPresent())
                return 1;
            else if (!o1.getEndDate().isPresent() && o2.getEndDate().isPresent())
                return -1;
            else
                return 0;
        });
        recyclerView.setAdapter(new RecyclerViewStateAdapter(context, tasks));
    }
}
