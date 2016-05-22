package nfinity.nfinity.ntype.generic;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class Nullable extends GenericBase {
    public Nullable() {
        ParentType = NAssembly.Null;

        Fields.add(new NField(new NSignature("Value", GenericType, NAccess.Public)));
    }
}
