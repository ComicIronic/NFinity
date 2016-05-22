package nfinity.nfinity.ntype.generic;

import com.sun.istack.internal.Pool;
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
        ParentType = NAssembly.Null;
        ImplementedType = NAssembly.Null;
    }

    public GenericInstance(NType parent, NType implemented) {
        ParentType = parent;
        ImplementedType = implemented;
    }

    public boolean acceptsCast(NType other) {
        if(other instanceof GenericInstance) {
            GenericInstance otherInstance = (GenericInstance) other;

            return otherInstance.ParentType == ParentType &&
                    otherInstance.ImplementedType == ImplementedType;
        } else {
            return false;
        }
    }
}
