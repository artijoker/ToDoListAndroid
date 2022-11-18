package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.todolist.entities.Task;
import com.example.todolist.observer.TaskObserver;

import java.util.ArrayList;
import java.util.List;

public class FavoriteTasksActivity extends AppCompatActivity implements TaskObserver {

    private DatabaseAdapter dbAdapter;
    private List<Task> tasks = new ArrayList<>();

    private RecyclerView recyclerView_FavoriteTasks;
    private LinearLayout linearLayout_noFavoriteTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_tasks);
        recyclerView_FavoriteTasks = findViewById(R.id.recyclerView_FavoriteTasks);
        linearLayout_noFavoriteTasks = findViewById(R.id.linearLayout_noFavoriteTasks);
        dbAdapter = new DatabaseAdapter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dbAdapter.open();
        tasks = dbAdapter.getTaggedTasks();
        dbAdapter.close();


        for (Task task: tasks) {
            task.addObserver(this);
        }
        updateRecyclerView();
    }

    void updateRecyclerView(){
        if (tasks.size() > 0){
            recyclerView_FavoriteTasks.setVisibility(View.VISIBLE);
            linearLayout_noFavoriteTasks.setVisibility(View.GONE);
        }
        else {
            recyclerView_FavoriteTasks.setVisibility(View.GONE);
            linearLayout_noFavoriteTasks.setVisibility(View.VISIBLE);
        }

        recyclerView_FavoriteTasks.setAdapter(new RecyclerViewStateAdapter(this, tasks));
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    public void update(Task task) {
        if (!task.isTagged()){
            tasks.remove(task);
            updateRecyclerView();
        }
    }
}