package aoc2022;

import aoc2022.common.Coord;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class Day23World {

    private Set<Coord> elfsPositions;
    private static final Coord N = new Coord(-1, 0);
    private static final Coord NW = new Coord(-1, -1);
    private static final Coord NE = new Coord(-1, +1);
    private static final Coord S = new Coord(+1, 0);
    private static final Coord SW = new Coord(+1, -1);
    private static final Coord SE = new Coord(+1, +1);
    private static final Coord W = new Coord(0, -1);
    private static final Coord E = new Coord(0, +1);

    private static final List<Coord> NORTH = List.of(N, NW, NE);
    private static final List<Coord> SOUTH = List.of(S, SW, SE);
    private static final List<Coord> WEST = List.of(W, NW, SW);
    private static final List<Coord> EAST = List.of(E, NE, SE);

    private List<List<Coord>> directions;

    public Day23World(Set<Coord> elfsPositions) {
        this.elfsPositions = elfsPositions;
        this.directions = List.of(NORTH, SOUTH, WEST, EAST);
    }

    public static Day23World parse(String input) {
        int i = 0;
        int j = 0;
        Set<Coord> elfsPositions = new HashSet<>();
        for (String line : input.split("\n")) {
            for (String cell : line.split("")) {
                if (cell.equals("#")) {
                    elfsPositions.add(new Coord(i, j));
                }
                j++;
            }
            i++;
            j = 0;
        }
        return new Day23World(elfsPositions);
    }

    public boolean makeRound() {
        Map<Coord, List<Coord>> stepToElfs = new HashMap<>();
        for (Coord elf : elfsPositions) {
            boolean noElfsAround = canMove(elf.adj());
            if (noElfsAround) {
                stepToElfs.put(elf, List.of(elf));
            } else {
                Coord step = proposeStep(elf, directions);
                List<Coord> elfs = stepToElfs.getOrDefault(step, new ArrayList<>());
                elfs.add(elf);
                stepToElfs.put(step, elfs);
            }
        }

        Set<Coord> newElfsPositions = new HashSet<>();
        for (Map.Entry<Coord, List<Coord>> each : stepToElfs.entrySet()) {
            if (each.getValue().size() > 1) {
                newElfsPositions.addAll(each.getValue());
            } else {
                newElfsPositions.add(each.getKey());
            }
        }

        directions = new ArrayList<>(directions);
        List<Coord> first = directions.remove(0);
        directions.add(first);

        boolean noMove = elfsPositions.equals(newElfsPositions);
        elfsPositions = newElfsPositions;
        return noMove;
    }

    private Coord proposeStep(Coord elf, List<List<Coord>> direction) {
        for (List<Coord> d : direction) {
            List<Coord> propositions = d.stream().map(c -> new Coord(elf.i() + c.i(), elf.j() + c.j())).toList();
            if (canMove(propositions)) {
                return propositions.get(0);
            }
        }
        return elf;
    }

    private boolean canMove(Collection<Coord> positions) {
        return positions.stream().noneMatch(p -> elfsPositions.contains(p));
    }

    public int countEmptyRegions() {
        int minRow = elfsPositions.stream().map(Coord::i).min(Integer::compareTo).get();
        int maxRow = elfsPositions.stream().map(Coord::i).max(Integer::compareTo).get();
        int minCol = elfsPositions.stream().map(Coord::j).min(Integer::compareTo).get();
        int maxCol = elfsPositions.stream().map(Coord::j).max(Integer::compareTo).get();
        int count = 0;
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                if (!elfsPositions.contains(new Coord(i, j))) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        int minRow = elfsPositions.stream().map(Coord::i).min(Integer::compareTo).get();
        int maxRow = elfsPositions.stream().map(Coord::i).max(Integer::compareTo).get();
        int minCol = elfsPositions.stream().map(Coord::j).min(Integer::compareTo).get();
        int maxCol = elfsPositions.stream().map(Coord::j).max(Integer::compareTo).get();
        StringBuilder sb = new StringBuilder();
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                if (elfsPositions.contains(new Coord(i, j))) {
                    sb.append("#");
                } else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}


public class Day23 {
    public static void main(String[] args) throws Exception {
        String example = ".....\n" +
                "..##.\n" +
                "..#..\n" +
                ".....\n" +
                "..##.\n" +
                ".....";

        String largeExample = "..............\n" +
                "..............\n" +
                ".......#......\n" +
                ".....###.#....\n" +
                "...#...#.#....\n" +
                "....#...##....\n" +
                "...#.###......\n" +
                "...##.#.##....\n" +
                "....#..#......\n" +
                "..............\n" +
                "..............\n" +
                "..............";

        String puzzleInput = Files.readString(Path.of("resources/day23.txt"));

        Day23World world = Day23World.parse(puzzleInput);
        System.err.println("\n\n" + world);

        System.err.println(" part1=" + part1(world));
        System.err.println(" part2=" + part2(world));
    }

    private static int part1(Day23World world) {
        for (int i = 0; i < 10; i++) {
            world.makeRound();
        }
        return world.countEmptyRegions();
    }

    private static int part2(Day23World world) {
        for (int i = 0; i < 1_000_000; i++) {
            if (world.makeRound()) {
                return i + 1;
            }
        }
        return -1;
    }
}
