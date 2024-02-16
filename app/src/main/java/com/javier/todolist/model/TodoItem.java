package com.javier.todolist.model;

import java.io.Serializable;
import java.util.Date;

public class TodoItem implements Serializable {
    private long id;
    private String title;
    private String description;
    private String category;
    private Date dueDate;
    private boolean completed;

    public TodoItem(){

    }

    public TodoItem(int id, String title, String description, String category, Date dueDate, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
