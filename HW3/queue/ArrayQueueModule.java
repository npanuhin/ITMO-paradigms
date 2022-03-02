package queue;

import java.util.Objects;


public class ArrayQueueModule {
    private static int head = 0, tail = 0;
    private static Object[] elements = new Object[1];

    private static int prev(int index) {
        return (index > 0 ? index : elements.length) - 1;
    }

    private static int next(int index) {
        return (index + 1) % elements.length;
    }

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
        tail = next(tail);
    }

    public static void push(Object element) {
        assert element != null;

        ensureCapacity(size() + 1);
        head = prev(head);
        elements[head] = element;
    }

    public static Object element() {
        assert size() > 0;

        return elements[head];
    }

    public static Object peek() {
        assert size() > 0;

        return elements[prev(tail)];
    }

    public static Object dequeue() {
        assert size() > 0;

        Object result = elements[head];
        elements[head] = null;
        head = next(head);
        return result;
    }

    public static Object remove() {
        assert size() > 0;

        tail = prev(tail);
        Object result = elements[tail];
        elements[tail] = null;
        return result;
    }

    public static int size() {
        return tail - head + (tail < head ? elements.length : 0);
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

    public static int indexOf(Object element) {
        for (int i = head; i != tail; i = next(i)) {
            if (Objects.equals(elements[i], element)) {
                return i - head + (i < head ? elements.length : 0);
            }
        }
        return -1;
    }

    public static int lastIndexOf(Object element) {
        for (int i = prev(tail); i != prev(head); i = prev(i)) {
            if (Objects.equals(elements[i], element)) {
                return i - head + (i < head ? elements.length : 0);
            }
        }
        return -1;
    }
}
