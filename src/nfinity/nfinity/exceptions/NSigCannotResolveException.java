package nfinity.nfinity.exceptions;

import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.nsignature.request.NSigRequest;

/**
 * Insert description here
 *
 * @author Comic
 * @since 11/06/2016 2016
 */
public class NSigCannotResolveException extends Exception {
    public NSigRequest Request;

    public NSigCannotResolveException(NSigRequest request) {
        Request = request;
    }

    public String Message() {
        return String.format("Could not resolve the " + (Request.isField() ? "field" : "proc") + " " + Request.MemberName);
    }
}
