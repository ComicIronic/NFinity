package nfinity.nfinity.exceptions;

/**
 * Insert description here
 *
 * @author Comic
 * @since 01/06/2016 2016
 */
public class NArgDeclareException extends Exception {
	public String Message;
	
	public NArgDeclareException(String message) {
		Message = message;
	}
}