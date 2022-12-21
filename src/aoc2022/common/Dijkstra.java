package aoc2022.common;

import java.util.*;
import java.util.function.Function;

public class Dijkstra<T> {

    private final Function<T, List<T>> neighbours;

    public Dijkstra(Function<T, List<T>> neighbours) {
        this.neighbours = neighbours;
    }

    public List<T> path(T source, T target) {
        Map<T, Integer> distTo = new HashMap<>();
        Map<T, T> prev = new HashMap<>();
        distTo.put(source, 0);
        Queue<T> q = new PriorityQueue<>(Comparator.comparingInt(distTo::get));
        q.add(source);
        prev.put(source, null);

        while (!q.isEmpty()) {
            T current = q.poll();
            if (current == target) {
                break;
            }
            for (T next : neighbours.apply(current)) {
                int dc = distTo.getOrDefault(current, Integer.MAX_VALUE - 1);
                int dn = dc + 1;
                if (!distTo.containsKey(next) || dn < distTo.getOrDefault(next, Integer.MAX_VALUE - 1)) {
                    distTo.put(next, dn);
                    prev.put(next, current);
                    q.remove(next);
                    q.add(next);
                }
            }
        }

        List<T> path = new ArrayList<>();
        T p = prev.get(target);
        while (p != null) {
            path.add(p);
            p = prev.get(p);
        }
        Collections.reverse(path);
//        System.err.println(distTo.get(target));
        return path;
    }
}
