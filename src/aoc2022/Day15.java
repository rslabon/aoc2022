package aoc2022;


import aoc2022.common.Coord;
import aoc2022.common.Range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

record Zone(Coord sensor, Coord beacon) {

    public int manhatan() {
        return Math.abs(sensor.i() - beacon.i()) + Math.abs(sensor.j() - beacon.j());
    }

    public Range sensorRangeForRow(int j) {
        int manhatan = manhatan();
        int rowDiff = Math.abs(sensor().j() - j);
        int start = sensor.i() - (manhatan - rowDiff);
        int end = sensor.i() + (manhatan - rowDiff);
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
            boolean hasSpace = beaconRanges.size() == 2;
            if (hasSpace) {
                int spaceSize = beaconRanges.get(1).start() - (beaconRanges.get(0).end() + 1);
                boolean hasSpaceForBeacon = spaceSize == 1;
                if (hasSpaceForBeacon) {
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
        for (Range current : exclusiveZones) {
            Range last = stack.pop();
            if (last.intersect(current)) {
                Range merged = new Range(Math.min(last.start(), current.start()), Math.max(last.end(), current.end()));
                stack.push(merged);
            } else {
                stack.push(last);
                stack.push(current);
            }
        }
        List<Range> mergedRanges = new ArrayList<>(stack);
        mergedRanges.sort(Comparator.comparingInt(Range::start));
        return mergedRanges;
    }
}
