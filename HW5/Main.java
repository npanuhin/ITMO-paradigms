import expression.exceptions.ParseException;
import expression.generic.GenericTabulator;


public class Main {
    public static void main(String[] args) {
        GenericTabulator tabulator = new GenericTabulator();
        Object[][][] result;

        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }

        try {
            result = tabulator.tabulate(args[0].substring(1), args[1], -2, 2, -2, 2, -2, 2);
        } catch (ParseException e) {
            System.out.println("Bad expression:" + e.getMessage());
            return;
        }


        System.out.println("\tx\t|\ty\t|\tz\t|     result");
        System.out.println("-".repeat(65));
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                for (int k = 0; k < result[i][j].length; k++) {
                    System.out.println(String.format(
                        "\t%d\t|\t%d\t|\t%d\t|\t%d",
                        i - 2, j - 2, k - 2, result[i][j][k]
                    ));
                }
            }
        }
    }
}
