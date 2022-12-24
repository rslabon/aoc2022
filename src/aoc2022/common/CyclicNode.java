package aoc2022.common;


import java.util.Collection;
import java.util.List;

public class CyclicNode<T> {

    public T value;
    public CyclicNode<T> next;
    public CyclicNode<T> prev;

    public CyclicNode(T value) {
        this.value = value;
    }

    public static <T> CyclicNode<T> cyclicLinkedList(Collection<T> values) {
        List<CyclicNode<T>> nodes = values.stream().map(CyclicNode::new).toList();
        CyclicNode<T> prev = nodes.get(0);
        CyclicNode<T> head = prev;
        CyclicNode<T> tail = null;
        for (int i = 1; i < nodes.size(); i++) {
            CyclicNode<T> current = nodes.get(i);
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

    public CyclicNode<T> getNodeAtIndex(int index) {
        int i = index;
        CyclicNode<T> n = this;
        while (i > 0) {
            n = n.next;
            i--;
        }
        return n;
    }

    public void insertBefore(CyclicNode<T> otherNode) {
        CyclicNode<T> prev = otherNode.prev;
        CyclicNode<T> node = this;
        prev.next = node;
        node.prev = prev;
        otherNode.prev = node;
        node.next = otherNode;
    }

    public void insertAfter(CyclicNode<T> otherNode) {
        CyclicNode<T> next = otherNode.next;
        CyclicNode<T> node = this;
        otherNode.next = node;
        node.prev = otherNode;
        node.next = next;
        next.prev = node;
    }
}
