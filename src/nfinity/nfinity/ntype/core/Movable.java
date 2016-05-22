package nfinity.nfinity.ntype.core;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class Movable extends NType {
    public Movable() {
        ParentType = NAssembly.Atom;
    }
}
