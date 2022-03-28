package expression.generic;

public class GenericVariable<T> implements GenericExpression<T> {
    private final String name;

    public GenericVariable(String content) {
        name = content;
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                throw new IllegalArgumentException("No such variale found: " + name);
        }
    }
}
