package expression;

public interface AbstractExpression extends Expression, TripleExpression/*, BigDecimalExpression*/ {
    public int getPriority();
    public boolean isAssociative();
    public boolean alwaysNeedsWrap();
}
