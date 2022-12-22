package aoc2022;


import aoc2022.common.Coord;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

enum Cell {
    OPEN, WALL, VOID
}

enum Direction {
    RIGHT(0, 1), LEFT(0, -1), UP(-1, 0), DOWN(1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    Direction turnRight() {
        return switch (this) {
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
            default -> RIGHT;
        };
    }

    Direction turnLeft() {
        return switch (this) {
            case RIGHT -> UP;
            case UP -> LEFT;
            case LEFT -> DOWN;
            default -> RIGHT;
        };
    }
}

record Position(int i, int j, Direction d) {
    public Position move(World world) {
        Position p = new Position(i + d.dx, j + d.dy, d);
        if (p.i < 0) {
            p = new Position(world.height() - 1, p.j, d);
        }
        if (p.j < 0) {
            p = new Position(p.i, world.width() - 1, d);
        }
        if (p.i >= world.height()) {
            p = new Position(0, p.j, d);
        }
        if (p.j >= world.width()) {
            p = new Position(p.i, 0, d);
        }
        return p;
    }

    public Position turnRight() {
        return new Position(i, j, d.turnRight());
    }

    public Position turnLeft() {
        return new Position(i, j, d.turnLeft());
    }
}

class World {
    private Cell[][] world;
    private List<Position> path = new ArrayList<>();

    public World(Cell[][] world) {
        this.world = world;
        this.path.add(findStartingPosition());
    }

    public static World parseWorld(String input) {
        String[] lines = input.split("\n");
        int width = Arrays.stream(lines).map(String::length).max(Integer::compareTo).get();
        int height = lines.length;
        Cell[][] world = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                world[i][j] = Cell.VOID;
            }
        }
        int i = 0;
        int j = 0;
        for (String line : lines) {
            for (String c : line.split("")) {
                world[i][j++] = switch (c) {
                    case " " -> Cell.VOID;
                    case "#" -> Cell.WALL;
                    default -> Cell.OPEN;
                };
            }
            i++;
            j = 0;
        }
        return new World(world);
    }

    public int width() {
        return world[0].length;
    }

    public int height() {
        return world.length;
    }

    public Position findStartingPosition() {
        for (int j = 0; j < width(); j++) {
            if (world[0][j] == Cell.OPEN) {
                return new Position(0, j, Direction.RIGHT);
            }
        }
        throw new IllegalStateException("No starting point!");
    }

    public Position currentPosition() {
        return path.get(path.size() - 1);
    }

    public Cell cellAt(Position p) {
        return world[p.i()][p.j()];
    }

    public void move(int n) {
        moveCurrentPosition(n);
    }

    public void turnRight() {
        setCurrentPosition(currentPosition().turnRight());
    }

    public void turnLeft() {
        setCurrentPosition(currentPosition().turnLeft());
    }

    public void execute(String instructions) {
        String val = "";
        for (char c : instructions.toCharArray()) {
            if (c == 'L') {
                move(Integer.parseInt(val));
                val = "";
                turnLeft();
            } else if (c == 'R') {
                move(Integer.parseInt(val));
                val = "";
                turnRight();
            } else {
                val += Character.toString(c);
            }
        }
        if (!val.isBlank()) {
            move(Integer.parseInt(val));
        }
    }

    public int finalPassword() {
        int row = 1 + currentPosition().i();
        int column = 1 + currentPosition().j();
        int face = switch (currentPosition().d()) {
            case RIGHT -> 0;
            case DOWN -> 1;
            case LEFT -> 2;
            case UP -> 3;
        };
        return 1000 * row + 4 * column + face;
    }

    private void moveCurrentPosition(int steps) {
        Position p = currentPosition();
        for (int step = 0; step < steps; ) {
            p = p.move(this);
            Cell c = cellAt(p);
            if (c == Cell.OPEN) {
                setCurrentPosition(p);
                step++;
            }
            if (c == Cell.WALL) {
                break;
            }
        }
    }

    private void setCurrentPosition(Position p) {
        path.add(p);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Map<Coord, Position> positionByCoords = new HashMap<>();
        for (Position p : path) {
            positionByCoords.put(new Coord(p.i(), p.j()), p);
        }

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                Cell c = world[i][j];
                Position position = positionByCoords.get(new Coord(i, j));
                if (position != null) {
                    sb.append(switch (position.d()) {
                        case UP -> "^";
                        case DOWN -> "v";
                        case LEFT -> "<";
                        case RIGHT -> ">";
                    });
                } else {
                    sb.append(switch (c) {
                        case VOID -> " ";
                        case WALL -> "#";
                        case OPEN -> ".";
                    });
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

public class Day22 {

    public static void main(String[] args) throws Exception {
        String puzzleInput = Files.readString(Path.of("resources/day22.txt"));
        String example = "        ...#\n" +
                "        .#..\n" +
                "        #...\n" +
                "        ....\n" +
                "...#.......#\n" +
                "........#...\n" +
                "..#....#....\n" +
                "..........#.\n" +
                "        ...#....\n" +
                "        .....#..\n" +
                "        .#......\n" +
                "        ......#.\n" +
                "\n" +
                "10R5L5R10L4R5L5";

        System.err.println(" part1=" + part1(puzzleInput));

    }

    private static int part1(String input) {
        String[] parts = input.split("\n\n");
        World world = World.parseWorld(parts[0]);
        System.err.println(world);
        System.err.println("\n****************************************************\n");
        world.execute(parts[1].trim());
        System.err.println(world);
        return world.finalPassword();
    }
}
