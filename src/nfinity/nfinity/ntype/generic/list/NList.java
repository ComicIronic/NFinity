package nfinity.nfinity.ntype.generic.list;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.generic.GenericBase;

/**
 * Created by Comic on 21/05/2016.
 */
public class NList extends GenericBase {
    {
        ParentType = NType.Null;
    }

    public NList(NType parentType, NAssembly assembly) {
        super(parentType, assembly);
    }
}
