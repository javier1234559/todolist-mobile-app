package com.javier.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.javier.todolist.callback.IUpdateListCallBack;
import com.javier.todolist.database.TaskDAO;
import com.javier.todolist.model.TodoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    private final String[] categories = {"Pending", "Done", "Overdue"};
    private TaskDAO taskDAO;
    private TodoItem editItem;
    private IUpdateListCallBack updateListCallBack;
    EditText titleText;
    EditText descriptionText;
    Spinner categoriesSpinner;
    Button buttonSelectDueDate;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        titleText = findViewById(R.id.edit_title);
        descriptionText = findViewById(R.id.edit_description);
        categoriesSpinner = findViewById(R.id.edit_spinner_category);
        buttonSelectDueDate = findViewById(R.id.edit_button_select_due_date);
        saveButton = findViewById(R.id.edit_button_save);

        setDataForSpinner();
        getDataFromIntent();
        addDateTimeDialogEvent();
        addSaveEvent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("update_item")) {
            editItem = (TodoItem) intent.getSerializableExtra("update_item");
            populateData();
        }
    }

    private void populateData() {
        titleText.setText(editItem.getTitle());
        descriptionText.setText(editItem.getDescription());
        setSeletedCategoryForSpinner(editItem.getCategory());
        Calendar calendar = Calendar.getInstance();
        if (editItem.getDueDate() != null) {
            calendar.setTime(editItem.getDueDate());
            // Extract components from the dueDate
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            // Pass components to handleDateTimeSelection
            handleDateTimeSelection(year, month, day, hour, minute);
        }
    }

    private void setSeletedCategoryForSpinner(String currentCategory) {
        int categoryPosition = Arrays.asList(categories).indexOf(currentCategory);
        categoriesSpinner.setSelection(categoryPosition);
    }

    private void addDateTimeDialogEvent() {
        buttonSelectDueDate.setOnClickListener(e -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            if (editItem.getDueDate() != null) {
                calendar.setTime(editItem.getDueDate());
            }

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
        Calendar calendar = Calendar.getInstance();
        if (editItem.getDueDate() != null) {
            calendar.setTime(editItem.getDueDate());
        }

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(calendar.getTime());

        TextView textDueDate = findViewById(R.id.text_due_date);
        textDueDate.setText("Selected Due Date: " + formattedDateTime);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
    }


}