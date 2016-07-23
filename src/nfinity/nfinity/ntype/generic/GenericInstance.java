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
    protected NType _implementedType;

    public NType getImplementedType() {
        return _implementedType;
    }

    public GenericInstance(NType parent, NType implemented, NAssembly assembly) {
        super(parent, assembly);
        _implementedType = implemented;
    }

    @Override
    public boolean acceptsTypeAssign(NType other) {
        if(other instanceof GenericInstance) {
            GenericInstance otherInstance = (GenericInstance) other;

            return otherInstance.ParentType == ParentType &&
                    otherInstance.getImplementedType() == getImplementedType();
        } else {
            return false;
        }
    }
}
