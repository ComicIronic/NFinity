package nfinity.nfinity.nmember;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.core.Null;

/**
 * Created by Comic on 21/05/2016.
 */
public abstract class NMember {
    public NContext Owner;

    public NSignature Signature;

    public NMember() {
    	Owner = null;
        Signature = new NSignature("", Null.Null, NAccess.Private);
    }

    public NMember(NContext owner, NSignature signature) {
        Owner = owner;
        Signature = signature;
    }
}
