// Общее:
//   * valid {smth} / валидное {что-то} — в соответствии с документацией Java
//   * "строковое представление" / "строка" — Непустая последовательность символов (char) без пробельных символов
//   * size({что-то}) — Размер {чего-то} в общепринятом понимании или в соответствии с документацией Java
//                      Для массивов: size(arr) ∈ ℤ, size(arr) ≥ 0
//   * Нумерация с 0


package search;


public class BinarySearchUni {

    // Pred: array — непустой массив целых чисел (int):
    //      * forall i = 0..(size(array) - 1): args[i] ∈ ℤ
    //      * ∃ res ∈ [0:size(array)), такой, что:
    //          * forall i = 0..(res - 2): args[i] ≥ args[i + 1]
    //          * forall i = res..(size(array) - 2): args[i] ≤ args[i + 1]
    //
    // Post: Возвращено число res (int):
    //      * 0 ≤ res < size(array)
    //      * res удовлетворяет условию Pred
    //
    public static int binsearch_iterative(final int[] array) {
        int left = -1, right = array.length - 1, middle;
        // left = -1, right = size(array) - 1, middle — undefined

        // Inv: -1 ≤ left < middle (may be undefined) < right < size(array)
        //      * left < res
        //      * right >= res
        // Длина отрезка [left:right] на каждом шаге уменьшается (доказано позже) ⇒ right - left -> 0 ⇒ цикл заканчивается
        while (right - left > 1) {
            // Inv && left + 1 < right

            // Inv && left + 1 < right
            middle = (left + right) / 2;
            // Inv [-1 ≤ left < middle < right < size(array)]
            // Второе неравенство Inv выполняется, поскольку left < right ⇒ left * 2 < (left + right) ⇒ left < (left + right) / 2
            // Третье неравенство Inv выполняется, поскольку left < right ⇒ (left + right) < right * 2 ⇒ (left + right) / 2 < right

            // Inv
            if (array[middle] >= array[middle + 1]) {
                // Inv && array[middle] >= array[middle + 1]
                left = middle;
                // -1 ≤ left=middle < size(array) && array[left] >= array[left + 1] ⇒ left < res

            } else {
                // -1 ≤ middle < right < size(array) && array[middle] < array[middle + 1]  (right < size(array))
                right = middle;
                // -1 < middle=right < size(array) && array[right] < array[right + 1]  (right < size(array)) ⇒ res <= right
            }
            // Inv && (left < new_left || new_right < right), то есть отрезок [new_left:new_right] меньше [left:right]

        }
        // Inv && left + 1 = right (right - left = 1), middle не важен

        // Inv && left + 1 = right
        // Поскольку 0 ≤ right < size(array), left < res, res <= right и left + 1 = right:
        //      res = right
        return right;
    }

    // Pred: array — непустой массив целых чисел (int):
    //          * forall i = 0..(size(array) - 1): args[i] ∈ ℤ
    //          * ∃ res ∈ [0:size(array)), такой, что:
    //              * forall i = 0..(res - 2): args[i] ≥ args[i + 1]
    //              * forall i = res..(size(array) - 2): args[i] ≤ args[i + 1]
    //       left — целое число (int), left ∈ ℤ, -1 ≤ left < size(array)
    //       right — целое неотрицательное число (int), right ∈ ℤ, 0 ≤ right < size(array)
    //
    // Inv: -1 ≤ left < middle (may be undefined) < right < size(array)
    //      * left < res
    //      * right >= res
    //
    // Условие выхода из рекурсии: Длина отрезка [left:right] на каждом шаге уменьшается ⇒ right - left -> 0 ⇒ цикл заканчивается
    //
    // Post: Возвращено число res (int):
    //      * 0 ≤ res < size(array)
    //      * res удовлетворяет условию Pred
    //
    private static int binsearch_recursive_inner(final int[] array, final int left, final int right) {
        // Inv
        if (right - left <= 1) {
            // Inv && left + 1 = right
            // Поскольку 0 ≤ right < size(array), left < res, res <= right и left + 1 = right:
            //      res = right
            return right;
        }
        // Inv && left + 1 < right

        // Inv && left + 1 < right
        final int middle = (left + right) / 2;
        // Inv [-1 ≤ left < middle < right < size(array)]
        // Второе неравенство Inv выполняется, поскольку left < right ⇒ left * 2 < (left + right) ⇒ left < (left + right) / 2
        // Третье неравенство Inv выполняется, поскольку left < right ⇒ (left + right) < right * 2 ⇒ (left + right) / 2 < right

        // Inv
        if (array[middle] >= array[middle + 1]) {
            // Inv && array[middle] >= array[middle + 1]
            //
            // Для вызова binsearch_recursive_inner:
            //      -1 ≤ left=middle < size(array) && array[left] >= array[left + 1] ⇒ left < res
            return binsearch_recursive_inner(array, middle, right);
        } else {
            // -1 ≤ middle < right < size(array) && array[middle] < array[middle + 1]  (right < size(array))
            //
            // Для вызова binsearch_recursive_inner:
            //      -1 < middle=right < size(array) && array[right] < array[right + 1]  (right < size(array)) ⇒ res <= right
            return binsearch_recursive_inner(array, left, middle);
        }
    }

    // Pred: array — непустой массив целых чисел (int):
    //          * forall i = 0..(size(array) - 1): args[i] ∈ ℤ
    //          * ∃ res ∈ [0:size(array)), такой, что:
    //              * forall i = 0..(res - 2): args[i] ≥ args[i + 1]
    //              * forall i = res..(size(array) - 2): args[i] ≤ args[i + 1]
    //
    // Post: Возвращено число res (int):
    //      * 0 ≤ res < size(array)
    //      * res удовлетворяет условию Pred
    //
    public static int binsearch_recursive(final int[] array) {
        return binsearch_recursive_inner(array, -1, array.length - 1);
    }

    // Pred: args — непустой массив строк:
    //      * forall x in args: x - строковое представление валидного целого числа (repr(args[i]) ∈ ℤ, основание сс = 10)
    //      * ∃ есть массив чисел b[int], такой что size(b) = size(args) и args[i] — строковое представление b[i]
    //      * ∃ res ∈ [0:size(b)), такой, что:
    //          * forall i = 0..(res - 2): b[i] ≥ b[i + 1]
    //          * forall i = res..(size(b) - 2): b[i] ≤ b[i + 1]
    //
    // Post: На поток System.out подано строковое представление целого числа (int) — ответа на задачу, и следующий за ним перевод строки
    //      * Ответ на задачу: неотрицательное число res (res ∈ ℤ, 0 ≤ res < right), удовлетворяющее Pred
    //
    public static void main(String[] args) {

        final int[] array = new int[args.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(args[i]);
        }

        System.out.println(binsearch_iterative(array));
        // System.out.println(binsearch_recursive(array));
    }
}
