package nfinity.nfinity.ntype.generic;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class GenericBase extends NType {
    public GenericType GenericType = new GenericType();

    public GenericBase(NType parentType, NAssembly assembly) {
        super(parentType, assembly);
    }

    public GenericBase() {
    }

    {
    }

    public boolean canCreateInstance(NType specificType) {
        return GenericType.acceptsCast(specificType);
    }

    /**
     * Creates a type to be filled with the degenerized members
     * @return
     */
    public NType genericInstance(NType specificType) {
        return new GenericInstance(ParentType, specificType, Assembly);
    }

    public NType createInstance(NType specificType) {
        NType instance = genericInstance(specificType);

        for(NMethod method : Methods) {
            instance.Methods.add(new NMethod(instance, method.Signature.degenerize(GenericType, specificType)));
        }

        for(NField field : Fields) {
            instance.Fields.add(new NField(instance, field.Signature.degenerize(GenericType, specificType)));
        }

        return instance;
    }
}
