package nfinity.nfinity.ntype.core;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.contexts.WorldContext;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class World extends NType {
    public World (NType parentType, NAssembly assembly){
    	super(parentType, assembly);
        TypeContext = Assembly.WorldContext;
    }
}
