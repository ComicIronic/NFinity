package nfinity.nfinity.nsignature;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;

import static nfinity.nfinity.nassembly.NAssembly.Null;

/**
 * Created by Comic on 21/05/2016.
 */
public class NArg {
    public NType Type;

    public boolean Optional;

    public NArg() {
        Type = Null;
        Optional = true;
    }

    public NArg(NType type, boolean optional) {
        Type = type;
        Optional = optional;
    }

    public boolean accepts(NType othertype) {
        return othertype.canCastTo(Type) || (Optional && othertype == Null);
    }
}
