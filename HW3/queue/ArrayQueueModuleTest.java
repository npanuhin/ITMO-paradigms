// Это задание не проверялось преподавателем, файл может содержать ошибки

package queue;

public class ArrayQueueModuleTest {

    public static void fill(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("enqueue(" + i + ")");
            ArrayQueueModule.enqueue(i);
        }
    }

    public static void rfill(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("push(" + i + ")");
            ArrayQueueModule.push(i);
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

    public static void rdump() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(
                "size = " + ArrayQueueModule.size() + "\t" +
                "peek = " + ArrayQueueModule.peek() + "\t" +
                "remove = " + ArrayQueueModule.remove()
            );
        }
    }

    public static int size() {
        return ArrayQueueModule.size();
    }

    public static void main(String[] args) {
        System.out.println("---------- ArrayQueueModule Test ----------");

        fill(10);
        assert size() == 10;
        dump();
        assert size() == 0;

        rfill(10);
        assert size() == 10;
        rdump();
        assert size() == 0;

        System.out.println("ArrayQueueModule size test passed\n\n");
    }
}
