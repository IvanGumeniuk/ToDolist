package com.example.student_3.todolist.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student_3.todolist.R;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.validators.Validator;
import com.example.student_3.todolist.views.TaskTextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Student_3 on 14/11/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private Validator<Date> dateValidator;

    public TaskAdapter(@NonNull List<Task> tasks){
        super();
        this.tasks = tasks;
        dateValidator = new Validator.DateValidatorBuilder()
                .setNotExpiredRule()
                .build();
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        TaskTextView name;
        TaskTextView description;

        public TaskViewHolder(View itemView){
            super(itemView);
            name = (TaskTextView) itemView.findViewById(R.id.toolbar);
            description = (TaskTextView) itemView.findViewById(R.id.descriptionTextView);
        }

        public void bind(Task task){
            name.setText(task.getName());
            description.setText(task.getDescription());
            if(!dateValidator.validate(task.getExpireDate())){
                name.setNotExpired(false);
                description.setNotExpired(false);
            }
        }
    }
}
