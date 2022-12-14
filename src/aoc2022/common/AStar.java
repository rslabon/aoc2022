package aoc2022.common;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


public class AStar<T> {
    private final Function<T, List<T>> neighbours;
    private final BiFunction<T, T, Integer> cost;
    private final BiFunction<T, T, Integer> heuristic;

    public AStar(Function<T, List<T>> neighbours,
                 BiFunction<T, T, Integer> cost,
                 BiFunction<T, T, Integer> heuristic) {
        this.neighbours = neighbours;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public List<T> path(T start, T goal) {
        Map<T, Integer> priorities = new HashMap<>();
        priorities.put(start, 0);
        PriorityQueue<T> frontier = new PriorityQueue<>(Comparator.comparingInt(priorities::get));
        frontier.add(start);
        Map<T, T> cameFrom = new HashMap<>();
        Map<T, Integer> costSoFar = new HashMap<>();
        cameFrom.put(start, null);
        costSoFar.put(start, 0);

        while (!frontier.isEmpty()) {
            T current = frontier.poll();
            if (current.equals(goal)) {
                break;
            }
            for (T next : neighbours.apply(current)) {
                int newCost = costSoFar.get(current) + cost.apply(current, next);
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    int priority = newCost + heuristic.apply(next, goal);
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
        AStar<Point> aStar = new AStar<>(Point::neighbours, Point::cost, Point::heuristic);
        List<Point> path = aStar.path(new Point(0, 0), new Point(3, 3));
        System.err.println(path);
    }
}
