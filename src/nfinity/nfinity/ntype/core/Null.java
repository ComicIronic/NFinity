package nfinity.nfinity.ntype.core;

import nfinity.nfinity.exceptions.NOperationNotSupportedException;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NOperation;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class Null extends NType {
    public static NType Null = new Null(null, null);

    private Null(NType parentType, NAssembly assembly) {
    	super(parentType, assembly);
    }

    @Override
    public NType unaryOperatorProduct(NOperation operation) throws NOperationNotSupportedException
    {
        if(operation == NOperation.Not) {
            return Assembly.Any;
        }
        return super.unaryOperatorProduct(operation);
    }
}
