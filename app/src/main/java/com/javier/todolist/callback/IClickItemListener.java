package com.javier.todolist.callback;

import com.javier.todolist.model.TodoItem;

public interface IClickItemListener {
    void onUpdateItem(TodoItem item);
    void onDeleteItem(int position);
}
