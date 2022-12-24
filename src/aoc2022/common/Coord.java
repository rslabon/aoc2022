package aoc2022.common;

import java.util.Set;

public record Coord(int i, int j) {
    public Set<Coord> adj() {
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
}
