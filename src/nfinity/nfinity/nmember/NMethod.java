package nfinity.nfinity.nmember;

import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;

import java.util.*;

/**
 * Created by Comic on 21/05/2016.
 */
public class NMethod extends NMember {
    public NMethod(NContext owner, NSignature signature) {
        Owner = owner;
        Signature = signature;
    }
}
