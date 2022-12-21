package aoc2022.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CyclicArray<T> {

    private final T[] array;
    private final Map<T, Integer> toIndex = new HashMap<>();

    public CyclicArray(T[] array) {
        this.array = array;
        for (int i = 0; i < array.length; i++) {
            toIndex.put(array[i], i);
        }
    }

    public T get(int index) {
        return array[index % array.length];
    }

    public void moveRightAt(int index) {
        index = index % array.length;
        int nextIndex = (index + 1) % array.length;
        toIndex.put(array[index], nextIndex);
        toIndex.put(array[nextIndex], index);
        swap(index, nextIndex);
    }

    public void moveRightValue(T value) {
        moveRightAt(toIndex.get(value));
    }

    public void moveLeftAt(int index) {
        index = index % (array.length);
        int prevIndex = index == 0 ? array.length - 1 : (index - 1) % array.length;
        toIndex.put(array[index], prevIndex);
        toIndex.put(array[prevIndex], index);
        swap(prevIndex, index);
    }

    public void moveLeftValue(T value) {
        moveLeftAt(toIndex.get(value));
    }

    private void swap(int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    public static void main(String[] args) {
        CyclicArray<Integer> a = new CyclicArray<>(new Integer[]{1, 2, 3, 4, 5});
        System.err.println(a);
        System.err.println(a.get(0));
        System.err.println(a.get(1));
        System.err.println(a.get(2));
        System.err.println(a.get(3));
        System.err.println(a.get(4));
        System.err.println(a.get(5));

        a.moveRightValue(3);
        System.err.println(a);
        a.moveLeftValue(3);
        System.err.println(a);

        a.moveLeftAt(4);
        System.err.println(a);
        a.moveRightAt(4);
        System.err.println(a);

        a.moveRightAt(5);
        System.err.println(a);
        a.moveLeftAt(5);
        System.err.println(a);
    }
}
