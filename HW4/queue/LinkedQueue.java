package queue;

import java.util.function.Predicate;
import java.util.Objects;


public class LinkedQueue extends AbstractQueue {

    private class Node {
        Object content;
        Node prev;
        Node next;

        Node(Object element, Node prev, Node next) {
            this.prev = prev;
            this.next = next;
            content = element;
        }
    }

    private Node head, tail;


    @Override
    public void enqueueImp(Object element) {
        tail = new Node(element, tail, null);
        if (head == null) {
            head = tail;
        } else {
            tail.prev.next = tail;
        }
    }

    @Override
    public void pushImp(Object element) {
        head = new Node(element, null, head);
        if (tail == null) {
            tail = head;
        } else {
            head.next.prev = head;
        }
    }

    @Override
    public Object elementImp() {
        return head.content;
    }

    @Override
    public Object peekImp() {
        return tail.content;
    }

    @Override
    public void dequeueImp() {
        if (head.next != null) head.next.prev = null;
        head = head.next;
    }

    @Override
    public void removeImp() {
        if (tail.prev != null) tail.prev.next = null;
        tail = tail.prev;
    }
}
