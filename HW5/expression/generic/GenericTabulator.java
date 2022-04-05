package expression.generic;

import expression.exceptions.ParseException;

import java.util.Map;


public class GenericTabulator implements Tabulator {
    private final static Map<String, Evaluator<?>> EVALUATORS = Map.of(
        "i", new CheckedIntegerEvaluator(),
        "d", new DoubleEvaluator(),
        "bi", new BigIntegerEvaluator(),
        "u", new IntegerEvaluator(),
        "l", new LongEvaluator(),
        "t", new TruncEvaluator()
    );

    private <T> Object[][][] build(
            Evaluator<T> evaluator, String expression,
            int x1, int x2, int y1, int y2, int z1, int z2
        ) throws ParseException {

        GenericExpression<T> expr = new ExpressionParser<T>().parse(expression);

        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        result[i][j][k] = expr.evaluate(
                            evaluator.getValue(x1 + i),
                            evaluator.getValue(y1 + j),
                            evaluator.getValue(z1 + k),
                            evaluator
                        );
                    } catch (ArithmeticException e) {
                        // result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object[][][] tabulate(
            String mode, String expression,
            int x1, int x2, int y1, int y2, int z1, int z2
        ) throws ParseException {

        return build(EVALUATORS.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }
}
