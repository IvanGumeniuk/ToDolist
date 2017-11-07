package com.example.student_3.todolist.homework;

/**
 * Created by gromi on 11/6/2017.
 */

public class MyLinkedList<T> {
    private Element<T> firstElement = null;
    private Element<T> lastElement = null;
    private Element<T> printingElement = null;

    public void add(T value){
        if(lastElement == null){
            firstElement = new Element<>(value);
            lastElement = firstElement;
            printingElement = firstElement;
        } else {
            Element<T> newElement = new Element<>(value);
            lastElement.setNext(newElement);
            lastElement = newElement;
        }
    }

    public void print(){
        if(printingElement != null){
            System.out.println(printingElement.getValue());
            printingElement = printingElement.getNext();
            print();
        } else {
            printingElement = firstElement;
        }
    }
}
