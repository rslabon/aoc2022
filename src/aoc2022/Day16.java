package aoc2022;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

record Pair<LEFT, RIGHT>(LEFT left, RIGHT right) {
}

record Move(Valve from, Valve to, int path, double cost) {
}

record Valve(String name, int rate, Set<Valve> tunnels) {

    public void addTunel(Valve v) {
        tunnels.add(v);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}

public class Day16 {

    public static void main(String[] args) throws Exception {
//        String input = "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB\n" +
//                "Valve BB has flow rate=13; tunnels lead to valves CC, AA\n" +
//                "Valve CC has flow rate=2; tunnels lead to valves DD, BB\n" +
//                "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE\n" +
//                "Valve EE has flow rate=3; tunnels lead to valves FF, DD\n" +
//                "Valve FF has flow rate=0; tunnels lead to valves EE, GG\n" +
//                "Valve GG has flow rate=0; tunnels lead to valves FF, HH\n" +
//                "Valve HH has flow rate=22; tunnel leads to valve GG\n" +
//                "Valve II has flow rate=0; tunnels lead to valves AA, JJ\n" +
//                "Valve JJ has flow rate=21; tunnel leads to valve II";

        String input = Files.readString(Path.of("resources/day16.txt"));// zle 1489


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

        System.err.println("part1=" + part1(nodes));
        System.err.println("part2=" + part2(nodes));
    }

    private static final Map<Set<String>, Integer> openValvesToMaxTotal = new HashMap<>();

    private static int part1(Map<String, Valve> valves) {
        openValvesToMaxTotal.clear();
        Map<Pair<Valve, Valve>, List<Valve>> paths = createPaths(valves);
        List<Move> moves = createMoves(valves, paths);
        Valve aa = valves.get("AA");
        State state = new State(0, 0, 0, new HashSet<>(), moves, paths);
        return step(30, aa, false, state).total;
    }

    private static int part2(Map<String, Valve> valves) {
        openValvesToMaxTotal.clear();
        Map<Pair<Valve, Valve>, List<Valve>> paths = createPaths(valves);
        List<Move> moves = createMoves(valves, paths);
        Valve aa = valves.get("AA");
        State state = new State(0, 0, 0, new HashSet<>(), moves, paths);
        step(26, aa, false, state);

        int max = Integer.MIN_VALUE;
        for (Set<String> myOpen : openValvesToMaxTotal.keySet()) {
            for (Set<String> elephantOpen : openValvesToMaxTotal.keySet()) {
                if (myOpen.equals(elephantOpen)) {
                    continue;
                }
                Set<String> intersection = new HashSet<>(myOpen);
                intersection.retainAll(elephantOpen);
                if (intersection.isEmpty()) {
                    max = Math.max(max, openValvesToMaxTotal.get(myOpen) + openValvesToMaxTotal.get(elephantOpen));
                }
            }
        }
        return max;
    }

    private static List<Move> createMoves(Map<String, Valve> valves, Map<Pair<Valve, Valve>, List<Valve>> paths) {
        List<Move> moves = new ArrayList<>();
        for (Valve first : valves.values()) {
            for (Valve second : valves.values()) {
                if (first != second && second.rate() > 0) {
                    List<Valve> path = paths.get(new Pair<>(first, second));
                    moves.add(new Move(first, second, path.size(), second.rate()));
                }
            }
        }
        return moves;
    }

    private static Map<Pair<Valve, Valve>, List<Valve>> createPaths(Map<String, Valve> valves) {
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
        return paths;
    }

    static class State {
        int current;
        int total;
        int minute;
        Set<String> opened;
        Map<Pair<Valve, Valve>, List<Valve>> paths;
        List<Move> moves;

        public State(int current, int total, int minute, Set<String> opened, List<Move> moves, Map<Pair<Valve, Valve>, List<Valve>> paths) {
            this.current = current;
            this.total = total;
            this.minute = minute;
            this.opened = opened;
            this.moves = moves;
            this.paths = paths;
        }

        public State copy() {
            return new State(
                    current, total, minute, new HashSet<>(opened), new ArrayList<>(moves), paths
            );
        }

    }

    private static State step(int maxMinute, Valve valve, boolean open, State state) {
        if (state.minute > maxMinute) {
            store(state);
            return state;
        }
        state.minute++;
//        System.err.println("\n\n== Minute " + state.minute + " ==");
//        if (state.opened.isEmpty()) {
//            System.err.println("No valves are open.");
//        }
//        if (!open) {
//            System.err.println("You move to valve " + valve.name() + ".");
//        }
        if (state.minute > maxMinute) {
            store(state);
            return state;
        }
        if (open && !state.opened.contains(valve.name())) {
            state.minute++;
            state.opened.add(valve.name());
            state.current += valve.rate();
            state.total += valve.rate() * (maxMinute - state.minute + 1);
            store(state);
//            System.err.println("\n\n== Minute " + state.minute + " ==");
//            System.err.println("Valves " + state.opened + " are open, releasing " + state.current + " pressure.");
        }
        if (state.minute > maxMinute) {
            store(state);
            return state;
        }
        Set<String> opened = state.opened;
        List<Move> moves = state.moves.stream()
                .filter(m -> m.from().equals(valve) && !opened.contains(m.to().name()))
                .toList();


        State max = state.copy();
        for (Move m : moves) {
            State s = state.copy();
            s.minute += m.path() - 1;
            State ss = step(maxMinute, m.to(), true, s);
            if (max.total < ss.total) {
                max = ss;
            }
        }
        store(max);
        return max;
    }

    private static void store(State state) {
        Integer max = openValvesToMaxTotal.get(state.opened);
        if (max == null || max < state.total) {
            openValvesToMaxTotal.put(new HashSet<>(state.opened), state.total);
        }
    }
}