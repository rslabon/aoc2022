package aoc2022;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

record SNAFU(String n) {

    public long toDecimal() {
        long value = 0;
        String[] digits = n.split("");
        for (int i = digits.length - 1, p = 0; i >= 0; i--, p++) {
            long pos = (long) Math.pow(5, p);
            long decimal = switch (digits[i]) {
                case "2" -> pos * 2;
                case "1" -> pos;
                case "0" -> 0;
                case "-" -> pos * -1;
                case "=" -> pos * -2;
                default -> throw new IllegalStateException();
            };
            value += decimal;
        }
        return value;
    }

    public static SNAFU fromDecimal(long value) {
        int base = 5;
        int max = 2;
        Stack<Long> reminders = new Stack<>();
        long c = 0;
        long pq = value;
        while (true) {
            long q = (long) Math.floor(pq / (double) base);
            long r = pq % base;
            r += c;
            c = 0;
            if (r > max) {
                r = r - base;
                c = 1;
            }
            reminders.push(r);
            if (q == 0) {
                break;
            }
            pq = q;
        }
        if (c > 0) {
            reminders.push(c);
        }

        StringBuilder sb = new StringBuilder();
        while (!reminders.isEmpty()) {
            Long n = reminders.pop();
            if (n == -2L) {
                sb.append("=");
            }
            if (n == -1L) {
                sb.append("-");
            }
            if (n == 0L) {
                sb.append("0");
            }
            if (n == 1L) {
                sb.append("1");
            }
            if (n == 2L) {
                sb.append("2");
            }
        }
        return new SNAFU(sb.toString());
    }

    public static SNAFU sum(Collection<SNAFU> numbers) {
        long sum = numbers.stream().map(SNAFU::toDecimal).reduce(0L, Long::sum);
        return SNAFU.fromDecimal(sum);
    }

    @Override
    public String toString() {
        return "SNAFU [" + n + "]";
    }
}

public class Day25 {
    public static void main(String[] args) throws Exception {
        List<String> example = List.of("1=-0-2", "12111", "2=0=", "21", "2=01", "111", "20012", "112", "1=-1=", "1-12", "12", "1=", "122");
        List<SNAFU> snafus = example.stream().map(SNAFU::new).toList();

        List<SNAFU> puzzleNumbers = Files.readAllLines(Path.of("resources/day25.txt"))
                .stream()
                .filter(s -> !s.isBlank())
                .map(SNAFU::new).toList();

        System.err.println(" part1=" + part1(snafus));
        System.err.println(" part1=" + part1(puzzleNumbers));
    }

    private static SNAFU part1(Collection<SNAFU> numbers) {
        return SNAFU.sum(numbers);
    }
}
