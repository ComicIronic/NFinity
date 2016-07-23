package nfinity.nfinity.exceptions;

/**
 * Insert description here
 *
 * @author Comic
 * @since 10/07/2016 2016
 */
public class NOperationNotSupportedException extends Exception {
    public String Message;

    public NOperationNotSupportedException(String message) {
        Message = message;
    }
}
