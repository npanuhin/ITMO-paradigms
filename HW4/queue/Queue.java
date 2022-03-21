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

    // Pred: element != null
    // Post:
    //     • a[n] = element
    //     • n += 1
    void enqueue(Object element);

    // Pred: element != null
    // Post:
    //     • a[1:n] = a[0:n-1], a[0] = element
    //     • n += 1
    void push(Object element);

    // Pred: n > 0
    // Post:
    //      Return: a[0]
    Object element();

    // Pred: n > 0
    // Post:
    //      Return: a[n-1]
    Object peek();

    // Pred: n > 0
    // Post:
    //      Return: a[0]
    //      • del(a[0])  {a[0:n-2] = a[1:n-1], del(a[n-2])}
    //      • n -= 1
    Object dequeue();

    // Pred: n > 0
    // Post:
    //      Return: a[n-1]
    //      • del(a[n-1])
    //      • n -= 1
    Object remove();

    // Pred: True
    // Post:
    //      Return: n
    int size();

    // Pred: True
    // Post:
    //      Return: True if n == 0 else False
    boolean isEmpty();

    // Pred: True
    // Post:
    //     • ∀ i ∈ [0;n) : a[i] = null
    void clear();

    // Pred: True
    // Post:
    //      • ∀ i ∈ [0;n) : del(a[i]) if not condition(a[i])
    //          Formal:
    //              a' = {a[i1], a[i2], ..., a[im]}
    //              where 0 <= i1 < i2 < ... < im < n
    //              and condition(a[i1], a[i2], ... a[im]) = True
    //              and condition(a[j] : j ∉ {i1, i2, ..., im}) = False
    void removeIf(Predicate<Object> condition);

    // Pred: True
    // Post:
    //      • ∀ i ∈ [0;n) : del(a[i]) if condition(a[i])
    //          Formal:
    //              a' = {a[i1], a[i2], ..., a[im]}
    //              where 0 <= i1 < i2 < ... < im < n
    //              and condition(a[i1], a[i2], ... a[im]) = False
    //              and condition(a[j] : j ∉ {i1, i2, ..., im}) = True
    void retainIf(Predicate<Object> condition);


    // Pred: True
    // Post:
    //      • Formal:
    //           a' = {a[0], a[1], ..., a[i - 1]}
    //           where 0 <= i <= n
    //           and condition(a[j] : 0 <= j < i) = True
    //           and condition(a[i]) = True
    void takeWhile(Predicate<Object> condition);
    // Pred: True
    // Post:
    //      • Formal:
    //           a' = {a[i], a[i + 1], ..., a[n - 1]}
    //           where 0 <= i <= n
    //           and condition(a[j] : 0 <= j < i) = True
    //           and condition(a[i]) = False
    void dropWhile(Predicate<Object> condition);
}
