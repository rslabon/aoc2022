package aoc2022;

import aoc2022.common.Coord;
import aoc2022.common.Direction;

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
    private final Set<Coord> open;
    private final Set<Coord> wall;

    public Valley(Set<Coord> open, Set<Coord> wall) {
        this.open = open;
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

    public int travel(int step, Coord start, Coord end, List<Set<Coord>> openPerMinute) {
        Set<Coord> possible = new HashSet<>();
        possible.add(start);
        int cycle = openPerMinute.size();
        while (true) {
            Set<Coord> targets = new HashSet<>();
            for (Coord current : possible) {
                Set<Coord> safe = openPerMinute.get(step % cycle);
                Set<Coord> nextMove = new HashSet<>(current.adj4());
                nextMove.add(current);
                nextMove.retainAll(safe);
                if (nextMove.contains(end)) {
                    return step;
                }
                targets.addAll(nextMove);
            }
            possible = targets;
            step++;
        }
    }

    public List<Set<Coord>> openPerMinute() {
        List<Set<Coord>> safe = new ArrayList<>();
        Set<Coord> openNow = new HashSet<>(open);
        Set<Coord> blizzardCoords = blizzards.stream().map(blizz -> new Coord(blizz.i, blizz.j)).collect(Collectors.toSet());
        openNow.removeAll(blizzardCoords);
        safe.add(openNow);

        for (int i = 0; i < width() * height(); i++) {
            for (Blizzard b : blizzards) {
                b.move(this);
            }
            blizzardCoords = blizzards.stream().map(blizz -> new Coord(blizz.i, blizz.j)).collect(Collectors.toSet());
            openNow = new HashSet<>(open);
            openNow.removeAll(blizzardCoords);
            safe.add(openNow);
        }
        return safe;
    }

    public int width() {
        return wall.stream().map(Coord::j).max(Integer::compareTo).get() + 1;
    }

    public int height() {
        return wall.stream().map(Coord::i).max(Integer::compareTo).get() + 1;
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

        List<Set<Coord>> openPerMinute = valley.openPerMinute();

        int startToEnd = valley.travel(0, valley.getExpedition(), valley.getFinalTarget(), openPerMinute);
        System.err.println(" part1=" + startToEnd);

        int endToStart = valley.travel(startToEnd, valley.getFinalTarget(), valley.getExpedition(), openPerMinute);
        int finalToEnd = valley.travel(endToStart, valley.getExpedition(), valley.getFinalTarget(), openPerMinute);
        System.err.println(" part2=" + finalToEnd);
    }
}
