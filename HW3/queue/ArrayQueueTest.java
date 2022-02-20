package queue;


public class ArrayQueueTest {

    public static void fill(ArrayQueue queue) {
        for (int i = 0; i < 10; i++) {
            System.out.println("put(" + i + ")");
            queue.enqueue(i);
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

    public static void main(String[] args) {
        System.out.println("------------- ArrayQueue Test -------------");

        ArrayQueue queue = new ArrayQueue();
        fill(queue);
        dump(queue);

        System.out.println();
    }
}
