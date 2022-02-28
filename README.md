<h1 align="center">Решения домашних заданий к курсу<br>«Парадигмы программирования»</h1>

> Оригинальный репозиторий с тестами: [kgeorgiy.info/paradigms-2022](https://www.kgeorgiy.info/git/geo/paradigms-2022/)

### Оглавление:
- [Домашнее задание 4. Очереди](#домашнее-задание-4-очереди)
- [Домашнее задание 3. Очередь на массиве](#домашнее-задание-3-очередь-на-массиве)
- [Домашнее задание 2. Бинарный поиск](#домашнее-задание-2-бинарный-поиск)
- [Домашнее задание 1. Обработка ошибок](#домашнее-задание-1-обработка-ошибок)

----------------------------------------------------------------------------------------------------

## Домашнее задание 4. Очереди

1. Определите интерфейс очереди Queue и опишите его контракт.
2. Реализуйте класс `LinkedQueue` — очередь на связном списке.
3. Выделите общие части классов `LinkedQueue` и `ArrayQueue` в базовый класс `AbstractQueue`.

Это домашнее задание _связано_ с предыдущим.


## Домашнее задание 3. Очередь на массиве

1. Определите модель и найдите инвариант структуры данных «[очередь»](http://ru.wikipedia.org/wiki/%D0%9E%D1%87%D0%B5%D1%80%D0%B5%D0%B4%D1%8C_(%D0%BF%D1%80%D0%BE%D0%B3%D1%80%D0%B0%D0%BC%D0%BC%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5)). Определите функции, которые необходимы для реализации очереди. Найдите их пред- и постусловия, при условии что очередь не содержит `null`.
2. Реализуйте классы, представляющие **циклическую** очередь на основе массива.
    - Класс `ArrayQueueModule` должен реализовывать один экземпляр очереди с использованием переменных класса.
    - Класс `ArrayQueueADT` должен реализовывать очередь в виде абстрактного типа данных (с явной передачей ссылки на экземпляр очереди).
    - Класс `ArrayQueue` должен реализовывать очередь в виде класса (с неявной передачей ссылки на экземпляр очереди).
    - Должны быть реализованы следующие функции (процедуры) / методы:
        - `enqueue` – добавить элемент в очередь;
        - `element` – первый элемент в очереди;
        - `dequeue` – удалить и вернуть первый элемент в очереди;
        - `size` – текущий размер очереди;
        - `isEmpty` – является ли очередь пустой;
        - `clear` – удалить все элементы из очереди.
    - Модель, инвариант, пред- и постусловия записываются в исходном коде в виде комментариев.
    - Обратите внимание на инкапсуляцию данных и кода во всех трех реализациях. 
3. Напишите тесты к реализованным классам. 


## Домашнее задание 2. Бинарный поиск

1. Реализуйте итеративный и рекурсивный варианты бинарного поиска в массиве.
2. На вход подается целое число `x` и массив целых чисел `a`, отсортированный по невозрастанию. Требуется найти минимальное значение индекса `i`, при котором `a[i] <= x`.
3. Для `main`, функций бинарного поиска и вспомогательных функций должны быть указаны, пред- и постусловия. Для реализаций методов должны быть приведены доказательства соблюдения контрактов в терминах троек Хоара.
4. Интерфейс программы.
    - Имя основного класса — `search.BinarySearch`.
    - Первый аргумент командной строки — число `x`.
    - Последующие аргументы командной строки — элементы массива `a`.
5. Пример запуска: `java search.BinarySearch 3 5 4 3 2 1`. Ожидаемый результат: `2`.


## Домашнее задание 1. Обработка ошибок

> Дубликат задания [ITMO-Java#13](https://github.com/npanuhin/ITMO-Java#домашнее-задание-13-обработка-ошибок). Решение и тесты в [ITMO-Java](https://github.com/npanuhin/ITMO-Java)

1. Добавьте в программу вычисляющую выражения обработку ошибок, в том числе:
    - ошибки разбора выражений;
    - ошибки вычисления выражений.
2. Для выражения `1000000*x*x*x*x*x/(x-1)` вывод программы должен иметь следующий вид:

    | **x** | **f**     |
    |-------|-----------|
    | 0     | 0         |
    | 2     | 32000000  |
    | 3     | 121500000 |
    | 4     | 341333333 |
    | 5     | overflow  |
    | 6     | overflow  |
    | 7     | overflow  |
    | 8     | overflow  |
    | 9     | overflow  |
    | 10    | overflow  |

    Результат `division by zero` (`overflow`) означает, что в процессе вычисления произошло деление на ноль (переполнение).
3. При выполнении задания следует обратить внимание на дизайн и обработку исключений.
4. Человеко-читаемые сообщения об ошибках должны выводится на консоль.
5. Программа не должна «вылетать» с исключениями (как стандартными, так и добавленными).
