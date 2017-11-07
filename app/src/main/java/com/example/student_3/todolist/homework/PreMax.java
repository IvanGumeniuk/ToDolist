package com.example.student_3.todolist.homework;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gromi on 11/6/2017.
 */

public class PreMax {

    public static Integer findPreMax(int[] array){
        boolean resultNonNull = false;
        int preMax = 0;
        if(array.length > 1){
            int iterator = 1;
            int max = array[0];
            while (!resultNonNull && iterator < array.length){
                if(max < array[iterator]){
                    preMax = max;
                    max = array[iterator];
                    resultNonNull = true;
                } else if (max > array[iterator]){
                    preMax = array[iterator];
                    resultNonNull = true;
                }
                iterator++;
            }
            while (iterator < array.length) {
                if (max < array[iterator]) {
                    preMax = max;
                    max = array[iterator];
                } else if (preMax < array[iterator] && max != array[iterator]) {
                    preMax = array[iterator];
                }
                iterator++;
            }
        }
        return resultNonNull ? preMax : null;
    }


    public static <T extends Number> T findPreMax(List<T> list){
        T premax = null;
        if(list.size() > 1){
            Iterator<T> iterator = list.iterator();
            T max = iterator.next();
            T temp;
            while (premax == null && iterator.hasNext()){
                temp = iterator.next();
                if(max.doubleValue() < temp.doubleValue()){
                    premax = max;
                    max = temp;
                } else if(max.doubleValue() > temp.doubleValue()){
                    premax = temp;
                }
            }
            while (iterator.hasNext()) {
                temp = iterator.next();
                if (max.doubleValue() < temp.doubleValue()) {
                    premax = max;
                    max = temp;
                } else if (premax.doubleValue() < temp.doubleValue() && max.doubleValue() != temp.doubleValue()) {
                    premax = temp;
                }
            }
        }
        return premax;
    }

}
