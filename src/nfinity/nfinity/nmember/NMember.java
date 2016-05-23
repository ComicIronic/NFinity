package nfinity.nfinity.nmember;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public abstract class NMember {
    public NType TypeOwner;

    public NSignature Signature;

    public NMember() {
    	TypeOwner = NType.Null;
        Signature = new NSignature("", NType.Null, NAccess.Private);
    }

    public NMember(NType owner, NSignature signature) {
        TypeOwner = owner;
        Signature = signature;
    }
}
