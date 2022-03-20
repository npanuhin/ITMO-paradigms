// Общее:
//   * valid {smth} / валидное {что-то} — в соответствии с документацией Java, **в т.ч. если это не указано**
//   * "строковое представление" / "строка" — Непустая последовательность символов (char) без пробельных символов
//   * size({что-то}) — Размер {чего-то} в общепринятом понимании или в соответствии с документацией Java
//                      Для массивов: size(arr) ∈ ℤ, size(arr) ≥ 0
//   * Нумерация (в т.ч. индексы массивов) с 0


package search;

public class BinarySearchUni {

    // Pred:
    //      * array[int], size(array) > 0
    //      * ∃ res ∈ [0;size(array)):
    //          * ∀ i ∈ [0;res - 2]: array[i] > array[i + 1]
    //          * ∀ i ∈ [res;size(array) - 2]: array[i] < array[i + 1]
    //
    // Post:
    //      * Return: int res, удовлетворяющий условию Pred
    //
    public static int binsearchIterative(final int[] array) {
        int left = -1, right = array.length - 1, middle;
        // left = -1, right = size(array) - 1, middle — undefined

        // Inv до конца функции:
        //      * -1 ≤ left ≤(2) middle (may be undefined) ≤(3) right < size(array)
        //      * left < res ≤ right
        //          Док-во в данный момент:
        //          * left = -1 < 0 ≤ res ⇒ left < res
        //          * res < size(array) ⇒ res ≤ size(array) - 1 = right ⇒ res ≤ right

        // Длина отрезка [left:right] на каждом шаге уменьшается (док-во в конце цикла) ⇒
        // ⇒ right - left -> 0 ⇒ цикл заканчивается
        while (right - left > 1) {
            // Inv && right - left > 1

            // Inv && right - left ≥ 2
            middle = (left + right) / 2;  // left + (right - left) // 2
            // Inv && right - left ≥ 2 && left < middle < right
            // Док-во left <(1) middle <(2) right (и Inv) в данный момент:
            //      Неравенства (1) и (2) выполняются, поскольку right - left ≥ 2:
            //      1: (right - left) // 2 ≥ 1 ⇒ left + (right - left) // 2 > left ⇒ left < middle
            //      2: left + (right - left) // 2 ≤ left + (right - left) / 2 = (left + right) / 2 <
            //          < (right + right) / 2 = right ⇒ middle < right

            // Inv && left < middle < right
            if (array[middle] >= array[middle + 1]) {  // middle < right < size(array) ⇒ middle + 1 < size(array)
                // Inv && left < middle < right && array[middle] ≥ array[middle + 1]
                left = middle;
                // Док-во Inv(left < res):
                //      -1 ≤ left=middle < size(array) && array[left] ≥ array[left + 1] и array монотонно убывает,
                //      затем возрастает (см. Pred) ⇒ нер-во выполняется для ∀ i ≤ left ⇒ left < res (Inv)

            } else {
                // Inv && left < middle < right && array[middle] < array[middle + 1]
                right = middle;
                // Док-во Inv(res ≤ right):
                //      -1 < middle=right < size(array) && array[right] < array[right + 1] и array монотонно убывает,
                //      затем возрастает (см. Pred) ⇒ нер-во выполняется для ∀ i ≥ right ⇒ res ≥ right (Inv)
            }
            // Inv && (old_left < new_left || new_right < old_right),
            // то есть отрезок [new_left:new_right] меньше [left:right] ⇒ цикл заканчивается
        }
        // Inv && right - left = 1

        // Inv && left + 1 = right
        // Поскольку left < res ≤ right и left + 1 = right: res = right
        return right;
    }

    // Pred:
    //      * array[int], size(array) > 0
    //      * int left, right:
    //          * left, right ∈ ℤ
    //          * -1 ≤ left < right < size(array)
    //      * ∃ res ∈ (left;right]:
    //          * ∀ i ∈ [0;res - 2]: array[i] > array[i + 1]
    //          * ∀ i ∈ [res;size(array) - 2]: array[i] < array[i + 1]
    //
    // Inv рекурсии: -1 ≤ left < res ≤ right < size(array)
    //      Док-во изначально (по Pred):
    //      -1 ≤ left < right < size(array) и ∃ res ∈ (left;right] ⇒ -1 ≤ left < res ≤ right < size(array)
    //
    // Условие выхода из рекурсии: Длина отрезка [left:right] на каждом шаге уменьшается ⇒
    //      ⇒ right - left -> 0 ⇒ рекурсия закончится на `if (right - left <= 1)`
    //
    // Post:
    //      * Return: int res, удовлетворяющий условию Pred
    //
    private static int binsearchRecursiveInner(final int[] array, final int left, final int right) {
        // Inv
        if (right - left <= 1) {
            // Inv && right - left = 1 ⇒ left + 1 = right
            // Поскольку left < res ≤ right и left + 1 = right: res = right
            return right;
        }
        // Inv && right - left > 1

        // Inv && right - left ≥ 2
        final int middle = (left + right) / 2;
        // Доп. Inv до конца функции:
        //      * -1 ≤ left < middle < right < size(array)

        // Inv && right - left ≥ 2 && left < middle < right
        // Док-во left < middle < right в данный момент:
        //      Неравенства выполняются, поскольку right - left ≥ 2:
        //      1: (right - left) // 2 ≥ 1 ⇒ left + (right - left) // 2 > left ⇒ left < middle
        //      2: left + (right - left) // 2 ≤ left + (right - left) / 2 = (left + right) / 2 <
        //          < (right + right) / 2 = right ⇒ middle < right

        // Inv && left < middle < right
        if (array[middle] >= array[middle + 1]) {  // middle < right < size(array) ⇒ middle + 1 < size(array)
            // Inv && left < middle < right && array[middle] ≥ array[middle + 1]

            // Док-во Inv(left < res) для вызова binsearchRecursiveInner:  new_left = middle
            //      -1 ≤ new_left=middle < size(array) && array[new_left] ≥ array[new_left + 1]
            //      и array монотонно убывает, затем возрастает (см. Pred) ⇒
            //      нер-во выполняется для ∀ i ≤ new_left ⇒ new_left < res (Inv)
            return binsearchRecursiveInner(array, middle, right);

        } else {
            // Inv && left < middle < right && array[middle] < array[middle + 1]

            // Док-во Inv(res ≤ right) для вызова binsearchRecursiveInner:  new_right = middle
            //      -1 < middle=new_right < size(array) && array[new_right] < array[new_right + 1]
            //      и array монотонно убывает, затем возрастает (см. Pred) ⇒
            //      ⇒ нер-во выполняется для ∀ i ≥ new_right ⇒ res ≥ new_right (Inv)
            return binsearchRecursiveInner(array, left, middle);
        }
    }

    // Pred:
    //      * array[int], size(array) > 0
    //      * ∃ res ∈ [0;size(array)):
    //          * ∀ i ∈ [0;res - 2]: array[i] > array[i + 1]
    //          * ∀ i ∈ [res;size(array) - 2]: array[i] < array[i + 1]
    //
    // Post:
    //      * Return: int res, удовлетворяющий условию Pred
    //
    public static int binsearchRecursive(final int[] array) {
        // left = -1, right = size(array) - 1 ⇒ 1 ≤ left < res ≤ right < size(array)
        // Cл. выполняется Pred(binsearchRecursiveInner)
        return binsearchRecursiveInner(array, -1, array.length - 1);
    }

    // Pred:
    //      * args[String], size(args) > 0
    //      * ∀ x ∈ args: x - строковое представление целого числа int, основание системы счисления: 10
    //      * ∃ есть массив чисел b[int], такой что size(b) = size(args), args[i] — строковое представление b[i] и:
    //          * ∃ res ∈ [0;size(b)):
    //              * ∀ i ∈ [0;res - 2]: b[i] ≥ b[i + 1]
    //              * ∀ i ∈ [res;size(b) - 2]: b[i] ≤ b[i + 1]
    //
    // Post:
    //      На поток System.out подано строковое представление числа (int) — ответа на задачу, затем перевод строки
    //      * Ответ на задачу: int res ∈ [0;size(array)), удовлетворяющий условию Pred
    //
    public static void main(String[] args) {

        final int[] array = new int[args.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(args[i]);
        }

        System.out.println(binsearchIterative(array));
        // System.out.println(binsearchRecursive(array));
    }
}
