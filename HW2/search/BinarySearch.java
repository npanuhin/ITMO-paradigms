// Warning! All comments in this file are outdated, incorrect, etc. New version is avaliable in BinarySearchUni.java modification
// Warning! Все комментарии в этом файле плохие, устаревшие, неправильные и т.п. Новая версия в модификации BinarySearchUni.java



// Общее:
//   * valid {smth} / валидное {что-то} — в соответствии с документацией Java
//   * "строчное представление" / "строка" — Непустая последовательность символов (char) без валидных пробельных символов
//   * size({что-то}) — Размер {чего-то} в общепринятом понимании или в соответствии с документацией Javaдля массивов ≥ 0
//                      Для массивов: size(arr) ∈ ℤ, size(arr) ≥ 0
//   * Нумерация с 0
//   * {varible}' — "Текущее значение {variable}", new_{variable} и old_{variable} по контексту


package search;


public class BinarySearch {

    // Pred: array — массив неубывающих целых чисел (int) (возможно пустой, но не null):
    //           * forall i = 0..(size(a) - 1): a[i] ∈ ℤ
    //           * forall i = 1..(size(a) - 1): a[i - 1] ≥ a[i]
    //       x — целое число (x ∈ ℤ)
    //
    // Post: Возвращено целое неотрицательное число res (a[i] ∈ ℤ, 0 ≤ a[i] ≤ size(array)) — минимальный индекс, при котором array[res] ≤ x,
    //       или size(array), если ∀ i: array[i] > x
    //
    public static int binsearchIterative(final int[] array, final int x) {
        int left = -1, right = array.length, middle;

        // Inv: -1 ≤ left < right ≤ size(array)
        while (right - left > 1) {

            // Inv && left + 1 < right
            middle = (left + right) / 2;
            // -1 ≤ left ≤ middle < right ≤ size(array)
            // Третье неравенство выполняется, поскольку left < right ⇒ (left + right) < right * 2 ⇒ (left + right) / 2 < right

            // -1 ≤ left ≤ middle < right ≤ size(array)
            if (array[middle] > x) {
                // -1 ≤ left ≤ middle < size(array)
                left = middle;
                // -1 ≤ left=middle < size(array)

            } else {
                // -1 ≤ middle < right ≤ size(array)
                right = middle;
                // -1 < middle=right ≤ size(array)
            }
            // Inv && (left < new_left || new_right < right)
        }
        // Inv && left + 1 = right (right - left = 1), middle не важен

        // 0 ≤ right ≤ size(array), т.к. Inv и left + 1 = right
        return right;
    }

    // Pred: array — массив неубывающих целых чисел (int) (возможно пустой, но не null):
    //           * forall i = 0..(size(a) - 1): a[i] ∈ ℤ
    //           * forall i = 1..(size(a) - 1): a[i - 1] ≥ a[i]
    //       x — целое число (x ∈ ℤ)
    //       left — целое число, больше или равно -1 и меньше size(array) (left ∈ ℤ, -1 ≤ left < size(array))
    //       right — целое неотрицательное число, меньше или равное size(array) (right ∈ ℤ, 0 ≤ right ≤ size(array))
    //           * Inv: -1 ≤ left < right ≤ size(array)
    //
    // Post: Возвращено целое неотрицательное число res (res ∈ ℤ, left < res ≤ right) — минимальный индекс
    //       из отрезка (left:right), при котором array[res] ≤ x, или size(array), если ∀ i: array[i] > x
    //
    private static int binsearchRecursiveInner(final int[] array, final int x, final int left, final int right) {
        // Inv
        if (right - left <= 1) {
            // 0 ≤ right ≤ size(array), т.к. left + 1 = right (right - left = 1) и Inv
            return right;
        }
        // Inv && left + 1 < right

        // Inv && left + 1 < right
        final int middle = (left + right) / 2;
        // -1 ≤ left ≤ middle < right ≤ size(array)
        // Третье неравенство выполняется, поскольку left < right ⇒ (left + right) < right * 2 ⇒ (left + right) / 2 < right

        // -1 ≤ left ≤ middle < right ≤ size(array)
        if (array[middle] > x) {
            // -1 ≤ left ≤ middle < size(array)
            return binsearchRecursiveInner(array, x, middle, right);  // (left = middle =>) -1 ≤ middle < right ≤ size(array)
        } else {
            // -1 ≤ middle < right ≤ size(array)
            return binsearchRecursiveInner(array, x, left, middle);  // (right = middle =>) -1 ≤ left ≤ middle < size(array)
        }
    }

    // Pred: array — массив неубывающих целых чисел (int) (возможно пустой, но не null):
    //           * forall i = 0..(size(a) - 1): a[i] ∈ ℤ
    //           * forall i = 1..(size(a) - 1): a[i - 1] ≥ a[i]
    //       x — целое число (x ∈ ℤ)
    //
    // Post: Возвращено целое неотрицательное число res (res ∈ ℤ, 0 ≤ res ≤ size(array)) — минимальный индекс, при котором array[res] ≤ x,
    //       или size(array), если ∀ i: array[i] > x
    //
    public static int binsearchRecursive(final int[] array, final int x) {
        return binsearchRecursiveInner(array, x, -1, array.length);
    }

    // Pred: args — массив строк (возможно пустой, но не null):
    //           *  a[0] — строчное представление валидного целого числа (repr(a[0]) ∈ ℤ, основание сс = 10)
    //           *  forall i = 1..(size(a) - 1): a[i] - строчное представление валидного целого числа (repr(a[i]) ∈ ℤ, основание сс = 10)
    //           *  Массив чисел b[int], такой что size(b) = size(a) и a[i] — строчное представление b[i], неубывает: forall i = 1..(size(b) - 1): b[i - 1] ≥ b[i]
    //
    // Post: На поток System.out подано строчное представление целого числа — ответа на задачу, и следующий за ним перевод строки
    //
    public static void main(String[] args) {

        final int x = Integer.parseInt(args[0]);

        final int[] array = new int[args.length - 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(args[i + 1]);
        }

        System.out.println(binsearchIterative(array, x));
        // System.out.println(binsearchRecursive(array, x));
    }
}
