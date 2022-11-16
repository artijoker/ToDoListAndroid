package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.TaskList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

public class AddEditTaskActivity extends AppCompatActivity {

    private final static String KEY_BUTTON_ADD_END_DATE = "keyButtonAddEndDate";
    private final static String KEY_BUTTON_CHANGE_DATE = "keyButtonChangeDate";
    private final static String KEY_BUTTON_DELETE_DATE = "keyButtonDeleteDate";
    private final static String KEY_BUTTON_DELETE_TASK = "keyButtonDeleteTask";
    private final static String KEY_BUTTON_SAVE_TASK = "keyButtonSaveTask";
    private final static String KEY_GRID_LAYOUT_STATUS_TASK = "keyGridLayoutStatusTask";
    private final static String KEY_CHECK_BOX_COMPLETED_TASK = "keyCheckBoxCompletedTask";
    private final static String KEY_CHECK_BOX_TAGGED_TASK = "keyCheckBoxTaggedTask";
    private final static String KEY_EDIT_TEXT_TASK_TITLE = "keyEditTextTaskTitle";
    private final static String KEY_EDIT_TEXT_TASK_TEXT = "keyEditTextTaskText";
    private final static String KEY_TEXT_VIEW_NEW_TASK = "keyTextViewNewTask";
    private final static String KEY_END_DATE = "keyEndDate";
    private final static String KEY_TASK_LIST = "keyTaskList";
    private final static String KEY_TASK = "keyTask";

    private ExtendedFloatingActionButton button_addEndDate;
    private ExtendedFloatingActionButton button_changeDate;
    private MaterialButton button_deleteDate;

    private ExtendedFloatingActionButton button_deleteTask;
    private Button button_saveTask;

    private GridLayout gridLayout_statusTask;
    private CheckBox checkBox_completedTask;
    private CheckBox checkBox_taggedTask;

    private EditText editText_taskTitle;
    private EditText editText_taskText;

    private TextView textView_newTask;

    private Optional<LocalDate> endDate = Optional.empty();

    private Optional<TaskList> list;
    private Optional<Task> task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        button_addEndDate = findViewById(R.id.button_addEndDate);
        button_changeDate = findViewById(R.id.button_changeDate);
        button_deleteDate = findViewById(R.id.button_deleteDate);
        button_deleteTask = findViewById(R.id.button_deleteTask);
        button_saveTask = findViewById(R.id.button_saveTask);

        gridLayout_statusTask = findViewById(R.id.gridLayout_statusTask);
        checkBox_completedTask = findViewById(R.id.checkBox_completedTask);
        checkBox_taggedTask = findViewById(R.id.checkBox_taggedTask);
        editText_taskTitle = findViewById(R.id.editText_taskTitle);
        editText_taskText = findViewById(R.id.editText_taskText);

        textView_newTask = findViewById(R.id.textView_newTask);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TaskList possiblyTaskList = extras.getParcelable("taskList");

            list = possiblyTaskList != null ? Optional.of(possiblyTaskList) : Optional.empty();

            Task possiblyTask = extras.getParcelable("task");
            if (possiblyTask != null) {
                task = Optional.of(extras.getParcelable("task"));

                editText_taskTitle.setText(task.get().getTitle());

                if (task.get().getText().isPresent())
                    editText_taskText.setText(task.get().getText().get());

                checkBox_completedTask.setChecked(task.get().isCompleted());
                checkBox_taggedTask.setChecked(task.get().isTagged());
                button_saveTask.setVisibility(View.GONE);

                if (task.get().getEndDate().isPresent()) {
                    endDate = task.get().getEndDate();
                    button_changeDate.setVisibility(View.VISIBLE);
                    button_deleteDate.setVisibility(View.VISIBLE);
                    button_addEndDate.setVisibility(View.GONE);
                    button_changeDate.setText(
                            task.get().getEndDate().get().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    );
                }

            } else {
                task = Optional.empty();
                gridLayout_statusTask.setVisibility(View.GONE);
                button_deleteTask.setVisibility(View.GONE);
                textView_newTask.setVisibility(View.VISIBLE);
            }

        }
        button_saveTask.setOnClickListener(v -> saveNewTask());
    }

    public void addEndDate(View view) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {

                    button_changeDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                    button_addEndDate.setVisibility(View.GONE);
                    button_changeDate.setVisibility(View.VISIBLE);
                    button_deleteDate.setVisibility(View.VISIBLE);
                    endDate = Optional.of(LocalDate.of(year, monthOfYear + 1, dayOfMonth));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void changeDate(View view) {
        DatePickerDialog datePickerDialog;
        if (endDate.isPresent()) {
            datePickerDialog = new DatePickerDialog(this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        button_changeDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        endDate = Optional.of(LocalDate.of(year, monthOfYear + 1, dayOfMonth));
                    },
                    endDate.get().getYear(),
                    endDate.get().getMonthValue() - 1,
                    endDate.get().getDayOfMonth()
            );
        } else {
            Calendar calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        button_changeDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        endDate = Optional.of(LocalDate.of(year, monthOfYear + 1, dayOfMonth));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        }

        datePickerDialog.show();
    }

    public void deleteDate(View view) {
        endDate = Optional.empty();
        button_addEndDate.setVisibility(View.VISIBLE);
        button_changeDate.setVisibility(View.GONE);
        button_deleteDate.setVisibility(View.GONE);
    }


    private void saveNewTask() {
        String title = editText_taskTitle.getText().toString();
        if (title.trim().isEmpty()) {
            Toast toast = Toast.makeText(
                    this,
                    "Задача не указана",
                    Toast.LENGTH_SHORT
            );
            toast.show();
            return;
        }

        String text = editText_taskText.getText().toString().trim();
        if (list.isPresent()) {
            Task task = new Task(list.get().getId(), title,
                    text.isEmpty() ? Optional.empty() : Optional.of(text),
                    endDate,
                    Optional.empty(),
                    false,
                    false
            );
            DatabaseAdapter dbAdapter =new DatabaseAdapter(this);
            dbAdapter.open();
            dbAdapter.addTask(task);
            dbAdapter.close();
        }

        goBack();
    }

    public void back(View view) {
        String title = editText_taskTitle.getText().toString().trim();
        String text = editText_taskText.getText().toString().trim();
        if (task.isPresent()) {
            DatabaseAdapter dbAdapter =new DatabaseAdapter(this);
            dbAdapter.open();

            Task currentTask = task.get();
            if (!title.isEmpty())
                currentTask.setTitle(title);
            if (!text.isEmpty())
                currentTask.setText(Optional.of(text));

            currentTask.setEndDate(endDate);
            currentTask.setTagged(checkBox_taggedTask.isChecked());
            currentTask.setCompleted(checkBox_completedTask.isChecked());
            dbAdapter.updateTask(task.get());

            dbAdapter.close();
        }

        goBack();
    }

    public void deleteTask(View view) {
        if (task.isPresent()) {

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Удалить")
                    .setMessage("Удалить эту задачу?")
                    .setNegativeButton("Отмена", (dialog, which) -> {
                        dialog.cancel();
                    })
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        DatabaseAdapter dbAdapter =new DatabaseAdapter(this);
                        dbAdapter.open();
                        dbAdapter.deleteTask(task.get().getId());
                        dbAdapter.close();
                        goBack();
                    })
                    .show();
        }

    }

    private void goBack() {
        finish();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(KEY_BUTTON_ADD_END_DATE, button_addEndDate.getVisibility());
        savedInstanceState.putInt(KEY_BUTTON_CHANGE_DATE, button_changeDate.getVisibility());
        savedInstanceState.putInt(KEY_BUTTON_DELETE_DATE, button_deleteDate.getVisibility());
        int v = button_deleteTask.getVisibility();
        savedInstanceState.putInt(KEY_BUTTON_DELETE_TASK, v);
        savedInstanceState.putInt(KEY_BUTTON_SAVE_TASK, button_saveTask.getVisibility());
        savedInstanceState.putInt(KEY_GRID_LAYOUT_STATUS_TASK, gridLayout_statusTask.getVisibility());

        savedInstanceState.putBoolean(KEY_CHECK_BOX_COMPLETED_TASK, checkBox_completedTask.isChecked());
        savedInstanceState.putBoolean(KEY_CHECK_BOX_TAGGED_TASK, checkBox_taggedTask.isChecked());

        savedInstanceState.putString(KEY_EDIT_TEXT_TASK_TITLE, editText_taskTitle.getText().toString());
        savedInstanceState.putString(KEY_EDIT_TEXT_TASK_TEXT, editText_taskText.getText().toString());

        savedInstanceState.putInt(KEY_TEXT_VIEW_NEW_TASK, textView_newTask.getVisibility());

        endDate.ifPresent(localDate -> savedInstanceState.putString(KEY_END_DATE, localDate.toString()));

        list.ifPresent(taskList -> savedInstanceState.putParcelable(KEY_TASK_LIST, taskList));

        task.ifPresent(task -> savedInstanceState.putParcelable(KEY_TASK, task));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        button_addEndDate.setVisibility(savedInstanceState.getInt(KEY_BUTTON_ADD_END_DATE));
        button_changeDate.setVisibility(savedInstanceState.getInt(KEY_BUTTON_CHANGE_DATE));
        button_deleteDate.setVisibility(savedInstanceState.getInt(KEY_BUTTON_DELETE_DATE));
        int v = savedInstanceState.getInt(KEY_BUTTON_DELETE_TASK);
        button_deleteTask.setVisibility(v);
        button_saveTask.setVisibility(savedInstanceState.getInt(KEY_BUTTON_SAVE_TASK));
        gridLayout_statusTask.setVisibility(savedInstanceState.getInt(KEY_GRID_LAYOUT_STATUS_TASK));

        checkBox_completedTask.setChecked(savedInstanceState.getBoolean(KEY_CHECK_BOX_COMPLETED_TASK));
        checkBox_taggedTask.setChecked(savedInstanceState.getBoolean(KEY_CHECK_BOX_TAGGED_TASK));

        editText_taskTitle.setText(savedInstanceState.getString(KEY_EDIT_TEXT_TASK_TITLE));
        editText_taskText.setText(savedInstanceState.getString(KEY_EDIT_TEXT_TASK_TEXT));

        textView_newTask.setVisibility(savedInstanceState.getInt(KEY_TEXT_VIEW_NEW_TASK));

        String possiblyEndDate = savedInstanceState.getString(KEY_END_DATE);
        endDate = possiblyEndDate != null ? Optional.of(LocalDate.parse(possiblyEndDate)): Optional.empty();

        TaskList possiblyTaskList = savedInstanceState.getParcelable(KEY_TASK_LIST);
        list = possiblyTaskList != null ? Optional.of(possiblyTaskList): Optional.empty();

        Task possiblyTask = savedInstanceState.getParcelable(KEY_TASK);
        task = possiblyTask != null ? Optional.of(possiblyTask): Optional.empty();
    }
}