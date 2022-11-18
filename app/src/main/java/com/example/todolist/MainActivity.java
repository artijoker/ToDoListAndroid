package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.todolist.entities.TaskList;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    private final static String KEY_CURRENT_TAB_POSITION = "keyTabPosition";
    private final static String KEY_COUNT_PAGES = "keyCountPages";

    private int tabPosition = 0;
    private int currentCountPages = 0;
    private ViewPager2 pager;
    private TabLayout tabLayout;

    private List<UpdateFragment> updateFragments = new ArrayList<>();

    private DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        dbAdapter = new DatabaseAdapter(this);
        MaterialToolbar topAppBar = findViewById(R.id.menu);

        topAppBar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.add) {
                Intent intent = new Intent(getApplicationContext(), AddEditTaskListActivity.class);
                startActivity(intent);

                return true;

            } else if (id == R.id.rename) {
                Intent intent = new Intent(getApplicationContext(), AddEditTaskListActivity.class);
                TaskList list = (TaskList) Objects.requireNonNull(
                        tabLayout.getTabAt(tabLayout.getSelectedTabPosition())
                ).getTag();
                assert list != null;
                if (list.getId() == 1) {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Ошибка!")
                            .setMessage("Нельзя преименовать список по умолчанию")
                            .setPositiveButton("Ok", null)
                            .show();
                } else {
                    intent.putExtra("taskList", list);
                    startActivity(intent);
                }
                return true;
            } else if (id == R.id.delete) {
                TaskList list = (TaskList) Objects.requireNonNull(
                        tabLayout.getTabAt(tabLayout.getSelectedTabPosition())
                ).getTag();
                assert list != null;
                if (list.getId() == 1) {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Ошибка!")
                            .setMessage("Нельзя удалить список по умолчанию")
                            .setPositiveButton("Ok", null)
                            .show();
                } else {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Удалить этот список?")
                            .setMessage("Все задачи в этом списке будут удалены без возможности востановления")
                            .setNegativeButton("Отмена", (dialog, which) -> {
                                dialog.cancel();
                            })
                            .setPositiveButton("Удалить", (dialog, which) -> {
                                dbAdapter.open();
                                dbAdapter.deleteTaskList(list.getId());
                                dbAdapter.close();
                                updateViewPager();
                            })
                            .show();

                }

                return true;
            }
            else if (id == R.id.favorites) {

                Intent intent = new Intent(getApplicationContext(), FavoriteTasksActivity.class);
                startActivity(intent);
            }

            return false;
        });

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabPosition = position;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewPager();
    }

    private List<Pair<Fragment, TaskList>> createFragments(List<TaskList> lists) {

        List<Pair<Fragment, TaskList>> fragments = new ArrayList<>();

        for (TaskList list : lists) {

            Bundle args = new Bundle();
            args.putParcelable("taskList", list);
            TasksFragment fragment = new TasksFragment();
            fragment.setArguments(args);
            fragments.add(new Pair<>(fragment, list));
            updateFragments.add(fragment);
        }
        return fragments;
    }

    private void updateViewPager() {
        dbAdapter.open();
        List<Pair<Fragment, TaskList>> fragments = createFragments(dbAdapter.getTaskLists());
        dbAdapter.close();

        FragmentStateAdapter adapter = new FragmentAdapter(
                this,
                fragments.stream().map(p -> p.first).collect(Collectors.toList())
        );
        pager.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout,
                pager,
                (tab, position) -> {
                    tab.setText(fragments.get(position).second.getName());
                    tab.setTag(fragments.get(position).second);
                });
        tabLayoutMediator.attach();
//        currentCountPages = Objects.requireNonNull(pager.getAdapter()).getItemCount();
//        if (adapter.getItemCount() != currentCountPages) {
//            currentCountPages = adapter.getItemCount();
//            tabPosition = currentCountPages - 1;
//        }

        tabLayout.selectTab(tabLayout.getTabAt(tabPosition));
    }

    public void addList(View view) {
        Intent intent = new Intent(getApplicationContext(), AddEditTaskListActivity.class);
        startActivity(intent);
    }

    public void favoriteTasks(View view) {
        Intent intent = new Intent(getApplicationContext(), FavoriteTasksActivity.class);
        startActivity(intent);
    }

    @Override

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(KEY_CURRENT_TAB_POSITION, tabPosition);
        savedInstanceState.putInt(KEY_COUNT_PAGES, currentCountPages);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        tabPosition = savedInstanceState.getInt(KEY_CURRENT_TAB_POSITION);
        currentCountPages = savedInstanceState.getInt(KEY_COUNT_PAGES);
        updateViewPager();
    }
}