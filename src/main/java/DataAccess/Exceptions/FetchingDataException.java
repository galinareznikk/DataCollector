package DataAccess.Exceptions;

public class FetchingDataException extends Exception {

    public FetchingDataException(String message) {
        super(message);
    }

    public FetchingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}