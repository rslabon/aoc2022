package aoc2022.common;

public record Range(int start, int end) {
    public boolean isValid() {
        return start <= end;
    }

    public int size() {
        return Math.abs(end - start);
    }

    public boolean intersect(Range other) {
        return other.start <= end;
    }
}
