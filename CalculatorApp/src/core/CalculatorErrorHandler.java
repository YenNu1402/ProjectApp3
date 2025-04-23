package core;

public class CalculatorErrorHandler {

    public static String checkBasicOperationError(double a, double b, String operator) {
        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            return "Math ERROR";
        }

        switch (operator) {
            case "/":
                if (Math.abs(b) < 1e-10) return "Math ERROR: Divide by zero";
                break;
            case "%":
                if (Math.abs(b) < 1e-10 && !operator.equals("%")) return "Math ERROR: Modulo by zero";
                break;
            case "^":
                if (a == 0 && b <= 0) return "Math ERROR: Undefined";
                if (!Double.isFinite(Math.pow(a, b))) return "Math ERROR: Out of range";
                break;
            case "logab":
                if (a <= 0 || Math.abs(a - 1) < 1e-10) return "Math ERROR: Invalid base";
                if (b <= 0) return "Math ERROR: Invalid argument";
                break;
            default:
                if (!operator.matches("[+\\-*/%^]")) return "Syntax ERROR: Invalid operator";
        }
        return null;
    }

    public static String checkTrigonometricError(String func, double angle) {
        if (!Double.isFinite(angle)) return "Math ERROR: Invalid angle";

        switch (func) {
            case "sin":
            case "cos":
                return null;
            case "tan":
                if (Math.abs(Math.cos(angle)) < 1e-10) return "Math ERROR: Undefined";
                break;
            case "cot":
                if (Math.abs(Math.sin(angle)) < 1e-10) return "Math ERROR: Undefined";
                break;
            default:
                return "Syntax ERROR: Unknown function";
        }
        return null;
    }

    public static String checkFactorialError(double n) {
        if (!Double.isFinite(n)) return "Math ERROR: Out of range";
        if (n < 0) return "Math ERROR: Negative not allowed";
        if (n > 69) return "Math ERROR: Too large";
        if (Math.abs(n - Math.floor(n)) > 1e-10) return "Math ERROR: Requires integer";
        return null;
    }

    public static String checkSquareRootError(double number) {
        if (!Double.isFinite(number)) return "Math ERROR: Out of range";
        if (number < 0) return "Math ERROR: Negative not allowed";
        return null;
    }

    public static String checkLogarithmError(double number) {
        if (!Double.isFinite(number)) return "Math ERROR: Out of range";
        if (number <= 0) return "Math ERROR: Non-positive not allowed";
        return null;
    }

    public static String checkInverseError(double number) {
        if (!Double.isFinite(number)) return "Math ERROR: Out of range";
        if (Math.abs(number) < 1e-10) return "Math ERROR: Divide by zero";
        return null;
    }

    public static String checkExpressionError(String expression) {
        if (expression == null || expression.trim().isEmpty()) return "Syntax ERROR: Empty";

        // Check for invalid consecutive operators (excluding unary minus)
        if (expression.matches(".*[+*/%^]{2,}.*") || expression.matches(".*\\-\\s*[+*/%^].*")) {
            return "Syntax ERROR: Invalid operators";
        }

        // Allow starting with minus for negative numbers
        if (expression.trim().matches(".*[+*/%^]$")) {
            return "Syntax ERROR: Invalid position";
        }

        // Basic expression validation
        try {
            String evalExpression = expression.replace("π", String.valueOf(Math.PI))
                                             .replace("e", String.valueOf(Math.E));
            String[] parts = evalExpression.trim().split("\\s+");
            if (parts.length == 1) {
                Double.parseDouble(parts[0]);
            } else if (parts.length == 3 && parts[1].matches("[+\\-*/%^]")) {
                Double.parseDouble(parts[0]);
                Double.parseDouble(parts[2]);
            } else {
                return "Syntax ERROR: Malformed";
            }
        } catch (NumberFormatException e) {
            return "Math ERROR: Invalid number";
        }

        return null;
    }
}