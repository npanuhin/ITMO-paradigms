package expression.generic;

import java.util.Set;

import expression.exceptions.CheckedStringStreamer;
import expression.exceptions.ParseException;
import expression.parser.AbstractStreamer;


public class ExpressionParser<T> {
    private static final Set<Character> VARIABLES = Set.of('x', 'y', 'z');
    private AbstractStreamer expr;

    private boolean isDigit(final char c) {
        // return Character.isDigit(c);
        return '0' <= c && c <= '9';
    }

    private void skipWhitespaces() {
        while (expr.hasNextChar() && Character.isWhitespace(expr.curChar())) {
            expr.getChar();
        }
    }

    private String parseConst(final boolean isNegative) throws ParseException {
        StringBuilder number = new StringBuilder();
        if (isNegative) {
            number.append('-');
        }
        while (expr.hasNextChar() && isDigit(expr.curChar())) {
            number.append(expr.getChar());
        }
        return number.toString();
    }

    private GenericExpression<T> parsePriority4() throws ParseException {
        skipWhitespaces();

        if (!expr.hasNextChar()) {
            throw new ParseException(
                "Unexpected end of stream, expected Const, Variable or any other Expression at the end"
            );
        }

        if (expr.skipIfMatch('(')) {
            final GenericExpression<T> result = parsePriority1();
            expr.expect(')');
            return result;
        }

        if (expr.skipIfMatch("count")) {
            return new GenericCount<>(parsePriority4());

        } else if (expr.skipIfMatch('-')) {
            if (!expr.hasNextChar()) {
                throw new ParseException("Unexpected end of stream, expected Const or any Expression after '-'");
            } else if (isDigit(expr.curChar())) {
                return new GenericConst<>(parseConst(true));
            } else {
                return new GenericNegate<>(parsePriority4());
            }

        } else if (isDigit(expr.curChar())) {
            return new GenericConst<>(parseConst(false));

        } else if (VARIABLES.contains(expr.curChar())) {
            return new GenericVariable<>(Character.toString(expr.getChar()));

        } else {
            throw new ParseException(
                "Parser can not identify expression starting with char '" + expr.curChar() + "'"
            );
        }
    }

    private GenericExpression<T> parsePriority3() throws ParseException {
        GenericExpression<T> result = parsePriority4();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch('*')) {
                result = new GenericMultiply<>(result, parsePriority4());
            } else if (expr.skipIfMatch('/')) {
                result = new GenericDivide<>(result, parsePriority4());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    private GenericExpression<T> parsePriority2() throws ParseException {
        GenericExpression<T> result = parsePriority3();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch('+')) {
                result = new GenericAdd<>(result, parsePriority3());
            } else if (expr.skipIfMatch('-')) {
                result = new GenericSubtract<>(result, parsePriority3());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    private GenericExpression<T> parsePriority1() throws ParseException {
        GenericExpression<T> result = parsePriority2();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch("min")) {
                result = new GenericMin<>(result, parsePriority2());
            } else if (expr.skipIfMatch("max")) {
                result = new GenericMax<>(result, parsePriority2());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    public GenericExpression<T> parse(String expression) throws ParseException {
        this.expr = new CheckedStringStreamer(expression);
        GenericExpression<T> result = parsePriority1();
        if (expr.hasNextChar()) {
            throw new ParseException("Expected end of stream after \"" + result + "\"");
        }
        return result;
    }
}
