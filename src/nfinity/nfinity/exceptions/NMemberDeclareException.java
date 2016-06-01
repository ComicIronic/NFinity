package nfinity.nfinity.exceptions;

/**
 * Insert description here
 *
 * @author Comic
 * @since 01/06/2016 2016
 */
public class NMemberDeclareException extends Exception {
	public String MemberName;
	
	public String Message;
	
	public NMemberDeclareException(String memberName, String message) {
		MemberName = memberName;
		Message = message;
	}
}