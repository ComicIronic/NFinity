package nfinity.nfinity.ntype.generic.nullable;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.generic.GenericBase;
import nfinity.nfinity.ntype.generic.GenericType;

/**
 * Created by Comic on 21/05/2016.
 */
public class Nullable extends GenericBase {
    {
        Fields.add(new NField(this, new NSignature("Value", GenericType, NAccess.Public)));
    }

    public Nullable() {
        ParentType = NType.Null;
        GenericType = new GenericType();
    }

    public NType genericInstance(NType specificType) {
        return new NullableInstance(ParentType, specificType, Assembly);
    }
}
