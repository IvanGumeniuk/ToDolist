package com.example.student_3.todolist;

import com.example.student_3.todolist.homework.MyLinkedList;
import com.example.student_3.todolist.homework.PreMax;

import org.junit.Test;

import java.util.Calendar;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

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

    @Test
    public void checkPreMax() throws Exception {
        assertNull(PreMax.findPreMax(new int[]{5,5,5,5,5}));
        assertNull(PreMax.findPreMax(new int[]{1}));
        assertNull(PreMax.findPreMax(new int[10]));
        assertEquals(5, (int)PreMax.findPreMax(new int[]{5,6,5,5,5}));
        assertEquals(-2, (int)PreMax.findPreMax(new int[]{-1,-2,-3,-5,-6}));
        assertEquals(-3, (int)PreMax.findPreMax(new int[]{-6,-5,-4,-3,-2}));
        assertEquals(1, (int)PreMax.findPreMax(new int[]{1, 5, 5}));
        assertEquals(1, (int)PreMax.findPreMax(new int[]{5, 5, 1}));
        assertEquals(2, (int)PreMax.findPreMax(new int[]{1, 2, 3}));
        assertEquals(2, (int)PreMax.findPreMax(new int[]{3, 2, 1}));
        assertEquals(2, (int)PreMax.findPreMax(new int[]{-6,-5,-4, 3, 2}));
    }

    @Test
    public void checkMyLinkedList() throws Exception {
        MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();
        myLinkedList.add(5);
        myLinkedList.add(5);
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0; i < 50; i++){
            myLinkedList.add(i * random.nextInt(10));
        }
        myLinkedList.print();
        System.out.print("\nfoofoofoo\nfoofoofoo\n\n");
        myLinkedList.print();
        System.out.print("\n\n\n\n\n");
        for(int i = 0; i < 50; i++){
            myLinkedList.add(i * random.nextInt(10));
        }
        myLinkedList.print();
    }

}
