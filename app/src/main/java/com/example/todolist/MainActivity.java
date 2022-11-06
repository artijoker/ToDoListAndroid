package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Pair;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.TaskList;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private List<Pair<Fragment, String>> fragments;
    private List<TaskList> lists;

    private  ViewPager2 pager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        pager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
//        dbAdapter.open();
//        lists = dbAdapter.getTaskLists();
//        fragments = createFragments(lists);
//        dbAdapter.close();
//
//        FragmentStateAdapter adapter = new StateAdapter(
//                this,
//                fragments.stream().map(p -> p.first).collect(Collectors.toList())
//        );
//        pager.setAdapter(adapter);
//
//        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout,
//                pager,
//                new TabLayoutMediator.TabConfigurationStrategy() {
//                    @Override
//                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                        tab.setText(fragments.get(position).second);
//                    }
//                });
//        tabLayoutMediator.attach();
//    }
//
//    private List<Pair<Fragment, String>> createFragments(List<TaskList> lists){
//        List<Pair<Fragment, String>> fragments = new ArrayList<>();
//        for (TaskList list: lists) {
//            fragments.add(new Pair<>(new TasksFragment(list), list.getName()));
//        }
//        return fragments;
//    }
}