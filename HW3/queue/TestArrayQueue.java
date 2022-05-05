// Это задание не проверялось преподавателем, файл может содержать ошибки

package queue;

public class TestArrayQueue {

    public static void fill(ArrayQueue queue, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("enqueue(" + i + ")");
            queue.enqueue(i);
        }
    }

    public static void rfill(ArrayQueue queue, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("push(" + i + ")");
            queue.push(i);
        }
    }

    public static void dump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(
                "size = " + queue.size() + "\t" +
                "element = " + queue.element() + "\t" +
                "dequeue = " + queue.dequeue()
            );
        }
    }

    public static void rdump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(
                "size = " + queue.size() + "\t" +
                "peek = " + queue.peek() + "\t" +
                "remove = " + queue.remove()
            );
        }
    }

    public static int size(ArrayQueue queue) {
        return queue.size();
    }

    public static void main(String[] args) {
        System.out.println("------------- ArrayQueue Test -------------");

        ArrayQueue queue = new ArrayQueue();

        fill(queue, 10);
        assert size(queue) == 10;
        dump(queue);
        assert size(queue) == 0;

        rfill(queue, 10);
        assert size(queue) == 10;
        rdump(queue);
        assert size(queue) == 0;

        System.out.println("ArrayQueue size test passed\n\n");
    }
}
