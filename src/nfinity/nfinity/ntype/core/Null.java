package nfinity.nfinity.ntype.core;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class Null extends NType {
    public static NType Null = new Null(null, null);

    private Null(NType parentType, NAssembly assembly) {
    	super(parentType, assembly);
    }
}
