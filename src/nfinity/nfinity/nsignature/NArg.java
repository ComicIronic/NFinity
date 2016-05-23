package nfinity.nfinity.nsignature;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class NArg {
    public NType Type;

    public boolean Optional;

    public NArg() {
        Type = NType.Null;
        Optional = true;
    }

    public NArg(NType type, boolean optional) {
        Type = type;
        Optional = optional;
    }

    public boolean accepts(NType othertype) {
        return Type.acceptsCast(othertype) || (Optional && othertype == NType.Null);
    }
}
