package queue;

import java.util.Arrays;


public class ArrayQueue {
    private int head = 0, tail = 0;
    private Object[] elements = new Object[1];

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
        tail = (tail + 1) % elements.length;
    }

    public Object element() {
        assert size() > 0;

        return elements[head];
    }

    public Object dequeue() {
        assert size() > 0;

        Object result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    public int size() {
        if (head > tail) {
            return elements.length - head + tail;
        } else {
            return tail - head;
        }
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
}
