package queue;

import java.util.Objects;


public class ArrayQueueADT {
    private int head = 0, tail = 0;
    private Object[] elements = new Object[1];

    private static int prev(ArrayQueueADT queue, int index) {
        return (index > 0 ? index : queue.elements.length) - 1;
    }

    private static int next(ArrayQueueADT queue, int index) {
        return (index + 1) % queue.elements.length;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (queue.elements.length - 1 < capacity) {
            Object[] new_elements = new Object[2 * capacity];

            int new_pos = 0;
            for (int pos = queue.head; pos != queue.tail; pos = next(queue, pos)) {
                new_elements[new_pos++] = queue.elements[pos];
            }

            queue.head = 0;
            queue.tail = new_pos;
            queue.elements = new_elements;
        }
    }

    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, size(queue) + 1);
        queue.elements[queue.tail] = element;
        queue.tail = next(queue, queue.tail);
    }

    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, size(queue) + 1);
        queue.head = prev(queue, queue.head);
        queue.elements[queue.head] = element;
    }

    public static Object element(ArrayQueueADT queue) {
        assert size(queue) > 0;

        return queue.elements[queue.head];
    }

    public static Object peek(ArrayQueueADT queue) {
        assert size(queue) > 0;

        return queue.elements[prev(queue, queue.tail)];
    }

    public static Object dequeue(ArrayQueueADT queue) {
        assert size(queue) > 0;

        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = next(queue, queue.head);
        return result;
    }

    public static Object remove(ArrayQueueADT queue) {
        assert size(queue) > 0;

        queue.tail = prev(queue, queue.tail);
        Object result = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        return result;
    }

    public static int size(ArrayQueueADT queue) {
        return queue.tail - queue.head + (queue.tail < queue.head ? queue.elements.length : 0);
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }

    public static void clear(ArrayQueueADT queue) {
        for (int i = 0; i < queue.elements.length; i++) {
            queue.elements[i] = null;
        }
        queue.head = 0;
        queue.tail = 0;
    }

    public static int indexOf(ArrayQueueADT queue, Object element) {
        for (int i = queue.head; i != queue.tail; i = next(queue, i)) {
            if (Objects.equals(queue.elements[i], element)) {
                return i - queue.head + (i < queue.head ? queue.elements.length : 0);
            }
        }
        return -1;
    }

    public static int lastIndexOf(ArrayQueueADT queue, Object element) {
        for (int i = prev(queue, queue.tail); i != prev(queue, queue.head); i = prev(queue, i)) {
            if (Objects.equals(queue.elements[i], element)) {
                return i - queue.head + (i < queue.head ? queue.elements.length : 0);
            }
        }
        return -1;
    }
}
