package aoc2022.common;

import java.util.Set;

public record Coord(int i, int j) {

    public int manhatanDistance(Coord other) {
        return Math.abs(other.i - i) + Math.abs(other.j - j);
    }

    public Set<Coord> adj8() {
        return Set.of(
                new Coord(i - 1, j),
                new Coord(i + 1, j),
                new Coord(i, j - 1),
                new Coord(i, j + 1),
                new Coord(i - 1, j - 1),
                new Coord(i - 1, j + 1),
                new Coord(i + 1, j - 1),
                new Coord(i + 1, j + 1)
        );
    }

    public Set<Coord> adj4() {
        return Set.of(
                new Coord(i - 1, j),
                new Coord(i + 1, j),
                new Coord(i, j - 1),
                new Coord(i, j + 1)
        );
    }
}
