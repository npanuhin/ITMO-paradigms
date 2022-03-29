package expression.generic;

public interface Evaluator<T> {
    T add(T x, T y);
    T subtract(T x, T y);
    T multiply(T x, T y);
    T divide(T x, T y);
    T negate(T x);
    T count(T x);
    T parse(String value);
    T getValue(int value);
    T max(T x, T y);
    T min(T x, T y);
}
