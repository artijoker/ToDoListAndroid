package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.entities.TaskList;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Optional;

public class AddEditTaskListActivity extends AppCompatActivity {

    private final static String KEY_EDIT_TEXT = "keyEditText";
    private final static String KEY_BUTTON_DELETE = "keyButtonDelete";
    private final static String KEY_TEXT_VIEW = "keyTextView";
    private final static String KEY_TASK_LIST = "keyTaskList";

    private DatabaseAdapter dbAdapter;
    private Optional<TaskList> list;

    private EditText editText;
    private ExtendedFloatingActionButton buttonDelete;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task_list);

        dbAdapter = new DatabaseAdapter(this);
        editText = findViewById(R.id.editText);

        textView = findViewById(R.id.textView);
        buttonDelete = findViewById(R.id.button_deleteList);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            list = Optional.of(extras.getParcelable("taskList"));
            editText.setText(list.get().getName());
            textView.setText("Редактировние списка");

        } else {
            list = Optional.empty();
            textView.setText("Создание списка");
            buttonDelete.setVisibility(View.GONE);
        }
    }

    public void save(View view) {
        String name = editText.getText().toString();
        if (name.trim().isEmpty()) {
            Toast toast = Toast.makeText(
                    this,
                    "Введите название списка",
                    Toast.LENGTH_SHORT
            );
            toast.show();
            return;
        }


        dbAdapter.open();
        if (list.isPresent()) {
            list.get().setName(name);
            dbAdapter.updateTaskList(list.get());
        } else {
            dbAdapter.addTaskList(new TaskList(name));
        }
        dbAdapter.close();
        goBack();
    }

    public void delete(View view) {
        if (list.isPresent()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Удалить этот список?")
                    .setMessage("Все задачи в этом списке будут удалены без возможности востановления")
                    .setNegativeButton("Отмена", (dialog, which) -> {
                        dialog.cancel();
                    })
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        dbAdapter.open();
                        dbAdapter.deleteTaskList(list.get().getId());
                        dbAdapter.close();
                        goBack();
                    })
                    .show();
        }
    }

    public void back(View view) {
        goBack();
    }

    private void goBack() {
        finish();
    }


    @Override

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(KEY_TEXT_VIEW, textView.getText().toString());
        savedInstanceState.putString(KEY_EDIT_TEXT, editText.getText().toString());
        savedInstanceState.putInt(KEY_BUTTON_DELETE, buttonDelete.getVisibility());
        list.ifPresent(taskList -> savedInstanceState.putParcelable(KEY_TASK_LIST, taskList));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        textView.setText(savedInstanceState.getString(KEY_TEXT_VIEW));
        editText.setText(savedInstanceState.getString(KEY_EDIT_TEXT));
        buttonDelete.setVisibility(savedInstanceState.getInt(KEY_BUTTON_DELETE));

        TaskList possiblyTaskList = savedInstanceState.getParcelable(KEY_TASK_LIST);
        list = possiblyTaskList != null ? Optional.of(possiblyTaskList) : Optional.empty();

    }
}