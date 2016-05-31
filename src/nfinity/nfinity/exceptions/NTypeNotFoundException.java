package nfinity.nfinity.exceptions;

/**
 * Insert description here
 *
 * @author Comic
 * @since 29/05/2016 2016
 */
public class NTypeNotFoundException extends Exception {
    public String FailedType = "";

    public NTypeNotFoundException(String failedType) {
        FailedType = failedType;
    }
}
