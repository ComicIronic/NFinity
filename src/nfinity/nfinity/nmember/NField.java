package nfinity.nfinity.nmember;

import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class NField extends NMember {
    public NField(NType owner, NSignature signature) {
        TypeOwner = owner;
        Signature = signature;
    }
}
