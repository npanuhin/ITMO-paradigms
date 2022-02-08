import expression.AbstractExpression;
import expression.exceptions.ExpressionParser;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

import java.io.IOException;

import myclasses.Scanner;


public class Main {
    public static void main(String[] args) {
        final ExpressionParser parser = new ExpressionParser();

        // final AbstractExpression task = parser.parse("1000000*x*x*x*x*x/(x-1)");
        // for (int i = 0; i <= 10; ++i)  {
        //     try {
        //         System.out.println(task.evaluate(i));
        //     } catch (DivisionByZeroException e) {
        //         System.out.println("division by zero");
        //     } catch (OverflowException e) {
        //         System.out.println("overflow");
        //     }
        // }

        try {
            final Scanner scanner = new Scanner(System.in);
            final AbstractExpression task = parser.parse(scanner.nextLine());
            scanner.skipLine();
            try {
                System.out.println(task.evaluate(scanner.nextInt()));
            } catch (DivisionByZeroException e) {
                System.out.println("division by zero");
            } catch (OverflowException e) {
                System.out.println("overflow");
            } catch (NumberFormatException e) {
                System.out.println("WHAT ARE YOU DOING?! PLEASE ENTER A VALID INTEGER!!!");
            } catch (ArithmeticException e) {
                System.out.println("arithmetic exception");
            }
        } catch (IOException e) {
            System.out.println("Cannot open input stream: " + e.getMessage());
        }
    }
}
