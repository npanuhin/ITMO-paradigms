package queue;

import java.util.Objects;


public class ArrayQueue {
    private int head = 0, tail = 0;
    private Object[] elements = new Object[1];

    private int prev(int index) {
        return (index > 0 ? index : elements.length) - 1;
    }

    private int next(int index) {
        return (index + 1) % elements.length;
    }

    private void ensureCapacity(int capacity) {
        int size = size();

        if (elements.length - 1 < capacity) {
            Object[] new_elements = new Object[2 * capacity];

            if (tail < head) {
                for (int i = head; i < elements.length; i++) {
                    new_elements[i - head] = elements[i];
                }
                for (int i = 0; i < tail; i++) {
                    new_elements[i + (elements.length - head)] = elements[i];
                }
            } else {
                for (int i = head; i < tail; i++) {
                    new_elements[i - head] = elements[i];
                }
            }

            head = 0;
            tail = size;
            elements = new_elements;
        }
    }

    public void enqueue(Object element) {
        assert element != null;

        ensureCapacity(size() + 1);
        elements[tail] = element;
        tail = next(tail);
    }

    public void push(Object element) {
        assert element != null;

        ensureCapacity(size() + 1);
        elements[head] = element;
        head = prev(head);
    }

    public Object element() {
        assert size() > 0;

        return elements[head];
    }

    public Object peek() {
        assert size() > 0;

        return elements[prev(tail)];
    }

    public Object dequeue() {
        assert size() > 0;

        Object result = elements[head];
        elements[head] = null;
        head = next(head);
        return result;
    }

    public Object remove() {
        assert size() > 0;

        tail = prev(tail);
        Object result = elements[tail];
        elements[tail] = null;
        return result;
    }

    public int size() {
        return tail - head + (tail < head ? elements.length : 0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
    }

    public int indexOf(Object element) {
        for (int i = head; i != tail; i = next(i)) {
            if (Objects.equals(elements[i], element)) {
                return i - head + (i < head ? elements.length : 0);
            }
        }
        return -1;
    }

    public int lastIndexOf(Object element) {
        for (int i = prev(tail); i != prev(head); i = prev(i)) {
            if (Objects.equals(elements[i], element)) {
                return i - head + (i < head ? elements.length : 0);
            }
        }
        return -1;
    }
}
