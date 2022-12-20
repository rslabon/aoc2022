package aoc2022;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class Node {
    public long value;
    public Node next;
    public Node prev;

    public Node(long value) {
        this.value = value;
    }
}

public class Day20 {
    public static void main(String[] args) throws Exception {
        List<Integer> example = List.of(1, 2, -3, 3, -2, 0, 4);

        List<Integer> puzzleInput = Files.readAllLines(Path.of("resources/day20.txt"))
                .stream().map(Integer::parseInt).toList();

        System.err.println("part1=" + part1(example));
        System.err.println("part1=" + part1(puzzleInput));

        System.err.println("part1=" + part2(example));
        System.err.println("part1=" + part2(puzzleInput));
    }

    private static long part1(List<Integer> values) {
        List<Node> nodes = values.stream().map(Node::new).toList();
        Node zero = null;
        for (int i = 0; i < values.size(); i++) {
            if (nodes.get(i).value == 0) {
                zero = nodes.get(i);
            }
        }
        makeCyclicLinkedList(nodes);
        for (Node n : nodes) {
            move(n, nodes.size());
        }
        Node n1000 = getNode(zero, 1000 % nodes.size());
        Node n2000 = getNode(zero, 2000 % nodes.size());
        Node n3000 = getNode(zero, 3000 % nodes.size());

        return n1000.value + n2000.value + n3000.value;
    }

    private static long part2(List<Integer> values) {
        List<Node> nodes = values.stream()
                .map(v -> v * 811589153L)
                .map(Node::new)
                .toList();
        Node zero = null;
        for (int i = 0; i < values.size(); i++) {
            if (nodes.get(i).value == 0) {
                zero = nodes.get(i);
            }
        }
        Node list = makeCyclicLinkedList(nodes);
        for (int i = 0; i < 10; i++) {
            for (Node n : nodes) {
                move(n, nodes.size());
            }
        }
        Node n1000 = getNode(zero, 1000 % nodes.size());
        Node n2000 = getNode(zero, 2000 % nodes.size());
        Node n3000 = getNode(zero, 3000 % nodes.size());

        return n1000.value + n2000.value + n3000.value;
    }

    private static void print(Node n) {
        Set<Node> visited = new HashSet<>();
        List<Long> values = new ArrayList<>();
        while (!visited.contains(n)) {
            values.add(n.value);
            visited.add(n);
            n = n.next;
        }
        System.out.println(values);
    }

    private static Node getNode(Node node, int index) {
        int i = index;
        Node n = node;
        while (i > 0) {
            n = n.next;
            i--;
        }
        return n;
    }

    private static void move(Node node, int size) {
        long i = Math.abs(node.value) % (size - 1);
        while (i > 0) {
            moveByOne(node);
            i--;
        }
    }

    private static void moveByOne(Node node) {
        if (node.value == 0) {
            return;
        }
        Node myPrev = node.prev;
        Node myNext = node.next;
        node.prev = null;
        node.next = null;
        myPrev.next = myNext;
        myNext.prev = myPrev;
        if (node.value > 0) {
            insertAfter(node, myNext);
        } else {
            insertBefore(node, myPrev);
        }
    }

    private static void insertBefore(Node node, Node otherNode) {
        Node prev = otherNode.prev;
        prev.next = node;
        node.prev = prev;
        otherNode.prev = node;
        node.next = otherNode;
    }

    private static void insertAfter(Node node, Node otherNode) {
        Node next = otherNode.next;
        otherNode.next = node;
        node.prev = otherNode;
        node.next = next;
        next.prev = node;
    }

    private static Node makeCyclicLinkedList(List<Node> nodes) {
        Node prev = nodes.get(0);
        Node head = prev;
        Node tail = null;
        for (int i = 1; i < nodes.size(); i++) {
            Node current = nodes.get(i);
            prev.next = current;
            current.prev = prev;
            tail = current;
            prev = current;
        }
        if (tail != null) {
            tail.next = head;
            head.prev = tail;
        }
        return head;
    }
}
