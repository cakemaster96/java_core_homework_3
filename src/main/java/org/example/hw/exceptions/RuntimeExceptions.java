package org.example.hw.exceptions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RuntimeExceptions {

    public void throwsNullPointerException() {
        List<Integer> list = new ArrayList<>();
        list.add(null);
        System.out.println(list.getFirst()*2);
    }

    public void throwsArrayIndexOutOfBoundsException() {
        int [] array = new int[5];
        System.out.println(array[6]);
        //
    }

    public void throwsNumberFormatException() {
        String myString =  "andfsdf";
        System.out.println(Integer.parseInt(myString) + 6);
    }

    public void throwsIllegalArgumentException() {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).limit(-4).toList();
    }

    public static void throwsClassCastException() {
        Object something = new Object();
        String text = (String) something;
        //
    }

}
