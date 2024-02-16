package com.javier.todolist.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.javier.todolist.EditTaskActivity;
import com.javier.todolist.R;
import com.javier.todolist.callback.IClickItemListener;
import com.javier.todolist.database.TaskDAO;
import com.javier.todolist.model.TodoItem;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private IClickItemListener iClickItemListener;
    private List<TodoItem> todoList;

    public TodoAdapter(List<TodoItem> todoList, IClickItemListener clickItemListener) {
        this.todoList = todoList;
        this.iClickItemListener = clickItemListener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem todoItem = todoList.get(position);
        holder.title.setText(todoItem.getTitle());
        holder.description.setText(todoItem.getDescription());
        holder.dueDate.setText("Due Date: " + todoItem.getDueDate().toString());

        // Set click listeners for buttons
        holder.editButton.setOnClickListener(v -> onEditButtonClick(position));
        holder.deleteButton.setOnClickListener(v -> onDeleteButtonClick(position));
    }

    private void onEditButtonClick(int position) {
        if (iClickItemListener != null) {
            iClickItemListener.onUpdateItem(todoList.get(position));
        }
    }

    private void onDeleteButtonClick(int position) {
        if (iClickItemListener != null) {
            iClickItemListener.onDeleteItem(position);
        }
    }

    @Override
    public int getItemCount() {
        if (todoList != null) {
            return todoList.size();
        }
        return 0;
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView dueDate;
        Button editButton;
        Button deleteButton;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            dueDate = itemView.findViewById(R.id.dueDateTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

}
