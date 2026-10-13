package org.example.hw.functional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Generator {

    public static void main(String[] args) {
        System.out.println("");
    }

    public static Set<Integer> generateNRandomNumber(int number) {
        if (number <= 0)
            throw new IllegalArgumentException("Кол-во цифр не может быть 0 или отриацтельным");
        return new Random().ints().distinct().limit(number).boxed().collect(Collectors.toSet());
    }

    //eg 3, 6,12,24 etc
    public static Set<Integer> generateDoublingNumbers(int initialNumber, int limit) {
        return IntStream.iterate(initialNumber, number -> number * 2).limit(limit).boxed().collect(Collectors.toSet());
    }

    public static List<String> generateUUIDs(int uuidLength, int limit) {  //Тут подсказка ИИшки. Сам не понял, как делать
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        return IntStream.range(0, limit).mapToObj(i -> IntStream.range(0, uuidLength)
                        .mapToObj(j -> String.valueOf(chars.charAt(random.nextInt(chars.length()))))
                        .collect(Collectors.joining()))
                .toList();
    }

    public static Stream<Integer> makeStreamFromArray(Integer[] values) {
        return values == null ? Stream.empty() : Stream.of(values);
    }

    public static Stream<LocalDate> generateAnyLocalDates() {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);
        return startDate.datesUntil(endDate.plusDays(1));
    }

    //return same string N times eg "hello",2 => "hello", "hello"
    public static List<String> streamGeneratesSameStringNTimes(String input, int times){
    return Stream.generate(() -> input).limit(times).toList();
    }
    //generate number from to eg 3,7 => 3,4,5,6,7
    public static List<Integer> generateNumberFromTo(int from, int to) {
        return IntStream.rangeClosed(from, to).boxed().collect(Collectors.toList());
    }
}
