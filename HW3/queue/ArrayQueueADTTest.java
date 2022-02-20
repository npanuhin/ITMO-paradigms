package queue;


public class ArrayQueueADTTest {

    public static void fill(ArrayQueueADT queue) {
        for (int i = 0; i < 10; i++) {
            System.out.println("put(" + i + ")");
            ArrayQueueADT.enqueue(queue, i);
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

    public static void main(String[] args) {
        System.out.println("----------- ArrayQueueADT Test ------------");

        ArrayQueueADT queue = new ArrayQueueADT();
        fill(queue);
        dump(queue);

        System.out.println();
    }
}
