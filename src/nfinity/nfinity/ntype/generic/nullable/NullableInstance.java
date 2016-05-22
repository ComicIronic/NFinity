package nfinity.nfinity.ntype.generic.nullable;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.generic.GenericInstance;

/**
 * The type used for matching signatures to see if an NType can be used in a generic
 *
 * @author Comic
 * @since 22/05/2016 2016
 */
public class NullableInstance extends GenericInstance {
    public boolean acceptsCast(NType other) {
        return other == NAssembly.Null || super.acceptsCast(other);
    }

    public NullableInstance(NType parent, NType implemented) {
        ParentType = parent;
        ImplementedType = implemented;
    }
}
