import java.util.Stack;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class PostfixCalculator {

    public int evaluatePostfix(String postfixExpression) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = postfixExpression.split(" ");

        try {
            for (String token : tokens) {
                if (isNumber(token)) {
                    stack.push(Integer.parseInt(token));
                } else {
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException("Invalid postfix expression: Not enough operands for operator " + token);
                    }
                    int operand2 = stack.pop();
                    int operand1 = stack.pop();
                    int result = 0;
                    switch (token) {
                        case "+":
                            result = operand1 + operand2;
                            break;
                        case "-":
                            result = operand1 - operand2;
                            break;
                        case "*":
                            result = operand1 * operand2;
                            break;
                        case "/":
                            if (operand2 == 0) {
                                throw new ArithmeticException("Division by zero");
                            }
                            result = operand1 / operand2;
                            break;
                        case "%":
                            if (operand2 == 0) {
                                throw new ArithmeticException("Modulo by zero");
                            }
                            result = operand1 % operand2;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid operator: " + token);
                    }
                    stack.push(result);
                }
            }

            if (stack.size() == 1) {
                return stack.pop();
            } else {
                throw new IllegalArgumentException("Invalid postfix expression: Too many operands");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return Integer.MIN_VALUE; // Indicate an error
        }
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void evaluateExpressionsFromFile(String filePath) {
        File file = new File(filePath);
        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String expression = scanner.nextLine().trim();
                if (!expression.isEmpty()) {
                    System.out.println("Evaluating expression " + lineNumber + ": \"" + expression + "\"");
                    int result = evaluatePostfix(expression);
                    // Still increment to track the line in the file
                    if (result != Integer.MIN_VALUE) {
                        System.out.println("Result: " + result);
                    }
                    lineNumber++; // Increment only on successful (or at least attempted) evaluation
                } else {
                    lineNumber++; // Increment for empty lines as well to maintain correct line numbers
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + filePath);
        }
    }

    public static void main(String[] args) {
        PostfixCalculator calculator = new PostfixCalculator();

        // Example 1: Valid Single-Digit and Multi-Digit Operands
        String expression1 = "4 2 * 3 +";
        System.out.println("Result 1: " + calculator.evaluatePostfix(expression1));

        // Example 2: Valid Expression
        String expression2 = "5 3 + 7 *";
        System.out.println("Result 2: " + calculator.evaluatePostfix(expression2));

        // Example 3: Invalid Expression (Missing operand)
        String expression3 = "4 2 * +";
        int result3 = calculator.evaluatePostfix(expression3);
        if (result3 != Integer.MIN_VALUE) {
            System.out.println("Result 3: " + result3);
        }

        // Example 4: Valid Expression with Division
        String expression4 = "10 2 /";
        System.out.println("Result 4: " + calculator.evaluatePostfix(expression4));

        // Example 5: Valid Expression with Modulo
        String expression5 = "10 3 %";
        System.out.println("Result 5: " + calculator.evaluatePostfix(expression5));

        // Example 6: Invalid Expression (Too many operands)
        String expression6 = "1 2 3 +";
        int result6 = calculator.evaluatePostfix(expression6);
        if (result6 != Integer.MIN_VALUE) {
            System.out.println("Result 6: " + result6);
        }

        // Test reading from a file (named "expressions.txt" in the project root)
        // with expressions like:
        // 10 5 +
        // 3 4 * 2 -
        // 15 % 3
        // 7 0 /
        System.out.println("\nEvaluating expressions from file:");
        calculator.evaluateExpressionsFromFile("expressions.txt");
    }
}