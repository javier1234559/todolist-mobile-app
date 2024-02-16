package com.javier.todolist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.javier.todolist.EditTaskActivity;
import com.javier.todolist.callback.IClickItemListener;
import com.javier.todolist.callback.IUpdateListCallBack;
import com.javier.todolist.R;
import com.javier.todolist.database.TaskDAO;
import com.javier.todolist.model.TodoItem;
import com.javier.todolist.adapter.TodoAdapter;

import java.util.List;
import java.util.Objects;


public class DoneFragment extends Fragment implements IUpdateListCallBack ,IClickItemListener {

    private TaskDAO taskDAO;
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;

    public DoneFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        //Get list task from database
        taskDAO = TaskDAO.getInstance(getContext());
        List<TodoItem> doneItems = taskDAO.getAllTasks();

        // Initialize RecycleView
        recyclerView = view.findViewById(R.id.recycler_view_done);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todoAdapter = new TodoAdapter(doneItems,this);
        recyclerView.setAdapter(todoAdapter);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        taskDAO.close();
    }

    @Override
    public void onNewTaskCreated(int pos) {
        todoAdapter.notifyItemInserted(pos);
    }

    @Override
    public void onUpdateItem(TodoItem item) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("update_item",item);
        intent.putExtras(bundle);
        requireContext().startActivity(intent);
    }

    @Override
    public void onDeleteItem(int position) {
        long id = todoAdapter.getItemId(position);
        taskDAO.delete(id);
        todoAdapter.notifyItemRemoved(position);
    }
}