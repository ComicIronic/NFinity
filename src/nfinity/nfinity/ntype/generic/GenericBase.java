package nfinity.nfinity.ntype.generic;

import nfinity.nfinity.exceptions.NTypeCannotExtendException;
import nfinity.nfinity.exceptions.NTypeNotFoundException;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.ntype.NType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class GenericBase extends NType {
    public List<GenericInstance> Instances = new ArrayList<>();

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

    @Override
    public NType getChildType(String typepath) throws NTypeNotFoundException {
        NType implemented = Assembly.getTypeInPath(typepath);

        for(GenericInstance instance : Instances) {
            if(instance.ImplementedType == implemented) {
                return instance;
            }
        }

        throw new NTypeNotFoundException("Could not find generic instance of " + typepath() + " for " + typepath);
    }

    @Override
    public NType getOrCreateChildType(String typepath) throws NTypeCannotExtendException {
        try {
            return getChildType(typepath);
        } catch (NTypeNotFoundException e) {
        }

        NType implementedType;
        try {
            implementedType = Assembly.getTypeInPath(typepath);
        } catch (NTypeNotFoundException e) {
            throw (NTypeCannotExtendException) new NTypeCannotExtendException("Could not create an instacnce of " + typepath() + ": could not find type " + typepath).initCause(e);
        }

        if(canCreateInstance(implementedType)) {
            GenericInstance instance = createInstance(implementedType);
            Instances.add(instance);
            return instance;
        } else {
            throw new NTypeCannotExtendException("Could not create an instance of " + typepath() + ": " + typepath + " does not match the requirements");
        }
    }

    /**
     * Creates a type to be filled with the degenerized members
     * @return
     */
    protected GenericInstance genericInstance(NType specificType) {
        return new GenericInstance(this, specificType, Assembly);
    }

    protected GenericInstance createInstance(NType specificType) {
        GenericInstance instance = genericInstance(specificType);

        for(NMethod method : Methods) {
            instance.Methods.add(new NMethod(instance.TypeContext, method.Signature.degenerize(GenericType, specificType)));
        }

        for(NField field : Fields) {
            instance.Fields.add(new NField(instance.TypeContext, field.Signature.degenerize(GenericType, specificType)));
        }

        return instance;
    }
}
