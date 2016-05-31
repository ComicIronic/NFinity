package nfinity.nfinity.nmember;

import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class NField extends NMember {
    public NField(NContext owner, NSignature signature) {
        Owner = owner;
        Signature = signature;
    }
}
