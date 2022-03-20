package queue;

import java.util.function.Predicate;

/*
Model:
    • Очередь: a0 -> a1 -> ... -> a(n - 1)    {a0 - голова, a(n-1) - хвост}
    • n — размер очереди

Inv:
    • n >= 0
    • ∀ i ∈ [0;n) : a[i] != null
*/

public interface Queue {
    void enqueue(Object element);
    void push(Object element);

    Object element();
    Object peek();

    Object dequeue();
    Object remove();

    int size();
    boolean isEmpty();

    void clear();

    void removeIf(Predicate<Object> condition);
    void retainIf(Predicate<Object> condition);

    void takeWhile(Predicate<Object> condition);
    void dropWhile(Predicate<Object> condition);
}
