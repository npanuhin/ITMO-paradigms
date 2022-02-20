package queue;

import java.util.Arrays;


public class ArrayQueueADT {
    private int head = 0, tail = 0;
    private Object[] elements = new Object[1];

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        int size = size(queue);

        if (queue.elements.length - 1 < capacity) {
            Object[] new_elements = new Object[2 * capacity];

            if (queue.tail < queue.head) {
                for (int i = queue.head; i < queue.elements.length; i++) {
                    new_elements[i - queue.head] = queue.elements[i];
                }
                for (int i = 0; i < queue.tail; i++) {
                    new_elements[i + (queue.elements.length - queue.head)] = queue.elements[i];
                }
            } else {
                for (int i = queue.head; i < queue.tail; i++) {
                    new_elements[i - queue.head] = queue.elements[i];
                }
            }

            queue.head = 0;
            queue.tail = size;

            queue.elements = new_elements;
        }
    }

    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, size(queue) + 1);
        queue.elements[queue.tail] = element;
        queue.tail = (queue.tail + 1) % queue.elements.length;
    }

    public static Object element(ArrayQueueADT queue) {
        assert size(queue) > 0;

        return queue.elements[queue.head];
    }

    public static Object dequeue(ArrayQueueADT queue) {
        assert size(queue) > 0;

        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elements.length;
        return result;
    }

    public static int size(ArrayQueueADT queue) {
        if (queue.head > queue.tail) {
            return queue.elements.length - queue.head + queue.tail;
        } else {
            return queue.tail - queue.head;
        }
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
}
