package nfinity.nfinity.exceptions;

/**
 * Insert description here
 *
 * @author Comic
 * @since 02/06/2016 2016
 */
public class BadNTypeException extends  Exception {
    public String Message;

    public BadNTypeException(String message) {
        Message = message;
    }
}
