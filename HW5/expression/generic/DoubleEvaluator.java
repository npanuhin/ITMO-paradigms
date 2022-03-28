package expression.generic;

public class DoubleEvaluator implements Evaluator<Double> {

    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double subtract(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double multiply(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double divide(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double negate(Double x) {
        return -x;
    }

    @Override
    public Double count(Double x) {
        return (double) Long.bitCount(Double.doubleToLongBits(x));
    }

    private boolean isNegative(double x) {
        return Double.doubleToRawLongBits(x) < 0;
    }

    @Override
    public Double max(Double x, Double y) {
        if (x.isNaN() || y.isNaN()) {
            return Double.NaN;
        }
        if (!isNegative(x) && isNegative(y)) {
            return x;
        }
        if (isNegative(x) && !isNegative(y)) {
            return y;
        }
        return x >= y ? x : y;
        // return Math.max(x, y);
    }

    @Override
    public Double min(Double x, Double y) {
        if (x.isNaN() || y.isNaN()) {
            return Double.NaN;
        }
        if (!isNegative(x) && isNegative(y)) {
            return y;
        }
        if (isNegative(x) && !isNegative(y)) {
            return x;
        }
        return x <= y ? x : y;
        // return Math.min(x, y);
    }

    @Override
    public Double parse(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public Double getValue(int value) {
        return (double) value;
    }
}
