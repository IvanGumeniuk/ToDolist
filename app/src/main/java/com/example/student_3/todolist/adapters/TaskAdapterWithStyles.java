package com.example.student_3.todolist.adapters;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.R;
import com.example.student_3.todolist.activities.TaskActivity;
import com.example.student_3.todolist.listeners.OnTaskClickListener;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.views.TaskTextView;

import java.util.List;

/**
 * Created by Student_3 on 21/11/2017.
 */

public class TaskAdapterWithStyles extends RecyclerView.Adapter<TaskAdapterWithStyles.TaskViewHolder>{

    private final int CATEGORY_NAME_LENGTH_MAX = 10;

    private List<Task> tasks;
    private OnTaskClickListener onTaskClickListener;

    public TaskAdapterWithStyles(@NonNull List<Task> tasks, OnTaskClickListener onTaskClickListener){
        super();
        this.tasks = tasks;
        this.onTaskClickListener = onTaskClickListener;
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
        holder.name.setTransitionName(BundleKey.NAME_TRANSITION.name() + position);
        holder.description.setTransitionName(BundleKey.DESCRIPTION_TRANSITION.name() + position);
        holder.category.setTransitionName(BundleKey.CATEGORY_TRANSITION.name() + position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TaskTextView name;
        private TaskTextView description;
        private TextView category;
        private Task task;

        public TaskViewHolder(View itemView){
            super(itemView);
            name = (TaskTextView) itemView.findViewById(R.id.nameTextView);
            description = (TaskTextView) itemView.findViewById(R.id.descriptionTextView);
            category = (TextView) itemView.findViewById(R.id.categoryTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(Task task){
            this.task = task;
            name.setText(task.getName());
            description.setText(task.getDescription());
            if(task.getCategory().getName().length() > CATEGORY_NAME_LENGTH_MAX){
                category.setText(String.format("%s...",task.getCategory().getName().substring(0,
                        CATEGORY_NAME_LENGTH_MAX)));
            } else {
                category.setText(task.getCategory().getName());
            }
            category.setTextColor(task.getCategory().getColor());
            ((GradientDrawable)category.getBackground()).setStroke(8, task.getCategory().getColor());
        }

        @Override
        public void onClick(View view) {
            onTaskClickListener.onTaskClick(task, view);
        }
    }

    public void updateList(List<Task> tasks){
        this.tasks = tasks;
    }
}
