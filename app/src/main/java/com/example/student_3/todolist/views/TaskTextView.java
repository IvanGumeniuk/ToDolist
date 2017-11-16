package com.example.student_3.todolist.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.example.student_3.todolist.R;

/**
 * Created by Student_3 on 16/11/2017.
 */

public class TaskTextView extends AppCompatTextView {

    private static final int[] STATE_MESSAGE_UNREAD = {R.attr.state_task_not_expired};

    private boolean taskNotExpired = true;

    public TaskTextView(Context context){
        super(context);
    };

    public TaskTextView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public TaskTextView(Context context, AttributeSet attributeSet, int style){
        super(context, attributeSet, style);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (taskNotExpired) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

            mergeDrawableStates(drawableState, STATE_MESSAGE_UNREAD);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    public void setNotExpired(boolean taskNotExpired) {
        this.taskNotExpired = taskNotExpired;
        refreshDrawableState();
    }
}
