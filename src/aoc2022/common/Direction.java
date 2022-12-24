package aoc2022.common;

public enum Direction {
    RIGHT(0, 1), LEFT(0, -1), UP(-1, 0), DOWN(1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Direction turnRight() {
        return switch (this) {
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
            default -> RIGHT;
        };
    }

    public Direction turnLeft() {
        return switch (this) {
            case RIGHT -> UP;
            case UP -> LEFT;
            case LEFT -> DOWN;
            default -> RIGHT;
        };
    }
}
