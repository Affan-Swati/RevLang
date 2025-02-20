import java.util.ArrayList;
import java.util.List;

class ErrorHandler
{
    private static List<String> errors = new ArrayList<>();

    public static void addError(String message, int line) {
        errors.add("Line " + line + ": " + message);
    }

    public static boolean hasErrors() {
        return !errors.isEmpty();
    }

    public static void printErrors()
    {
        for (String error : errors)
        {
            System.out.println(error);
        }
    }
}