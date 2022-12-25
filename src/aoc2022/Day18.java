package aoc2022;

import aoc2022.common.Coord3D;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

        Set<Coord3D> cubes = new HashSet<>();
        for (String line : input.split("\n")) {
            List<Integer> coords = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
            Coord3D cube = new Coord3D(coords.get(0), coords.get(1), coords.get(2));
            cubes.add(cube);
        }

        part1(cubes);
        part2(cubes);
    }

    private static void part1(Set<Coord3D> cubes) {
        int area = 6 * cubes.size();
        for (Coord3D c : cubes) {
            int connected = 0;
            for (Coord3D other : c.potentialNeighbours()) {
                if (cubes.contains(other)) {
                    connected++;
                }
            }
            area -= connected;
        }
        System.err.println(area);
    }

    private static void part2(Set<Coord3D> cubes) {
        int minX = cubes.stream().map(Coord3D::x).min(Integer::compareTo).get() - 1;
        int minY = cubes.stream().map(Coord3D::y).min(Integer::compareTo).get() - 1;
        int minZ = cubes.stream().map(Coord3D::z).min(Integer::compareTo).get() - 1;

        int maxX = cubes.stream().map(Coord3D::x).max(Integer::compareTo).get() + 1;
        int maxY = cubes.stream().map(Coord3D::y).max(Integer::compareTo).get() + 1;
        int maxZ = cubes.stream().map(Coord3D::z).max(Integer::compareTo).get() + 1;


        Set<Coord3D> air = new HashSet<>();
        air.add(new Coord3D(minX, minY, minZ));
        Queue<Coord3D> q = new LinkedList<>();
        q.add(new Coord3D(minX, minY, minZ));

        int area = 0;
        while (!q.isEmpty()) {
            Coord3D current = q.poll();
            for (Coord3D next : current.potentialNeighbours()) {
                if (next.x() > maxX || next.y() > maxY || next.z() > maxZ || next.x() < minX || next.y() < minY || next.z() < minZ) {
                    continue;
                }
                if (cubes.contains(next)) {
                    area++;
                }
                if (air.contains(next) || cubes.contains(next)) {
                    continue;
                }
                q.add(next);
                air.add(next);
            }
        }
        System.err.println(area);
    }


}
