package nfinity.nfinity.ntype.core;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;

/**
 * Insert description here
 *
 * @author Comic
 * @since 23/05/2016 2016
 */
public class Any extends NType {
    public Any(NType parentType, NAssembly assembly) {
        super(parentType, assembly);
    }

    public boolean acceptsTypeAssign(NType other) {
        return true;
    }
}
