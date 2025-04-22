package core;

public class CalculatorErrorHandler {

    /**
     * Kiểm tra và xử lý lỗi cho các phép toán cơ bản (+, -, *, /, %, ^, logab).
     * @param a Số thứ nhất
     * @param b Số thứ hai
     * @param operator Toán tử
     * @return Thông báo lỗi nếu có, hoặc null nếu không có lỗi
     */
    public static String checkBasicOperationError(double a, double b, String operator) {
        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            return "Input out of range";
        }

        switch (operator) {
            case "/":
                if (b == 0) {
                    return "Cannot divide by zero";
                }
                break;
            case "%":
                if (b == 0) {
                    return "Cannot modulo by zero";
                }
                break;
            case "^":
                if (a == 0 && b <= 0) {
                    return "Zero raised to non-positive power undefined";
                }
                double result = Math.pow(a, b);
                if (!Double.isFinite(result)) {
                    return "Result out of range";
                }
                break;
            case "logab":
                if (a <= 0 || a == 1) {
                    return "Invalid base for logarithm";
                }
                if (b <= 0) {
                    return "Logarithm undefined for non-positive number";
                }
                break;
            default:
                if (!operator.matches("[+\\-*/%^]")) {
                    return "Invalid operator";
                }
        }
        return null;
    }

    /**
     * Kiểm tra lỗi cho các hàm lượng giác (sin, cos, tan, cot).
     * @param func Hàm lượng giác
     * @param angle Góc đầu vào
     * @return Thông báo lỗi nếu có, hoặc null nếu không có lỗi
     */
    public static String checkTrigonometricError(String func, double angle) {
        if (!Double.isFinite(angle)) {
            return "Input out of range";
        }

        switch (func) {
            case "sin":
            case "cos":
                return null; // sin và cos luôn hợp lệ với số thực
            case "tan":
                double tanVal = Math.tan(angle);
                if (!Double.isFinite(tanVal)) {
                    return "Tangent undefined";
                }
                break;
            case "cot":
                double cotVal = Math.tan(angle);
                if (cotVal == 0) {
                    return "Cotangent undefined";
                }
                break;
            default:
                return "Invalid trigonometric function";
        }
        return null;
    }

    /**
     * Kiểm tra lỗi cho phép tính giai thừa.
     * @param n Số cần tính giai thừa
     * @return Thông báo lỗi nếu có, hoặc null nếu không có lỗi
     */
    public static String checkFactorialError(double n) {
        if (!Double.isFinite(n)) {
            return "Number too large for factorial";
        }
        if (n < 0) {
            return "Cannot compute factorial of negative number";
        }
        if (n > 20) {
            return "Factorial too large";
        }
        if (n != Math.floor(n)) {
            return "Factorial only defined for integers";
        }
        return null;
    }

    /**
     * Kiểm tra lỗi cho phép tính căn bậc hai.
     * @param number Số cần tính căn bậc hai
     * @return Thông báo lỗi nếu có, hoặc null nếu không có lỗi
     */
    public static String checkSquareRootError(double number) {
        if (!Double.isFinite(number)) {
            return "Input out of range";
        }
        if (number < 0) {
            return "Cannot take square root of negative number";
        }
        return null;
    }

    /**
     * Kiểm tra lỗi cho hàm logarit (log10, ln).
     * @param number Số cần tính logarit
     * @return Thông báo lỗi nếu có, hoặc null nếu không có lỗi
     */
    public static String checkLogarithmError(double number) {
        if (!Double.isFinite(number)) {
            return "Input out of range";
        }
        if (number <= 0) {
            return "Logarithm undefined for non-positive number";
        }
        return null;
    }

    /**
     * Kiểm tra lỗi cho phép tính nghịch đảo (1/x).
     * @param number Số cần tính nghịch đảo
     * @return Thông báo lỗi nếu có, hoặc null nếu không có lỗi
     */
    public static String checkInverseError(double number) {
        if (!Double.isFinite(number)) {
            return "Input out of range";
        }
        if (number == 0) {
            return "Cannot divide by zero";
        }
        return null;
    }

    /**
     * Kiểm tra lỗi cho biểu thức đầu vào (ví dụ: "5++2", "++").
     * @param expression Biểu thức cần kiểm tra
     * @return Thông báo lỗi nếu có, hoặc null nếu không có lỗi
     */
    public static String checkExpressionError(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return "Empty expression";
        }

        // Kiểm tra các toán tử liên tiếp (ví dụ: "5++2")
        if (expression.matches(".*[+\\-*/%^]{2,}.*")) {
            return "Invalid expression: consecutive operators";
        }

        // Kiểm tra biểu thức bắt đầu hoặc kết thúc bằng toán tử
        if (expression.trim().matches("^[+\\-*/%^].*") || expression.trim().matches(".*[+\\-*/%^]$")) {
            return "Invalid expression: starts or ends with operator";
        }

        // Kiểm tra định dạng số hợp lệ
        try {
            String[] parts = expression.split(" ");
            if (parts.length == 3 && parts[1].matches("[+\\-*/%^]")) {
                Double.parseDouble(parts[0].trim());
                Double.parseDouble(parts[2].trim());
            } else if (parts.length == 1) {
                Double.parseDouble(parts[0].trim());
            } else {
                return "Invalid expression format";
            }
            return null;
        } catch (NumberFormatException e) {
            return "Invalid number format";
        }
    }
}