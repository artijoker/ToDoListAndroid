package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.TaskList;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {
    private Context context;
    private final List<Task> tasks = new ArrayList<>();
    private final TaskList taskList;

    private ListView listView;
    private ArrayAdapter<Task> adapter;

    public TasksFragment(TaskList taskList){
        this.taskList = taskList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        context = getActivity();
//        View view = inflater.inflate(R.layout.tasks_fragment_page, container, false);
//        TextView title = view.findViewById(R.id.title);
//        title.setText(taskList.getName());
//        listView = (ListView) view.findViewById(R.id.taskList);
//        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, tasks);
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
//        dbAdapter.open();
//
//        tasks.addAll(dbAdapter.getTasksByListId(taskList.getId()));
//        dbAdapter.close();
//    }
}
