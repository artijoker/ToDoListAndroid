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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TasksFragment extends Fragment {

    private final static String KEY_CONTEXT = "keyContext";
    private final static String KEY_TASKS= "keyTasks";
    private final static String KEY_TASK_LIST= "keyTaskList";
    private final static String KEY_RECYCLER_VIEW= "keyRecyclerView";
    private final static String KEY_LINEAR_LAYOUT= "keyLinearLayout";

    private Context context;
    private List<Task> tasks = new ArrayList<>();
    private Optional<TaskList> taskList = Optional.empty();

    private RecyclerView recyclerView;
    private LinearLayout linearLayout_noTasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = getArguments() != null ? Optional.of(getArguments().getParcelable("taskList"))  : Optional.empty();
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
            taskList = Optional.of(savedInstanceState.getParcelable(KEY_TASK_LIST)) ;
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
        if (taskList.isPresent()){
            Intent intent = new Intent(context, AddEditTaskActivity.class);
            intent.putExtra("taskList", taskList.get());
            startActivity(intent);
        }

    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        taskList.ifPresent(list -> savedInstanceState.putParcelable(KEY_TASK_LIST, list));

    }

    public void sortToAbc(){
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }
}
