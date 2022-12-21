package aoc2022;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Monkey {
    public String id;
    public Long value;
    public BiFunction<Long, Long, Long> math;
    public String left;
    public String right;

    public Monkey(String id, Long value) {
        this.id = id;
        this.value = value;
    }

    public Monkey(String id, BiFunction<Long, Long, Long> math) {
        this.id = id;
        this.math = math;
    }

    public long yell(Map<String, Monkey> monkeys) {
        if (value != null) {
            return value;
        }
        value = math.apply(monkeys.get(left).yell(monkeys), monkeys.get(right).yell(monkeys));
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Monkey monkey = (Monkey) o;
        return Objects.equals(id, monkey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


public class Day21 {
    public static void main(String[] args) throws Exception {
        String example = "root: pppw + sjmn\n" +
                "dbpl: 5\n" +
                "cczh: sllz + lgvd\n" +
                "zczc: 2\n" +
                "ptdq: humn - dvpt\n" +
                "dvpt: 3\n" +
                "lfqf: 4\n" +
                "humn: 5\n" +
                "ljgn: 2\n" +
                "sjmn: drzm * dbpl\n" +
                "sllz: 4\n" +
                "pppw: cczh / lfqf\n" +
                "lgvd: ljgn * ptdq\n" +
                "drzm: hmdt - zczc\n" +
                "hmdt: 32";

        String puzzleInput = Files.readString(Path.of("resources/day21.txt"));

        Map<String, Monkey> monkeys = parseMonkeys(puzzleInput);
        System.err.println(" part1=" + part1(monkeys));
        System.err.println(" part2=" + part2(monkeys));
    }

    private static long part1(Map<String, Monkey> monkeys) {
        return monkeys.get("root").yell(monkeys);
    }

    record Result(long left, long right, Long number) {
    }

    private static long part2(Map<String, Monkey> monkeys) {
        long start = 0;
        long end = 5_000_000_000_000_000L;
        Result first = guessHummNumber(0, monkeys);

        while (start < end) {
            long mid = Math.divideExact(Math.addExact(start, end), 2);
            Result current = guessHummNumber(mid, monkeys);
            if (current.number != null) {
                return current.number;
            } else if (first.left < first.right) {
                if (current.left < current.right) {
                    start = mid;
                } else {
                    end = mid;
                }
            } else if (first.left > first.right) {
                if (current.left > current.right) {
                    start = mid;
                } else {
                    end = mid;
                }
            }
        }
        return -1;
    }

    private static Result guessHummNumber(long n, Map<String, Monkey> monkeys) {
        Monkey root = monkeys.get("root");
        Monkey left = monkeys.get(root.left);
        Monkey right = monkeys.get(root.right);
        Monkey humn = monkeys.get("humn");
        for (Monkey monkey : monkeys.values()) {
            if (monkey.math != null) {
                monkey.value = null;
            }
        }
        humn.value = n;
        long leftValue = left.yell(monkeys);
        long rightValue = right.yell(monkeys);

        if (leftValue == rightValue) {
            return new Result(leftValue, rightValue, n);
        }
        return new Result(leftValue, rightValue, null);
    }

    private static Map<String, Monkey> parseMonkeys(String input) {
        Map<String, Monkey> monkeys = new HashMap<>();
        for (String line : input.split("\n")) {
            Monkey monkey = parseMonkey(line);
            monkeys.put(monkey.id, monkey);
        }
        return monkeys;
    }

    private static Monkey parseMonkey(String line) {
        String[] parts = line.split(":");
        Long value = null;
        try {
            value = Long.parseLong(parts[1].trim());
        } catch (Exception e) {
            Pattern p = Pattern.compile("(\\w+)\\s+([\\+\\-\\*\\/])\\s+(\\w+)");
            Matcher m = p.matcher(parts[1].trim());
            m.find();
            String op = m.group(2);
            Monkey monkey = new Monkey(parts[0], parseMath(op));
            monkey.left = m.group(1);
            monkey.right = m.group(3);
            return monkey;
        }
        return new Monkey(parts[0], value);
    }

    private static BiFunction<Long, Long, Long> parseMath(String op) {
        return switch (op) {
            case "+" -> (v1, v2) -> v1 + v2;
            case "-" -> (v1, v2) -> v1 - v2;
            case "*" -> (v1, v2) -> v1 * v2;
            default -> (v1, v2) -> v1 / v2;
        };
    }
}
