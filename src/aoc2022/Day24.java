package aoc2022;

import aoc2022.common.Coord;
import aoc2022.common.Direction;
import aoc2022.common.Perf;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


class Blizzard {
    int i;
    int j;
    Direction d;

    public Blizzard(int i, int j, Direction d) {
        this.i = i;
        this.j = j;
        this.d = d;
    }

    public void move(Valley valley) {
        i = i + d.dx;
        j = j + d.dy;
        if (i == 0) {
            i = valley.height() - 2;
        } else if (i == valley.height() - 1) {
            i = 1;
        }
        if (j == 0) {
            j = valley.width() - 2;
        } else if (j == valley.width() - 1) {
            j = 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blizzard blizzard = (Blizzard) o;
        return i == blizzard.i && j == blizzard.j && d == blizzard.d;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, d);
    }
}

class Valley {
    private final Coord expedition;

    private final Set<Blizzard> blizzards = new HashSet<>();
    private final Set<Coord> allOpen;
    private final Set<Coord> wall;

    private int width = -1;
    private int height = -1;

    public Valley(Set<Coord> allOpen, Set<Coord> wall) {
        this.allOpen = allOpen;
        this.wall = wall;
        this.expedition = new Coord(0, 1);
    }

    public static Valley parse(String input) {
        String[] lines = input.split("\n");
        int i = 0;
        int j = 0;
        Set<Coord> open = new HashSet<>();
        Set<Coord> wall = new HashSet<>();
        Valley valley = new Valley(open, wall);
        for (String line : lines) {
            for (String c : line.split("")) {
                if (c.equals("#")) {
                    wall.add(new Coord(i, j));
                } else {
                    open.add(new Coord(i, j));
                }
                Blizzard blizzard = switch (c) {
                    case ">" -> new Blizzard(i, j, Direction.RIGHT);
                    case "<" -> new Blizzard(i, j, Direction.LEFT);
                    case "^" -> new Blizzard(i, j, Direction.UP);
                    case "v" -> new Blizzard(i, j, Direction.DOWN);
                    default -> null;
                };
                if (blizzard != null) {
                    valley.blizzards.add(blizzard);
                }
                j++;
            }
            i++;
            j = 0;
        }
        return valley;
    }

    public Coord getExpedition() {
        return expedition;
    }

    public Coord getFinalTarget() {
        return new Coord(height() - 1, width() - 2);
    }

    public int travel(int minute, Coord start, Coord end, List<Set<Coord>> openPerMinute) {
        Queue<Coord> possibleSoFar = new LinkedList<>();
        possibleSoFar.add(start);
        int cycle = openPerMinute.size();
        while (true) {
            Set<Coord> possibleAtPresent = new HashSet<>();
            while (!possibleSoFar.isEmpty()) {
                Coord current = possibleSoFar.poll();
                Set<Coord> open = openPerMinute.get(minute % cycle);
                Set<Coord> nextMove = new HashSet<>(current.adj4());
                nextMove.add(current);
                nextMove.retainAll(open);
                if (nextMove.contains(end)) {
                    return minute;
                }
                possibleAtPresent.addAll(nextMove);
            }
            possibleSoFar.addAll(possibleAtPresent);
            minute++;
        }
    }

    public List<Set<Coord>> openPerMinute() {
        List<Set<Coord>> openPerMinute = new ArrayList<>();
        openPerMinute.add(openNow());

        for (int i = 0; i < width() * height(); i++) {
            for (Blizzard b : blizzards) {
                b.move(this);
            }
            openPerMinute.add(openNow());
        }
        return openPerMinute;
    }

    private Set<Coord> openNow() {
        Set<Coord> openNow = new HashSet<>(allOpen);
        openNow.removeAll(getBlizzardCoords());
        return openNow;
    }

    private Set<Coord> getBlizzardCoords() {
        return blizzards.stream().map(blizz -> new Coord(blizz.i, blizz.j)).collect(Collectors.toSet());
    }

    public int width() {
        if (width < 0) {
            width = wall.stream().map(Coord::j).max(Integer::compareTo).get() + 1;
        }
        return width;
    }

    public int height() {
        if (height < 0) {
            height = wall.stream().map(Coord::i).max(Integer::compareTo).get() + 1;
        }
        return height;
    }
}

public class Day24 {

    public static void main(String[] args) throws Exception {
        String example = "#.#####\n" +
                "#.....#\n" +
                "#>....#\n" +
                "#.....#\n" +
                "#...v.#\n" +
                "#.....#\n" +
                "#####.#";

        String largeExample = "#.######\n" +
                "#>>.<^<#\n" +
                "#.<..<<#\n" +
                "#>v.><>#\n" +
                "#<^v^^>#\n" +
                "######.#";

        String puzzleInput = Files.readString(Path.of("resources/day24.txt"));
        Valley valley = Valley.parse(puzzleInput);

        Perf.time(() -> {
            List<Set<Coord>> openPerMinute = valley.openPerMinute();

            int startToEnd = valley.travel(0, valley.getExpedition(), valley.getFinalTarget(), openPerMinute);
            System.err.println(" part1=" + startToEnd);

            int endToStart = valley.travel(startToEnd, valley.getFinalTarget(), valley.getExpedition(), openPerMinute);
            int finalToEnd = valley.travel(endToStart, valley.getExpedition(), valley.getFinalTarget(), openPerMinute);
            System.err.println(" part2=" + finalToEnd);
            return null;
        });
    }
}
