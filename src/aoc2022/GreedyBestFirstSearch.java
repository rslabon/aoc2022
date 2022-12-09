package aoc2022;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GreedyBestFirstSearch<T> {
    private final Function<T, List<T>> neighbours;
    private final BiFunction<T, T, Integer> heuristic;

    public GreedyBestFirstSearch(Function<T, List<T>> neighbours,
                 BiFunction<T, T, Integer> heuristic) {
        this.neighbours = neighbours;
        this.heuristic = heuristic;
    }

    public List<T> path(T start, T goal) {
        Map<T, Integer> priorities = new HashMap<>();
        priorities.put(start, 0);
        PriorityQueue<T> frontier = new PriorityQueue<>(Comparator.comparingInt(priorities::get));
        frontier.add(start);
        Map<T, T> cameFrom = new HashMap<>();
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            T current = frontier.poll();
            if (current.equals(goal)) {
                break;
            }
            for (T next : neighbours.apply(current)) {
                if (!cameFrom.containsKey(next)) {
                    int priority = heuristic.apply(next, goal);
                    frontier.remove(next);
                    priorities.put(next, priority);
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }

        T p = goal;
        List<T> path = new ArrayList<>();
        while (p != null) {
            path.add(p);
            p = cameFrom.get(p);
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        GreedyBestFirstSearch<Point> search = new GreedyBestFirstSearch<>(Point::neighbours, Point::heuristic);
        List<Point> path = search.path(new Point(0, 0), new Point(3, 3));
        System.err.println(path);
    }
}
