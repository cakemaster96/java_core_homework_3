package org.example.hw.exceptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Errors {

    public void throwsOOMException() {
     int [] array = new int[8];
        List<byte[]> memoryKiller = new ArrayList<>();
        while (true) {
            memoryKiller.add(new byte[1000000000]);
        }
    }

    public void throwsStackOverFlowError() { //бесконечная рекурсия
        throwsStackOverFlowError();
    }
}
