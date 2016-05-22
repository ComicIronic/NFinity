package nfinity.nfinity.nmember;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.nsignature.NSignature;

/**
 * Created by Comic on 21/05/2016.
 */
public abstract class NMember {
    public NSignature Signature;

    public NMember() {
        Signature = new NSignature(NAssembly.Null, NAccess.Private);
    }
}
