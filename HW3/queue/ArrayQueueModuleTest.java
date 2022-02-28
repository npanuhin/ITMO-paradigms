package queue;


public class ArrayQueueModuleTest {

    public static void fill() {
        for (int i = 0; i < 10; i++) {
            System.out.println("put(" + i + ")");
            ArrayQueueModule.enqueue(i);
        }
    }

    public static void dump() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(
                "size = " + ArrayQueueModule.size() + "\t" +
                "element = " + ArrayQueueModule.element() + "\t" +
                "dequeue = " + ArrayQueueModule.dequeue()
            );
        }
    }

    public static void main(String[] args) {
        System.out.println("---------- ArrayQueueModule Test ----------");

        ArrayQueueModule.enqueue(0);
        ArrayQueueModule.enqueue(1);
        ArrayQueueModule.enqueue(2);
        ArrayQueueModule.enqueue(2);
        ArrayQueueModule.enqueue(2);
        ArrayQueueModule.enqueue(2);
        ArrayQueueModule.enqueue(2);
        ArrayQueueModule.enqueue(2);

        System.out.println(ArrayQueueModule.element());

        // System.out.println("size = " + ArrayQueueModule.size());

        // dump();

        // ArrayQueueModule.enqueue(0);
        // ArrayQueueModule.enqueue(1);
        // ArrayQueueModule.enqueue(2);

        // System.out.println("size = " + ArrayQueueModule.size());

        // dump();

        // System.out.println();
    }
}
