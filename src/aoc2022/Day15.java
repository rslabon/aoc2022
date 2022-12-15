package aoc2022;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

record Range(int start, int end) {
    boolean isValid() {
        return start <= end;
    }

    public int size() {
        return Math.abs(end - start);
    }

    public boolean intersect(Range other) {
        return other.start <= end;
    }
}

record Zone(Coord sensor, Coord beacon) {

    public int manhatan() {
        return Math.abs(sensor.i() - beacon.i()) + Math.abs(sensor.j() - beacon.j());
    }

    public Range sensorRangeForRow(int j) {
        int d = manhatan();
        int dj = Math.abs(sensor().j() - j);
        dj = dj * 2;
        dj /= 2;
        int start = sensor.i() - (d - dj);
        int end = sensor.i() + (d - dj);
        return new Range(start, end);
    }
}

public class Day15 {

    public static String example = "Sensor at x=2, y=18: closest beacon is at x=-2, y=15\n" +
            "Sensor at x=9, y=16: closest beacon is at x=10, y=16\n" +
            "Sensor at x=13, y=2: closest beacon is at x=15, y=3\n" +
            "Sensor at x=12, y=14: closest beacon is at x=10, y=16\n" +
            "Sensor at x=10, y=20: closest beacon is at x=10, y=16\n" +
            "Sensor at x=14, y=17: closest beacon is at x=10, y=16\n" +
            "Sensor at x=8, y=7: closest beacon is at x=2, y=10\n" +
            "Sensor at x=2, y=0: closest beacon is at x=2, y=10\n" +
            "Sensor at x=0, y=11: closest beacon is at x=2, y=10\n" +
            "Sensor at x=20, y=14: closest beacon is at x=25, y=17\n" +
            "Sensor at x=17, y=20: closest beacon is at x=21, y=22\n" +
            "Sensor at x=16, y=7: closest beacon is at x=15, y=3\n" +
            "Sensor at x=14, y=3: closest beacon is at x=15, y=3\n" +
            "Sensor at x=20, y=1: closest beacon is at x=15, y=3";


    public static Zone parse(String line) {
        String regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        m.find();
        Coord sensor = new Coord(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        Coord beacon = new Coord(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
        return new Zone(sensor, beacon);
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("resources/day15.txt"));
//        List<String> lines = Arrays.stream(example.split("\n")).toList();

        int row = 2000000;
        List<Zone> zones = parseZones(lines);
        System.err.println("part1=" + part1(zones, row));
        System.err.println("part2=" + part2(zones, 4000000));
    }

    private static int part1(List<Zone> zones, int row) {
        List<Range> exclusiveZones = getExclusiveZones(zones, row);
        int total = 0;
        for (Range r : exclusiveZones) {
            total += r.size();
        }
        return total;
    }

    private static long part2(List<Zone> zones, int max) {
        for (int i = 0; i < max; i++) {
            List<Range> beaconRanges = getExclusiveZones(zones, i);
            if (beaconRanges.size() == 2) {
                int length = beaconRanges.get(1).start() - beaconRanges.get(0).end();
                if (length == 2) {
                    int col = beaconRanges.get(0).end() + 1;
                    return ((long) col * max) + i;
                }
            }
        }
        return -1;
    }

    private static List<Range> getExclusiveZones(List<Zone> zones, int row) {
        List<Range> exclusiveZones = new ArrayList<>();
        for (Zone zone : zones) {
            Range r = zone.sensorRangeForRow(row);
            if (r.isValid()) {
                exclusiveZones.add(r);
            }
        }
        return merge(exclusiveZones);
    }

    private static List<Zone> parseZones(List<String> lines) {
        List<Zone> zones = new ArrayList<>();
        for (String line : lines) {
            if (!line.isBlank()) {
                zones.add(parse(line));
            }
        }
        return zones;
    }

    private static List<Range> merge(List<Range> exclusiveZones) {
        if (exclusiveZones.isEmpty()) {
            return Collections.emptyList();
        }
        exclusiveZones.sort(Comparator.comparingInt(Range::start));
        Stack<Range> stack = new Stack<>();
        stack.push(exclusiveZones.get(0));
        for (Range r : exclusiveZones) {
            Range last = stack.pop();
            if (last.intersect(r)) {
                Range r3 = new Range(Math.min(last.start(), r.start()), Math.max(last.end(), r.end()));
                stack.push(r3);
            } else {
                stack.push(last);
                stack.push(r);
            }
        }
        List<Range> mergedRanges = new ArrayList<>(stack);
        mergedRanges.sort(Comparator.comparingInt(Range::start));
        if (exclusiveZones.equals(mergedRanges)) {
            return exclusiveZones;
        } else {
            return merge(mergedRanges);
        }
    }
}
