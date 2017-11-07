package com.example.student_3.todolist.homework;

import android.support.annotation.NonNull;

/**
 * Created by gromi on 11/6/2017.
 */

public final class Element<T> {
    private final T value;
    private Element next;

    public Element(@NonNull T value){
        this.value =  value;
    }

    public T getValue(){
        return value;
    }

    public Element getNext() {
        return next;
    }

    public void setNext(@NonNull Element next) {
        if(hasNext()){
            throw new RuntimeException("Can't change initialized parameter 'next'");
        }else {
            this.next = next;
        }
    }

    public boolean hasNext(){
        return next!=null;
    }
}

