package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.TaskList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

public class AddEditTaskActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton button_addEndDate;
    private ExtendedFloatingActionButton button_date;
    private MaterialButton button_deleteDate;
    private ExtendedFloatingActionButton button_delete;
    private Button button_save;
    private TextView textView_listName;

    private GridLayout gridLayout_statusTask;
    private CheckBox checkBox_completed;
    private CheckBox checkBox_tagged;

    private EditText editText_taskTitle;

    private String title;
    private String text;
    private LocalDate endDate;

    private TaskList list;
    private Optional<Task>  task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        button_addEndDate = findViewById(R.id.button_addEndDate);
        button_date = findViewById(R.id.button_date);
        button_deleteDate = findViewById(R.id.button_deleteDate);



    }

    public void addEndDate(View view) {
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {

                    button_date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                    button_addEndDate.setVisibility(View.GONE);
                    button_date.setVisibility(View.VISIBLE);
                    button_deleteDate.setVisibility(View.VISIBLE);
                    endDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void changeDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    button_date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                    endDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                },
                endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth()
        );
        datePickerDialog.show();
    }

    public void deleteDate(View view) {
        endDate = null;
        button_addEndDate.setVisibility(View.VISIBLE);
        button_date.setVisibility(View.GONE);
        button_deleteDate.setVisibility(View.GONE);
    }


}