package com.example.student_3.todolist;

import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Student_3 on 02/11/2017.
 */

public class TaskUnitTest {

    @Test
    public void checkInheritance() throws Exception {
        Task task = new Task();
        assertEquals(true, task.getStatus() == TaskObject.TaskStatus.NEW);
        assertEquals(false, task.isDone());
        assertEquals(true, task.isExpired());
    }

    @Test
    public void checkTimeSimple() throws Exception {
        Task task = new Task();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        calendar.add(Calendar.SECOND, 1);
        task.setExpireDate(calendar.getTime());
        assertEquals("7 days", task.getLeftTime());
    }

    @Test
    public void checkTimeExtends() throws Exception {
        Task task = new Task();
        Calendar calendar = Calendar.getInstance();

        task.setExpireDate(calendar.getTime());
        assertEquals("expired", task.getLeftTime());

        calendar.add(Calendar.MINUTE, 2);
        task.setExpireDate(calendar.getTime());
        assertEquals("1 minute", task.getLeftTime());

        calendar.add(Calendar.HOUR, 3);
        task.setExpireDate(calendar.getTime());
        assertEquals("3 hours", task.getLeftTime());

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        task.setExpireDate(calendar.getTime());
        assertEquals("1 day", task.getLeftTime());

        calendar.add(Calendar.DAY_OF_MONTH, 9);
        task.setExpireDate(calendar.getTime());
        assertEquals("10 days", task.getLeftTime());
    }
}
