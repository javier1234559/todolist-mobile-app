package com.javier.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.javier.todolist.callback.IUpdateListCallBack;
import com.javier.todolist.database.TaskDAO;
import com.javier.todolist.model.TodoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTaskActivity extends AppCompatActivity {

    private TaskDAO taskDAO;
    private IUpdateListCallBack updateListCallBack;
    EditText titleText;
    EditText descriptionText;
    Spinner categoriesSpinner;
    Button buttonSelectDueDate;
    Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titleText = findViewById(R.id.input_title);
        descriptionText = findViewById(R.id.input_description);
        categoriesSpinner = findViewById(R.id.spinner_category);
        buttonSelectDueDate = findViewById(R.id.button_select_due_date);
        saveButton = findViewById(R.id.button_save);

        setDataForSpinner();
        addDateTimeDialogEvent();
        addSaveEvent();
    }

    private void addDateTimeDialogEvent() {
        buttonSelectDueDate.setOnClickListener(e -> {
            Calendar calendar = Calendar.getInstance(); // get current time for initial
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (datePicker, selectedYear, selectedMonth, selectedDay) -> showTimePickerDialog(selectedYear, selectedMonth, selectedDay),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });
    }

    private void showTimePickerDialog(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(); // get current time for initial
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (timePicker, hour, minute) -> handleDateTimeSelection(year, month, day, hour, minute),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    private void handleDateTimeSelection(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        TextView textDueDate = findViewById(R.id.text_due_date);
        textDueDate.setText("Selected Due Date: " + calendar);
        textDueDate.setVisibility(View.VISIBLE);
    }


    private void addSaveEvent() {
        taskDAO = TaskDAO.getInstance(getBaseContext());
        saveButton.setOnClickListener(v -> {
            String title = titleText.getText().toString();
            String description = descriptionText.getText().toString();
            String category = categoriesSpinner.getSelectedItem().toString();
            TextView textDueDate = findViewById(R.id.text_due_date);
            String dueDate = textDueDate.getText().toString().replace("Selected Due Date: ", "");

            TodoItem todoItem = new TodoItem();
            todoItem.setTitle(title);
            todoItem.setDescription(description);
            todoItem.setCategory(category);


            // Parse the due date string to a Date object and set it in the TodoItem
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            try {
                Date parsedDueDate = dateFormat.parse(dueDate);
                todoItem.setDueDate(parsedDueDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int newPostion = (int) taskDAO.createTask(todoItem);
            updateListCallBack.onNewTaskCreated(newPostion);
            Toast.makeText(this, "The new task is created", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setDataForSpinner() {
        String[] categories = {"Pedding", "Done", "Overdue"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
    }


}