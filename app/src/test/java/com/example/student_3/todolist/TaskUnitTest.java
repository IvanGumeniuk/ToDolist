package com.example.student_3.todolist;

import com.example.student_3.todolist.homework.MyLinkedList;
import com.example.student_3.todolist.homework.PreMax;
import com.example.student_3.todolist.validators.Validator;

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

    @Test
    public void checkNumberValidator() throws Exception {
        Validator<Integer> integerValidator = new Validator.NumberValidatorBuilder<Integer>()
                .setMinNumber(5)
                .build();
        assertEquals(true,integerValidator.validate(10));
        assertEquals(false,integerValidator.validate(3));
        assertEquals("must be bigger than 5", integerValidator.getLastMessage());
        assertEquals(true,integerValidator.validate(5));
        Validator<Double> doubleValidator = new Validator.NumberValidatorBuilder<Double, Double>()
                .setMaxNumber(25.5523)
                .setMinNumber(10.034)
                .build();
        assertEquals(true, doubleValidator.validate(17.54));
        assertEquals(false, doubleValidator.validate(25.55231));
        assertEquals("must be smaller than 25.5523", doubleValidator.getLastMessage());
        assertEquals(false, doubleValidator.validate(10.033));
        Validator<Float> floatValidator = new Validator.NumberValidatorBuilder<Float, Float>()
                .setMinNumber((float)3.0)
                .setMaxNumber((float)15.4)
                .setRange((float)4, (float) 14)
                .build();
        assertEquals(true, floatValidator.validate((float)6));
        assertEquals(false, floatValidator.validate((float)2.3));
        assertEquals("must be in range [4.0, 14.0]", floatValidator.getLastMessage());
        Validator<Byte> byteValidator = new Validator.NumberValidatorBuilder<Byte, Byte>()
                .setMinNumber((byte)10)
                .setMaxNumber((byte)2)
                .build();
        assertEquals(false, byteValidator.validate((byte) 2));
        assertEquals(false, byteValidator.validate((byte) 7));
        assertEquals("must be exactly 10", byteValidator.getLastMessage());

    }

}
