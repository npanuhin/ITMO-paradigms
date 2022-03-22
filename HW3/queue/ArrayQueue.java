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

public class ArrayQueue {
    private Object[] elements = new Object[1];
    private int head = 0, tail = 0;

    // Private Def:
    //     • prev(i) = (size(elements) - 1) if (index == 0) else (index - 1)  циклически-предыдущий индекс
    //     • next(i) = (index + 1) % size(elements)                           циклически-следующий индекс

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
    private int prev(int index) {
        return (index > 0 ? index : elements.length) - 1;
    }

    // Pred: index ∈ [0;n)
    // Post:
    //      Return: new_index
    //         • new_index ∈ [0;n)
    //         • new_index = next(index)
    private int next(int index) {
        return (index + 1) % elements.length;
    }

    // Pred: capacity >= 0
    // Post:
    //     • size(elements) >= capacity + 1
    private void ensureCapacity(int capacity) {
        assert capacity >= 0;

        if (elements.length < capacity + 1) {
            Object[] newElements = new Object[2 * capacity];

            int newPos = 0;
            for (int pos = head; pos != tail; pos = next(pos)) {
                newElements[newPos++] = elements[pos];
            }

            // System.arraycopy(elements, head, newElements, 0, elements.length - head);
            // System.arraycopy(elements, 0, newElements, elements.length - head, head);

            head = 0;
            tail = newPos;
            elements = newElements;
        }
    }

    // Pred: element != null
    // Post:
    //     • elements[tail] = element           (⇔ a[n] = element)
    //     • tail' = next(tail)                 (⇔ n += 1)
    public void enqueue(Object element) {
        assert element != null;

        ensureCapacity(size() + 1);
        elements[tail] = element;
        tail = next(tail);
    }

    // Pred: element != null
    // Post:
    //     • elements[prev(head)] = element     (⇔ a[1:n] = a[0:n-1], a[0] = element)
    //     • head' = prev(head)                 (⇔ n += 1)
    public void push(Object element) {
        assert element != null;

        ensureCapacity(size() + 1);
        head = prev(head);
        elements[head] = element;
    }

    // Pred: n > 0
    // Post:
    //      Return: elements[head]              (⇔ a[0])
    public Object element() {
        assert size() > 0;

        return elements[head];
    }

    // Pred: n > 0
    // Post:
    //      Return: elements[prev(tail)]        (⇔ a[n-1])
    public Object peek() {
        assert size() > 0;

        return elements[prev(tail)];
    }

    // Pred: n > 0
    // Post:
    //      Return: elements[head]              (⇔ a[0])
    //      • elements[head] = null             (⇔ del(a[0]) {a[0:n-2] = a[1:n-1], del(a[n-2])})
    //      • head' = next(head)                (⇔ n -= 1)
    public Object dequeue() {
        assert size() > 0;

        Object result = elements[head];
        elements[head] = null;
        head = next(head);
        return result;
    }

    // Pred: n > 0
    // Post:
    //      Return: elements[prev(tail)]        (⇔ a[n-1])
    //      • elements[prev(tail)] = null       (⇔ del(a[n-1]))
    //      • tail' = prev(tail)                (⇔ n -= 1)
    public Object remove() {
        assert size() > 0;

        tail = prev(tail);
        Object result = elements[tail];
        elements[tail] = null;
        return result;
    }

    // Pred: True
    // Post:
    //      Return: n
    public int size() {
        return tail - head + (tail < head ? elements.length : 0);
    }

    // Pred: True
    // Post:
    //      Return: True if size == 0 else False   (⇔ True if n == 0 else False)
    public boolean isEmpty() {
        return size() == 0;
    }

    // Pred: True
    // Post:
    //     • ∀ i ∈ [0;size(elements)) : elements[i] = null     (⇔ ∀ i ∈ [0;n) : a[i] = null)
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
    }

    // Pred: True
    // Post:
    //      Return: index
    //         • ∀ i ∈ [head;index) {step=next(i)} : elements[i] != element      (⇔ ∀ i ∈ [0;index) : a[i] != element)
    //         • elements[index] = element                                        (⇔ a[index] = element)
    //         • index = -1 if ∀ i ∈ [head;tail) {step=next(i)} : elements[i] != element
    //                                                                            (⇔ ∀ i ∈ [0;n) : a[i] != element)
    public int indexOf(Object element) {
        for (int i = head; i != tail; i = next(i)) {
            if (Objects.equals(elements[i], element)) {
                return i - head + (i < head ? elements.length : 0);
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
    public int lastIndexOf(Object element) {
        for (int i = prev(tail); i != prev(head); i = prev(i)) {
            if (Objects.equals(elements[i], element)) {
                return i - head + (i < head ? elements.length : 0);
            }
        }
        return -1;
    }
}
