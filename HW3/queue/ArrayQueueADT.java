// Это задание не проверялось преподавателем, файл может содержать ошибки

package queue;

import java.util.Objects;

/*
Model:
    • Очередь: a0 -> a1 -> ... -> a(n - 1)    {a0 - голова, a(n-1) - хвост}
    • n — размер очереди

Inv:
    • n >= 0
    • ∀ i ∈ [0;n) : a[i] != null
*/

public class ArrayQueueADT {
    private Object[] elements = new Object[1];
    private int head = 0, tail = 0;

    // Private Def:
    //     • prev(i) = (size(elements) - 1) if (index == 0) else (index - 1)
    //     • next(i) = (index + 1) % size(elements)

    // Private model:
    //     • elements — циклический массив элементов a0..a(n - 1)
    //     • head — голова очереди, elements[head] = a0
    //     • tail — невкл. хвост очереди, elements[prev(tail)] = a(n-1)

    // Private Inv:
    //     • size(elements) >= 1
    //     • tail, head ∈ [0;n)


    // Pred: index ∈ [0;n)
    // Post:
    //      Return: new_index
    //         • new_index ∈ [0;n)
    //         • new_index = prev(index)
    private static int prev(ArrayQueueADT queue, int index) {
        return (index > 0 ? index : queue.elements.length) - 1;
    }

    // Pred: index ∈ [0;n)
    // Post:
    //      Return: new_index
    //         • new_index ∈ [0;n)
    //         • new_index = next(index)
    private static int next(ArrayQueueADT queue, int index) {
        return (index + 1) % queue.elements.length;
    }

    // Pred: capacity >= 0
    // Post:
    //     • size(elements) >= capacity + 1
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        assert capacity >= 0;

        if (queue.elements.length - 1 < capacity) {
            Object[] newElements = new Object[2 * capacity];

            int newPos = 0;
            for (int pos = queue.head; pos != queue.tail; pos = next(queue, pos)) {
                newElements[newPos++] = queue.elements[pos];
            }

            queue.head = 0;
            queue.tail = newPos;
            queue.elements = newElements;
        }
    }

    // Pred: element != null
    // Post:
    //     • elements[tail] = element              (⇔ a[n] = element)
    //     • tail' = next(tail)                    (⇔ n += 1)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, size(queue) + 1);
        queue.elements[queue.tail] = element;
        queue.tail = next(queue, queue.tail);
    }

    // Pred: element != null
    // Post:
    //     • elements[prev(head)] = element        (⇔ a[1:n] = a[0:n-1], a[0] = element)
    //     • head' = prev(head)                    (⇔ n += 1)
    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, size(queue) + 1);
        queue.head = prev(queue, queue.head);
        queue.elements[queue.head] = element;
    }

    // Pred: n > 0
    // Post:
    //      Return: element
    //         • element = elements[head]          (⇔ a[0])
    public static Object element(ArrayQueueADT queue) {
        assert size(queue) > 0;

        return queue.elements[queue.head];
    }

    // Pred: n > 0
    // Post:
    //      Return: element
    //         • element = elements[prev(tail)]    (⇔ a[n-1])
    public static Object peek(ArrayQueueADT queue) {
        assert size(queue) > 0;

        return queue.elements[prev(queue, queue.tail)];
    }

    // Pred: n > 0
    // Post:
    //      Return: element
    //         • element = elements[head]          (⇔ a[0])
    //         • elements[head] = null             (⇔ del(a[0]) {a[0:n-2] = a[1:n-1], del(a[n-2])})
    //         • head' = next(head)                (⇔ n -= 1)
    public static Object dequeue(ArrayQueueADT queue) {
        assert size(queue) > 0;

        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = next(queue, queue.head);
        return result;
    }

    // Pred: n > 0
    // Post:
    //      Return: element
    //         • element = elements[prev(tail)]    (⇔ a[n-1])
    //         • elements[prev(tail)] = null       (⇔ del(a[n-1]))
    //         • tail' = prev(tail)                (⇔ n -= 1)
    public static Object remove(ArrayQueueADT queue) {
        assert size(queue) > 0;

        queue.tail = prev(queue, queue.tail);
        Object result = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        return result;
    }

    // Pred: True
    // Post:
    //      Return: n
    public static int size(ArrayQueueADT queue) {
        return queue.tail - queue.head + (queue.tail < queue.head ? queue.elements.length : 0);
    }

    // Pred: True
    // Post:
    //      Return: True if size == 0 else False   (⇔ True if n == 0 else False)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }

    // Pred: True
    // Post:
    //     • ∀ i ∈ [0;size(elements)) : elements[i] = null     (⇔ ∀ i ∈ [0;n) : a[i] = null)
    public static void clear(ArrayQueueADT queue) {
        for (int i = 0; i < queue.elements.length; i++) {
            queue.elements[i] = null;
        }
        queue.head = 0;
        queue.tail = 0;
    }

    // Pred: True
    // Post:
    //      Return: index
    //         • ∀ i ∈ [head;index) {step=next(i)} : elements[i] != element      (⇔ ∀ i ∈ [0;index) : a[i] != element)
    //         • elements[index] = element                                        (⇔ a[index] = element)
    //         • index = -1 if ∀ i ∈ [head;tail) {step=next(i)} : elements[i] != element
    //                                                                            (⇔ ∀ i ∈ [0;n) : a[i] != element)
    public static int indexOf(ArrayQueueADT queue, Object element) {
        for (int i = queue.head; i != queue.tail; i = next(queue, i)) {
            if (Objects.equals(queue.elements[i], element)) {
                return i - queue.head + (i < queue.head ? queue.elements.length : 0);
            }
        }
        return -1;
    }

    // Pred: True
    // Post:
    //      Return: index
    //         • ∀ i ∈ (index;tail) {step=next(i)} : elements[i] != element      (⇔ ∀ i ∈ (index;n) : a[i] != element)
    //         • elements[index] = element                                        (⇔ a[index] = element)
    //         • index = -1 if ∀ i ∈ [head;tail) {step=next(i)} : elements[i] != element
    //                                                                            (⇔ ∀ i ∈ [0;n) : a[i] != element)
    public static int lastIndexOf(ArrayQueueADT queue, Object element) {
        for (int i = prev(queue, queue.tail); i != prev(queue, queue.head); i = prev(queue, i)) {
            if (Objects.equals(queue.elements[i], element)) {
                return i - queue.head + (i < queue.head ? queue.elements.length : 0);
            }
        }
        return -1;
    }
}
