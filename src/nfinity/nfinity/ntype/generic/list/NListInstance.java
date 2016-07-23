package nfinity.nfinity.ntype.generic.list;

import nfinity.nfinity.exceptions.NOperationNotSupportedException;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NOperation;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.generic.GenericInstance;

/**
 * Insert description here
 *
 * @author Comic
 * @since 23/07/2016 2016
 */
public class NListInstance extends GenericInstance {
    public NListInstance(NType parent, NType implemented, NAssembly assembly) {
        super(parent, implemented, assembly);
    }

    @Override
    public NType binaryOperatorProduct(NType other, NOperation operation) throws NOperationNotSupportedException {
        if(operation == NOperation.Add ||
                operation == NOperation.Subtract ||
                operation == NOperation.Binary_OR ||
                operation == NOperation.Binary_AND) {
            //We can do list operations if the types match
            if(other instanceof NListInstance) {
                NListInstance otherList = (NListInstance) other;

                if(otherList.getImplementedType() == getImplementedType()) {
                    return this;
                } else {
                    throw new NOperationNotSupportedException("List types must match: Expected " + _implementedType.typepath() +
                                                         " Got " + other.typepath());
                }
            } else {
                if(_implementedType.acceptsTypeAssign(other)) {
                    return this;
                } else {
                    throw new NOperationNotSupportedException(
                            "Expected " + _implementedType.typepath()
                            + " Got " + other.typepath());
                }
            }
        } else {
            return super.binaryOperatorProduct(other, operation);
        }
    }
}
