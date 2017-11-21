package com.example.student_3.todolist.adapters;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student_3.todolist.R;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.views.TaskTextView;

import java.util.List;

/**
 * Created by Student_3 on 21/11/2017.
 */

public class TaskAdapterWithStyles extends RecyclerView.Adapter<TaskAdapterWithStyles.TaskViewHolder>{

    private List<Task> tasks;

    public TaskAdapterWithStyles(@NonNull List<Task> tasks){
        super();
        this.tasks = tasks;
    }


    @Override
    public TaskAdapterWithStyles.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(new ContextThemeWrapper(parent.getContext(), viewType))
                .inflate(R.layout.item_task, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return tasks.get(position).isExpired() ? R.style.Item_Expired : R.style.Item;
    }

    @Override
    public void onBindViewHolder(TaskAdapterWithStyles.TaskViewHolder holder, int position) {
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
            name = (TaskTextView) itemView.findViewById(R.id.nameTextView);
            description = (TaskTextView) itemView.findViewById(R.id.descriptionTextView);
        }

        public void bind(Task task){
            name.setText(task.getName());
            description.setText(task.getDescription());
        }
    }
}
