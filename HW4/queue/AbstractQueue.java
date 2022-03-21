package queue;

import java.util.function.Predicate;


public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    protected abstract void enqueueImp(Object element);
    protected abstract void pushImp(Object element);
    protected abstract Object elementImp();
    protected abstract Object peekImp();
    protected abstract void dequeueImp();
    protected abstract void removeImp();

    @Override
    public void enqueue(Object element) {
        assert element != null;
        enqueueImp(element);
        size++;
    }

    @Override
    public void push(Object element) {
        assert element != null;
        pushImp(element);
        size++;
    }

    @Override
    public Object element() {
        assert size > 0;
        return elementImp();
    }

    @Override
    public Object peek() {
        assert size > 0;
        return peekImp();
    }

    @Override
    public Object dequeue() {
        assert size > 0;
        Object result = elementImp();
        dequeueImp();
        size--;
        return result;
    }

    @Override
    public Object remove() {
        assert size > 0;
        Object result = peekImp();
        removeImp();
        size--;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    @Override
    public void retainIf(Predicate<Object> condition) {
        int tmpSize = size;
        for (int i = 0; i < tmpSize; i++) {
            Object element = dequeue();
            if (condition.test(element)) {
                enqueue(element);
            }
        }
    }

    @Override
    public void removeIf(Predicate<Object> condition) {
        retainIf(condition.negate());
    }

    private void whileImp(Predicate<Object> condition, boolean keep) {
        int tmpSize = size;
        while (tmpSize > 0 && condition.test(element())) {
            Object element = dequeue();
            if (keep) {
                enqueue(element);
            }
            tmpSize--;
        }

        if (keep) {
            for (int i = 0; i < tmpSize; i++) {
                dequeue();
            }
        }
    }

    @Override
    public void takeWhile(Predicate<Object> condition) {
        whileImp(condition, true);
    }

    @Override
    public void dropWhile(Predicate<Object> condition) {
        whileImp(condition, false);
    }
}
