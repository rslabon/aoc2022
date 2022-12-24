package aoc2022.common;

import java.util.concurrent.Callable;

public class Perf {
    public static void time(Callable<Void> action) {
        long start = System.currentTimeMillis();
        try {
            action.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.err.println("Took " + (end - start) + " [ms]");
    }
}
