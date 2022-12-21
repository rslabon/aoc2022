package aoc2022.common;

import java.util.Set;

public record Coord3D(int x, int y, int z) {

    public Set<Coord3D> potentialNeighbours() {
        return Set.of(
                new Coord3D(x - 1, y, z),
                new Coord3D(x, y - 1, z),
                new Coord3D(x, y, z - 1),
                new Coord3D(x, y, z + 1),
                new Coord3D(x, y + 1, z),
                new Coord3D(x + 1, y, z)
        );
    }
}
