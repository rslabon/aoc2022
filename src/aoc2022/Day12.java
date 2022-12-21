package aoc2022;

import aoc2022.common.Coord;
import aoc2022.common.Dijkstra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Day12 {

    public static final int GOOL = Integer.MAX_VALUE;
    public static final int START = -1;

    public static void main(String[] args) throws Exception {
        String puzzleInput = Files.readString(Path.of("resources/day12.txt"));
//        int[][] grid = grid("Sabqponm\n" +
//                "abcryxxl\n" +
//                "accszExk\n" +
//                "acctuvwj\n" +
//                "abdefghi");
        int[][] grid = grid(puzzleInput);
        Coord E = null;
        Coord S = null;
        Map<Coord, List<Coord>> adj = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                int c = grid[i][j];
                if (c == GOOL) {
                    E = new Coord(i, j);
                }
                if (c == START) {
                    S = new Coord(i, j);
                }
                List<Coord> n = new ArrayList<>();
                if (!rowOutOfBands(i - 1, grid)) {
                    n.add(new Coord(i - 1, j));
                }
                if (!rowOutOfBands(i + 1, grid)) {
                    n.add(new Coord(i + 1, j));
                }
                if (!columnOutOfBands(j + 1, grid)) {
                    n.add(new Coord(i, j + 1));
                }
                if (!columnOutOfBands(j - 1, grid)) {
                    n.add(new Coord(i, j - 1));
                }
                adj.put(new Coord(i, j), n);
            }
        }

        Function<Coord, List<Coord>> neighbours = coord -> {
            int c = grid[coord.i()][coord.j()];
            return adj.get(coord).stream()
                    .filter(next -> {
                        int n = grid[next.i()][next.j()];
                        if (c == 'z' - 97) {
                            int v = n - c;
                            return n == GOOL || v <= 1;
                        } else {
                            int v = n - c;
                            return v <= 1;
                        }
                    })
                    .toList();
        };

        Dijkstra<Coord> dijkstra = new Dijkstra<>(neighbours);
        List<Coord> part1 = dijkstra.path(S, E);
        System.err.println("part1 = " + part1.size());


        List<Coord> startingPoints = new ArrayList<>(find(grid, 'a' - 97));
        startingPoints.add(S);
        List<Integer> part2 = new ArrayList<>();
        for (Coord startPoint : startingPoints) {
            int size = dijkstra.path(startPoint, E).size();
            if (size > 0) {
                part2.add(size);
            }
        }
        System.err.println("part2 = " + part2.stream().min(Integer::compareTo));

    }

    private static boolean rowOutOfBands(int row, int[][] grid) {
        int maxRow = grid.length;
        return row >= maxRow || row < 0;
    }

    private static boolean columnOutOfBands(int col, int[][] grid) {
        int maxCol = grid[0].length;
        return col >= maxCol || col < 0;
    }

    private static int[][] grid(String input) {
        String[] lines = input.split("\n");
        int[][] grid = new int[lines.length][lines[0].length()];
        int i = 0;
        int j = 0;
        for (String line : lines) {
            for (String cell : line.split("")) {
                if (cell.equals("S"))
                    grid[i][j] = START;
                else if (cell.equals("E"))
                    grid[i][j] = GOOL;
                else
                    grid[i][j] = cell.charAt(0) - 97;
                j++;
            }
            i++;
            j = 0;
        }
        return grid;
    }

    private static List<Coord> find(int[][] grid, int value) {
        List<Coord> result = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == value) {
                    result.add(new Coord(i, j));
                }
            }
        }
        return result;
    }
}
