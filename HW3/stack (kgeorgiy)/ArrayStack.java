import java.util.Arrays;

public class ArrayStack {
    private int size;
    private Object[] elements = new Object[5];

    // Полная форма
    public void push(ArrayStack this, Object element) {
        assert element != null;

        this.ensureCapacity(size + 1);
        this.elements[this.size++] = element;
    }

    // Неявный this
    private void ensureCapacity(int capacity) {
        if (capacity > this.elements.length) {
            this.elements = Arrays.copyOf(this.elements, 2 * capacity);
        }
    }

    // Необязательный this
    public Object pop() {
        assert size > 0;

        Object value = peek();
        elements[--size] = 0;
        return value;
    }

    public Object peek() {
        assert size > 0;

        return elements[size - 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public ArrayStack makeCopy() {
        final ArrayStack copy = new ArrayStack();
        copy.size = size;
        copy.elements = Arrays.copyOf(elements, size);
        return copy;
    }
}
