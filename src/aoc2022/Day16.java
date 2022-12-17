package aoc2022;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

record Pair<LEFT, RIGHT>(LEFT left, RIGHT right) {
}

class Move {
    Valve from;
    Valve to;
    int path;
    int cost;

    public Move(Valve from, Valve to, int path) {
        this.from = from;
        this.to = to;
        this.path = path;
    }

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
//        System.err.println(cost(3, 21));
    }

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
                    moves.add(new Move(first, second, path.size()));
                }
            }
        }

        final AtomicReference<Valve> current = new AtomicReference<>(start);
        int minute = 0;
        int currentPressure = 0;
        int totalPressure = 0;

        Set<String> open = new HashSet<>();

        while (minute < 30) {
            minute++;
            System.err.println("== Minute " + minute + " ==");
            System.err.println("Valves " + open.stream().sorted().toList() + " are open, releasing " + currentPressure + " pressure" + " total="+totalPressure);

            int rem = 30 - minute+1;
            moves = moves.stream().filter(m -> !open.contains(m.to.name()))
                    .filter(m->m.to.rate() > 0)
                    .collect(Collectors.toList());
            moves.forEach(m -> {
                m.cost = (m.to.rate() * rem) - cost(m.path, m.to.rate());
//                System.err.println(m +  " cost=" + m.to.rate() + "*" + rem +"=" +(m.to.rate()*rem) + "-" + cost(m.path, m.to.rate()) + " path=" + m.path);
            });
            moves.sort((m1, m2) -> m2.cost - m1.cost);
            System.err.println(rem + " " + moves);
            Optional<Move> move = moves.stream().filter(m -> m.from == current.get() && !open.contains(m.to.name())).findFirst();
//            System.err.println(moves);

            if (move.isPresent()) {
                System.err.println("Jump from " + move.get().from.name() + " to " + move.get().to.name());
                Valve next = move.get().to;
                List<Valve> path = paths.get(new Pair<>(current.get(), next));
                Valve prev = path.get(0);
                for (Valve each : path.subList(1, path.size())) {
                    minute++;
                    if (minute > 30) return totalPressure;

                    System.err.println(path.stream().map(Valve::name).toList());

                    System.err.println("== Minute " + minute + " ==");
                    System.err.println("Valves " + open.stream().sorted().toList() + " are open, releasing " + currentPressure + " pressure");
                    System.err.println("You moved from " + prev.name() + " to valve " + each.name());
                    prev = each;
                    current.set(each);
                }
                current.set(next);
                minute++;
                if (minute > 30) return totalPressure;

                System.err.println("== Minute " + minute + " ==");
                System.err.println("Valves " + open.stream().sorted().toList() + " are open, releasing " + currentPressure + " pressure");
                System.err.println("You open valve " + move.get().to.name());
                currentPressure += current.get().rate();
                totalPressure += current.get().rate() * (30 - minute-1);
                open.add(current.get().name());
//                current.set(next);
            }
        }
        return totalPressure;
    }

    private static int cost(int pathSize, int rate) {
        int c = 0;
        for (int i = 0; i <= pathSize; i++) {
            c += i * rate;
        }
        return c;
    }
}