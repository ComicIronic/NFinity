package nfinity.nfinity.ntype.generic;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;

/**
 * Insert description here
 *
 * @author Comic
 * @since 22/05/2016 2016
 */
public class GenericInstance extends NType {
    public NType ImplementedType;

    public GenericInstance() {
        ParentType = NType.Null;
        ImplementedType = NType.Null;
    }

    public GenericInstance(NType parent, NType implemented, NAssembly assembly) {
        super(parent, assembly);
        ImplementedType = implemented;
    }

    public boolean acceptsTypeAssign(NType other) {
        if(other instanceof GenericInstance) {
            GenericInstance otherInstance = (GenericInstance) other;

            return otherInstance.ParentType == ParentType &&
                    otherInstance.ImplementedType == ImplementedType;
        } else {
            return false;
        }
    }
}
