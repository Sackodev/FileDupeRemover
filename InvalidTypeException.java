import java.util.Arrays;
import java.util.List;

public class InvalidTypeException extends Exception {
    public InvalidTypeException(String message, List<String> ALLOWEDTYPES){
        super(message + Arrays.toString(ALLOWEDTYPES.toArray()));
    }
}
