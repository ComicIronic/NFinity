package nfinity.nfinity.ntype.generic;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class GenericBase extends NType {
    public GenericType GenericType = new GenericType();

    public GenericBase() {
        ParentType = NAssembly.Datum;
    }

    public boolean canCreateInstance(NType specificType) {
        return GenericType.acceptsCast(specificType);
    }

    public NType createInstance(NType specificType) {
        NType instance = new NType(ParentType);

        for(NMethod method : Methods) {
            instance.Methods.add(new NMethod(method.Signature.degenerize(GenericType, specificType)));
        }

        for(NField field : Fields) {
            instance.Fields.add(new NField(field.Signature.degenerize(GenericType, specificType)));
        }

        return instance;
    }
}
