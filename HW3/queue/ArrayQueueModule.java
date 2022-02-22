package queue;

import java.util.Arrays;


public class ArrayQueueModule {
    private static int head = 0, tail = 0;
    private static Object[] elements = new Object[1];

    private static void ensureCapacity(int capacity) {
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

    public static void enqueue(Object element) {
        assert element != null;

        ensureCapacity(size() + 1);
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    public static void push(Object element) {
        assert element != null;

        ensureCapacity(size() + 1);
        elements[head] = element;
        head = (head > 0 ? head - 1 else element.length);
    }

    public static Object element() {
        assert size() > 0;

        return elements[head];
    }

    public static Object dequeue() {
        assert size() > 0;

        Object result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    public static int size() {
        if (head > tail) {
            return elements.length - head + tail;
        } else {
            return tail - head;
        }
    }

    public static boolean isEmpty() {
        return size() == 0;
    }

    public static void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
    }
}
