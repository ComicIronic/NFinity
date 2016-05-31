package nfinity.nfinity.exceptions;

/**
 * Insert description here
 *
 * @author Comic
 * @since 31/05/2016 2016
 */
public class NTypeCannotExtendException extends Exception {
    public String FinalType = "";

    public NTypeCannotExtendException(String finalType) {
        FinalType = finalType;
    }
}
