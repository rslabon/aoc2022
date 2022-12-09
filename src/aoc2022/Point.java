package aoc2022;

import java.util.Collections;
import java.util.List;

record Point(int x, int y) {

    public static List<Point> neighbours(Point p) {
        if (p.equals(new Point(0, 0))) {
            return List.of(new Point(1, 1), new Point(2, 1));
        }
        if (p.equals(new Point(1, 1))) {
            return List.of(new Point(2, 2));
        }
        if (p.equals(new Point(2, 2))) {
            return List.of(new Point(3, 3));
        }
        if (p.equals(new Point(2, 1))) {
            return List.of(new Point(3, 3));
        }
        return Collections.emptyList();
    }

    public static int cost(Point from, Point to) {
        return 1;
    }

    public static int heuristic(Point a, Point b) {
        int dx = Math.abs(a.x() - b.x());
        int dy = Math.abs(a.y() - b.y());
        return dx + dy;
    }
}