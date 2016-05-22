package nfinity.nfinity.nsignature;

import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class NArgList extends NArg {
    public NArgList(NType type) {
        Type = type;
        Optional = false;
    }

    public boolean accepts(NType othertype) {
         return Type.acceptsCast(othertype);
    }
}
