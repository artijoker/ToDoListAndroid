package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.entities.TaskList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.Optional;

public class AddEditTaskListActivity extends AppCompatActivity {

    private final ObjectMapper mapper = new ObjectMapper();

    private DatabaseAdapter dbAdapter;
    private Optional<TaskList>  list;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task_list);

        dbAdapter = new DatabaseAdapter(this);
        editText = findViewById(R.id.editText);

        TextView textView = findViewById(R.id.textView);
        ExtendedFloatingActionButton buttonDelete = findViewById(R.id.buttonDelete);

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
        goMainActivity();
    }

    public void delete(View view) {
        if (list.isPresent()){
            dbAdapter.open();
            dbAdapter.deleteTaskList(list.get().getId());
            dbAdapter.close();
            goMainActivity();
        }
    }

    public void back(View view) {
        goMainActivity();
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}