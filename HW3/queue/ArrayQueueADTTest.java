package queue;

public class ArrayQueueADTTest {

    public static void fill(ArrayQueueADT queue, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("enqueue(" + i + ")");
            ArrayQueueADT.enqueue(queue, i);
        }
    }

    public static void rfill(ArrayQueueADT queue, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("push(" + i + ")");
            ArrayQueueADT.push(queue, i);
        }
    }

    public static void dump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(
                "size = " + ArrayQueueADT.size(queue) + "\t" +
                "element = " + ArrayQueueADT.element(queue) + "\t" +
                "dequeue = " + ArrayQueueADT.dequeue(queue)
            );
        }
    }

    public static void rdump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(
                "size = " + ArrayQueueADT.size(queue) + "\t" +
                "peek = " + ArrayQueueADT.peek(queue) + "\t" +
                "remove = " + ArrayQueueADT.remove(queue)
            );
        }
    }

    public static int size(ArrayQueueADT queue) {
        return ArrayQueueADT.size(queue);
    }

    public static void main(String[] args) {
        System.out.println("----------- ArrayQueueADT Test ------------");

        ArrayQueueADT queue = new ArrayQueueADT();

        fill(queue, 10);
        assert size(queue) == 10;
        dump(queue);
        assert size(queue) == 0;

        rfill(queue, 10);
        assert size(queue) == 10;
        rdump(queue);
        assert size(queue) == 0;

        System.out.println("ArrayQueueADT size test passed\n\n");
    }
}
