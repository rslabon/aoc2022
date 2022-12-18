package aoc2022;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

record Cube(int x, int y, int z) {

    public Set<Cube> potentialNeighbours() {
        return Set.of(
                new Cube(x - 1, y, z),
                new Cube(x, y - 1, z),
                new Cube(x, y, z - 1),
                new Cube(x, y, z + 1),
                new Cube(x, y + 1, z),
                new Cube(x + 1, y, z)
        );
    }
}

public class Day18 {
    public static void main(String[] args) throws Exception {
//        String input = "2,2,2\n" +
//                "1,2,2\n" +
//                "3,2,2\n" +
//                "2,1,2\n" +
//                "2,3,2\n" +
//                "2,2,1\n" +
//                "2,2,3\n" +
//                "2,2,4\n" +
//                "2,2,6\n" +
//                "1,2,5\n" +
//                "3,2,5\n" +
//                "2,1,5\n" +
//                "2,3,5";

        String input = Files.readString(Path.of("resources/day18.txt"));

        Set<Cube> cubes = new HashSet<>();
        for (String line : input.split("\n")) {
            List<Integer> coords = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
            Cube cube = new Cube(coords.get(0), coords.get(1), coords.get(2));
            cubes.add(cube);
        }

        part1(cubes);
    }

    private static void part1(Set<Cube> cubes) {
        int area = 6 * cubes.size();
        for (Cube c : cubes) {
            int connected = 0;
            for (Cube other : c.potentialNeighbours()) {
                if (cubes.contains(other)) {
                    connected++;
                }
            }
            area -= connected;
        }
        System.err.println(area);
    }
}
