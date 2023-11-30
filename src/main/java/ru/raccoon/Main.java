package ru.raccoon;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static void main(String[] args) {
        ArrayBlockingQueue<String> arrayBlockingQueueForA = new ArrayBlockingQueue<>(100);
        ArrayBlockingQueue<String> arrayBlockingQueueForB = new ArrayBlockingQueue<>(100);
        ArrayBlockingQueue<String> arrayBlockingQueueForC = new ArrayBlockingQueue<>(100);

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String str = generateText("abc", 100_000);
                try {
                    arrayBlockingQueueForA.put(str);
                    arrayBlockingQueueForB.put(str);
                    arrayBlockingQueueForC.put(str);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

            }
        }).start();

        new Thread(() -> doCount(arrayBlockingQueueForA, "a")).start();

        new Thread(() -> doCount(arrayBlockingQueueForB, "b")).start();

        new Thread(() -> doCount(arrayBlockingQueueForC, "c")).start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    //метод подсчёта символов в строке
    public static int getSymbolCount(String str, String symbol) {
        return str.length() - str.replace(symbol, "").length();
    }

    public static void printResult(String symbol, int count, String resultString) {
        System.out.println("\nСтрока с максимальным количеством символов " + symbol + " (" + count + " вхождений): " + resultString);
    }

    public static void doCount(ArrayBlockingQueue<String> array, String symbol) {
        int count = 0;
        String stringWithMaxCount = "";
        for (int i = 0; i < 10_000; i++) {
            String tmp;
            try {
                tmp = array.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int tmpLength = getSymbolCount(tmp, symbol);
            if (tmpLength > count) {
                count = tmpLength;
                stringWithMaxCount = tmp;
            }
        }
        printResult(symbol, count, stringWithMaxCount);
    }
}