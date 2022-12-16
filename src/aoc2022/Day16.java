package aoc2022;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

record Pair<LEFT, RIGHT>(LEFT left, RIGHT right) {
}

record Move(Valve from, Valve to, int path, double cost) {

    @Override
    public String toString() {
        return "move " + from.name() + " -> " + to.name() + " c=" + cost;
    }
}

record Valve(String name, int rate, Set<Valve> tunnels) {

    public void addTunel(Valve v) {
        tunnels.add(v);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "{" + name + "[" + rate + "] <" + tunnels.stream().map(Valve::name).toList() + "> }";
    }
}

public class Day16 {

    public static void main(String[] args) throws Exception {
        String input = "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB\n" +
                "Valve BB has flow rate=13; tunnels lead to valves CC, AA\n" +
                "Valve CC has flow rate=2; tunnels lead to valves DD, BB\n" +
                "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE\n" +
                "Valve EE has flow rate=3; tunnels lead to valves FF, DD\n" +
                "Valve FF has flow rate=0; tunnels lead to valves EE, GG\n" +
                "Valve GG has flow rate=0; tunnels lead to valves FF, HH\n" +
                "Valve HH has flow rate=22; tunnel leads to valve GG\n" +
                "Valve II has flow rate=0; tunnels lead to valves AA, JJ\n" +
                "Valve JJ has flow rate=21; tunnel leads to valve II";

//        String input = Files.readString(Path.of("resources/day16.txt"));// zle 1489


        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Valve> nodes = new HashMap<>();
        for (String line : input.split("\n")) {
            Pattern p = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); (tunnel|tunnels) (lead|leads) to (valves|valve) (.+)");
            Matcher m = p.matcher(line);
            m.find();
            Valve valve = new Valve(m.group(1), Integer.parseInt(m.group(2)), new HashSet<>());
            nodes.put(valve.name(), valve);
            adj.put(valve.name(), Arrays.stream(m.group(6).split(",")).map(String::trim).collect(Collectors.toSet()));
        }

        for (String name : adj.keySet()) {
            Valve valve = nodes.get(name);
            Set<Valve> nextValves = adj.get(name).stream().map(n -> nodes.get(n)).collect(Collectors.toSet());
            for (Valve next : nextValves) {
                valve.addTunel(next);
            }
        }

        Valve startingValve = nodes.get("AA");
        System.err.println("part1=" + part1(nodes, startingValve));
    }

//    private static int part1(Map<String, Valve> valves, Valve start) {
//        Dijkstra<Valve> dijkstra = new Dijkstra<>(current -> current.tunnels().stream().toList());
//        Map<Pair<Valve, Valve>, List<Valve>> paths = new HashMap<>();
//        for (Valve first : valves.values()) {
//            for (Valve second : valves.values()) {
//                if (first != second) {
//                    List<Valve> path = dijkstra.path(first, second);
//                    paths.put(new Pair<>(first, second), path);
//                }
//            }
//        }
//        Set<Valve> open = new HashSet<>();
//        List<Move> moves = new ArrayList<>();
//        for (Valve first : valves.values()) {
//            for (Valve second : valves.values()) {
//                if (first != second) {
//                    List<Valve> path = paths.get(new Pair<>(first, second));
//                    // System.err.println(first.name() + "->" + second.name() + " p=" + path.size());
//                    double ratePerMinute = ((double) second.rate()) / path.size();
//                    moves.add(new Move(first, second, path.size(), ratePerMinute));
//                }
//            }
//        }
//        moves = moves.stream().filter(m -> m.cost() > 0).collect(Collectors.toList());
//        moves.sort((m1, m2) -> Double.compare(m2.cost(), m1.cost()));
//
//
//        final AtomicReference<Valve> current = new AtomicReference<>(start);
//        int minute = 0;
//        int currentPressure = 0;
//        int totalPressure = 0;
//        while (minute < 30) {
//            totalPressure += currentPressure;
//            minute++;
//            System.err.println("== Minute " + minute + " ==");
//            System.err.println("Valves " + open.stream().map(Valve::name).sorted().toList() + " are open, releasing " + currentPressure + " pressure");
//
//            int rem = 30 - minute + 1;
//            moves = moves.stream().filter(m -> !open.contains(m.to())).collect(Collectors.toList());
//            moves.sort((m1, m2) -> Double.compare(m2.cost() * (Math.floor((double) rem / m2.path())), m1.cost() * (Math.floor((double) rem / m1.path()))));
////            System.err.println(moves);
//            Optional<Move> move = moves.stream().filter(m -> m.from() == current.get() && !open.contains(m.to())).findFirst();
////            System.err.println(moves);
//
//            if (move.isPresent()) {
////                System.err.println("Jump to " + move.get().to().name());
//                Valve next = move.get().to();
//                List<Valve> path = paths.get(new Pair<>(current.get(), next));
//                for (Valve each : path.subList(1, path.size())) {
//                    minute++;
//                    if (minute > 30) return totalPressure;
//                    totalPressure += currentPressure;
//
//                    System.err.println(path.stream().map(Valve::name).toList());
//
//                    System.err.println("== Minute " + minute + " ==");
//                    System.err.println("Valves " + open.stream().map(Valve::name).sorted().toList() + " are open, releasing " + currentPressure + " pressure");
//                    System.err.println("You move to valve " + each.name());
//                }
//                minute++;
//                if (minute > 30) return totalPressure;
//                totalPressure += currentPressure;
//
//                System.err.println("== Minute " + minute + " ==");
//                System.err.println("Valves " + open.stream().map(Valve::name).sorted().toList() + " are open, releasing " + currentPressure + " pressure");
//                System.err.println("You open valve " + move.get().to().name());
//                currentPressure += next.rate();
//                open.add(next);
//                current.set(next);
//            }
//        }
//        return totalPressure;
//    }

    private static int part1(Map<String, Valve> valves, Valve start) {
        Dijkstra<Valve> dijkstra = new Dijkstra<>(current -> current.tunnels().stream().toList());
        Map<Pair<Valve, Valve>, List<Valve>> paths = new HashMap<>();
        for (Valve first : valves.values()) {
            for (Valve second : valves.values()) {
                if (first != second) {
                    List<Valve> path = dijkstra.path(first, second);
                    paths.put(new Pair<>(first, second), path);
                }
            }
        }

        List<Move> moves = new ArrayList<>();
        for (Valve first : valves.values()) {
            for (Valve second : valves.values()) {
                if (first != second) {
                    List<Valve> path = paths.get(new Pair<>(first, second));
                    // System.err.println(first.name() + "->" + second.name() + " p=" + path.size());
                    double ratePerMinute = ((double) second.rate()) / path.size();
                    moves.add(new Move(first, second, path.size(), ratePerMinute));
                }
            }
        }
        moves = moves.stream().filter(m -> m.cost() > 0).collect(Collectors.toList());
        moves.sort((m1, m2) -> Double.compare(m2.cost(), m1.cost()));

//
        final AtomicReference<Valve> current = new AtomicReference<>(start);
        int minute = 0;
        int currentPressure = 0;
        int totalPressure = 0;

        Set<String> open = new HashSet<>();


        HashSet<Valve> ta = new HashSet<>();
        HashSet<Valve> tb = new HashSet<>();
        HashSet<Valve> tc = new HashSet<>();
        Valve c = new Valve("C", 15, tc);
        Valve b = new Valve("B", 10, tb);
        Valve a = new Valve("A", 0, ta);
        tc.add(a);
        tc.add(b);
//        tc.add(c);
//        tb.add(a);
//        tb.add(b);
        tb.add(c);
//        ta.add(a);
        ta.add(b);
        ta.add(c);
//        start = a;


//        int maxMinute = 30;
//        int total = move(start, start, currentPressure, totalPressure, minute, maxMinute, open, new ArrayList<>());
//        return total;
//
//
        while (minute < 30) {
//            totalPressure += currentPressure;
            minute++;
            System.err.println("== Minute " + minute + " ==");
            System.err.println("Valves " + open.stream().sorted().toList() + " are open, releasing " + currentPressure + " pressure");

            int rem = 30 - minute + 1;
            moves = moves.stream().filter(m -> !open.contains(m.to().name())).collect(Collectors.toList());
            moves.sort((m1, m2) -> Double.compare(
                    (m2.to().rate() * rem) - (m2.to().rate() * m2.path()),
                    (m1.to().rate() * rem) - (m1.to().rate() * m1.path())
            ));
//            System.err.println(moves);
            Optional<Move> move = moves.stream().filter(m -> m.from() == current.get() && !open.contains(m.to().name())).findFirst();
//            System.err.println(moves);

            if (move.isPresent()) {
                System.err.println("Jump from " + move.get().from().name() + " to " + move.get().to().name());
                Valve next = move.get().to();
                List<Valve> path = paths.get(new Pair<>(current.get(), next));
                Valve prev = path.get(0);
                for (Valve each : path.subList(1, path.size())) {
                    minute++;
                    if (minute > 30) return totalPressure;
//                    totalPressure += currentPressure;

                    System.err.println(path.stream().map(Valve::name).toList());

                    System.err.println("== Minute " + minute + " ==");
                    System.err.println("Valves " + open.stream().sorted().toList() + " are open, releasing " + currentPressure + " pressure");
                    System.err.println("You moved from " +prev.name() + " to valve " + each.name());
                    prev = each;
                    current.set(each);
                    minute++;
                }
                current.set(next);
                minute++;
                if (minute > 30) return totalPressure;

                System.err.println("== Minute " + minute + " ==");
                System.err.println("Valves " + open.stream().sorted().toList() + " are open, releasing " + currentPressure + " pressure");
                System.err.println("You open valve " + move.get().to().name());
                currentPressure += current.get().rate();
                totalPressure += current.get().rate() * (30 - minute + 1);
                open.add(current.get().name());
//                current.set(next);
            }
        }
        return totalPressure;
    }

    static int maxTotal = Integer.MIN_VALUE;
    public static Map<List, Integer> cache = new HashMap<>();


    private static int move(Valve prev, Valve current, int currentPressure, int totalPressure, int minute, int maxMinute, Set<String> open, List<String> path) {
//        System.err.println(cache);

        if (cache.containsKey(getKey(prev, minute, open, current))) {
            Integer value = cache.get(getKey(prev, minute, open, current));
            System.err.println("cache " + prev.name() + "->" + current.name() + " MOVE " + getKey(prev, minute, open, current) + " hit=" + value);
            return value;
        }

//        Integer diff = cache.get(List.of(30 - minute, open, current.name()));
//        if(diff != null) {
//            System.err.println("HIT !!!!!!!!!!!!!!!!!!!!!!");
//        }
//
        int startP = totalPressure;
        if (minute >= maxMinute) {
            maxTotal = Math.max(maxTotal, totalPressure);
            System.err.println(" maxTotal=" + maxTotal + " totalPressure=" + totalPressure + " path=" + path);
            return totalPressure;
        }

//        path = new ArrayList<>(path);
        path.add(current.name() + " MOVE " + totalPressure);

//        Integer c = moveCache.get(new Pair<>(current.name(), minute));
//        if(c!= null) {
//            return c;
//        }

        minute++;
//        totalPressure += currentPressure;
        int max = totalPressure;
        for (Valve next : current.tunnels()) {

            List<String> movePath = new ArrayList<>(path);
            Set<String> openMove = new HashSet<>(open);
            int moveVal = move(current, next, currentPressure, totalPressure, minute, maxMinute, openMove, movePath);

            ArrayList<String> openPath = new ArrayList<>(path);
            HashSet<String> openOpen = new HashSet<>(open);
            int openVal = open(current, next, currentPressure, totalPressure, minute, maxMinute, openOpen, openPath);


            Set<String> orgOpen = new HashSet<>(open);

            if (moveVal > openVal) {
                max = moveVal;
                path = movePath;
                open = openMove;
            } else {
                max = openVal;
                path = openPath;
                open = openOpen;
            }


            System.err.println(minute + " " + current.name() + "->" + next.name() + " max=" + max + " path=" + path + " open=" + open);
            List<Object> key = getKey(current, minute, orgOpen, next);
            //cache.put(key, Math.max(cache.getOrDefault(key, -1), max));
        }
        System.err.println(minute + " t=" + max + " path=" + path + " open=" + open);
        return max;
    }

    private static int open(Valve prev, Valve current, int currentPressure, int totalPressure, int minute, int maxMinute, Set<String> open, List<String> path) {
//        System.err.println("Count " + count + " maxTotal=" + maxTotal);


//        System.err.println(cache);
        if (cache.containsKey(getKey(prev, minute, open, current))) {
            Integer value = cache.get(getKey(prev, minute, open, current));
            System.err.println("cache " + prev.name() + "->" + current.name() + " OPEN " + getKey(prev, minute, open, current) + " hit=" + value);
            return value;
        }

        int startP = totalPressure;
        if (open.contains(current.name()) || current.rate() == 0) {
            return totalPressure;
        }
        if (minute >= maxMinute) {
            maxTotal = Math.max(maxTotal, totalPressure);
            System.err.println(" maxTotal=" + maxTotal + " totalPressure=" + totalPressure + " path=" + path);
            return totalPressure;
        }

//        path = new ArrayList<>(path);
        path.add(current.name() + " OPEN " + totalPressure);

//
//        Integer diff = cache.get(List.of(30 - minute, open, current.name()));
//        if(diff != null) {
//            System.err.println("HIT !!!!!!!!!!!!!!!!!!!!!!");
//        }

//        Integer c = openCache.get(new Pair<>(current.name(), minute));
//        if(c != null) {
//            return c;
//        }

        minute += 2;
//        currentPressure += current.rate();
        totalPressure += current.rate() * (maxMinute - minute + 1);
//        open = new HashSet<>(open);
        open.add(current.name());
        int max = totalPressure;
        for (Valve next : current.tunnels()) {
//            max = Math.max(max, move(current, next, currentPressure, totalPressure, minute, maxMinute, open, path));
//            max = Math.max(max, open(current, next, currentPressure, totalPressure, minute, maxMinute, open, path));

            List<String> movePath = new ArrayList<>(path);
            Set<String> openMove = new HashSet<>(open);
            int moveVal = move(current, next, currentPressure, totalPressure, minute, maxMinute, openMove, movePath);

            ArrayList<String> openPath = new ArrayList<>(path);
            HashSet<String> openOpen = new HashSet<>(open);
            int openVal = open(current, next, currentPressure, totalPressure, minute, maxMinute, openOpen, openPath);


            Set<String> orgOpen = new HashSet<>(open);

            if (moveVal > openVal) {
                max = moveVal;
                path = movePath;
                open = openMove;
            } else {
                max = openVal;
                path = openPath;
                open = openOpen;
            }


            System.err.println(minute + " " + current.name() + "->" + next.name() + " max=" + max + " path=" + path + " open=" + open);
            List<Object> key = getKey(current, minute, orgOpen, next);
            // cache.put(key, Math.max(cache.getOrDefault(key, -1), max));
        }
        System.err.println(minute + " t=" + max + " path=" + path + " open=" + open);
        return max;
    }

    private static List<Object> getKey(Valve current, int minute, Set<String> open, Valve next) {
        return List.of(minute, open, current.name() + "->" + next.name());
    }
}