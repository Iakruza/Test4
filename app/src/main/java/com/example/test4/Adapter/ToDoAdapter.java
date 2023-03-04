package com.example.test4.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test4.AddNewTask;
import com.example.test4.MainActivity;
import com.example.test4.Model.ToDoModel;
import com.example.test4.R;
import com.example.test4.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    // standard implementation of recycler view adaptor

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase(); // open database
        ToDoModel item = todoList.get(position); // in the todolist you get the item
        holder.task.setText(item.getTask()); // set the task from the item position
        holder.task.setChecked(toBoolean(item.getStatus())); // checks the status of the item if it is checked or not
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else{
                    db.updateStatus(item.getId(),0);
                }

            }
        });

    }

    //sets the getItem count, this will let it know how many items it needs to print

    public int getItemCount(){
        return todoList.size();
    }

    // sense the checkmark is boolean type, need to convert to boolean
    private boolean toBoolean(int n){
        return n!=0;
    }

    //for dummy data

    public void setTask(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged(); // so recycler view is updated
    }

    public Context getContext(){
        return activity;
    }

    // delete item from activity

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId()); // id of item being deleted
        todoList.remove(position); //remove from the item
        notifyItemRemoved(position); // notifies that the item will be removed and will automatically update view
    }

    //update edited items
    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    // for the checkbox in the mainactivity
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
