package expression.generic;

public class TruncEvaluator extends IntegerEvaluator {
    private Integer truncate(Integer x) {
        return (x / 10) * 10;
    }

    @Override
    public Integer add(Integer x, Integer y) {
        return truncate(super.add(x, y));
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        return truncate(super.subtract(x, y));
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        return truncate(super.multiply(x, y));
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        return truncate(super.divide(x, y));
    }

    @Override
    public Integer negate(Integer x) {
        return truncate(super.negate(x));
    }

    @Override
    public Integer count(Integer x) {
        return truncate(super.count(x));
    }

    @Override
    public Integer max(Integer x, Integer y) {
        return truncate(super.max(x, y));
    }

    @Override
    public Integer min(Integer x, Integer y) {
        return truncate(super.min(x, y));
    }

    @Override
    public Integer parse(String value) {
        return truncate(super.parse(value));
    }

    @Override
    public Integer getValue(int value) {
        return truncate(super.getValue(value));
    }
}
