package nfinity.nfinity.nmember;

import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;

import java.util.*;

/**
 * Created by Comic on 21/05/2016.
 */
public class NMethod extends NMember {
    public NMethod(NType owner, NSignature signature) {
        TypeOwner = owner;
        Signature = signature;
    }
}
