package nfinity.nfinity.ncontext.contexts;

import nfinity.nfinity.exceptions.NTypeCannotExtendException;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.*;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.util.ListUtils;

import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class TypeContext extends NContext {
    public NType Type;

    public TypeContext(NAssembly assembly, NType type) {
    	Assembly = assembly;
        Type = type;
    }

    public List<NMethod> Methods() {
        return Type.getMethods();
    }

    public List<NField> Fields() {
        return Type.getFields();
    }

    public boolean canAccess(NMember member) {
        switch(member.Signature.Access) {
            case Public: {
                return true;
            }
            case Protected: {
                return Type.isChildOf(member.Owner.getType());
            }
            case Private: {
                return Type == member.Owner.getType();
            }
            default: {
                return false;
            }
        }
    }

    public NType getType() {
        return Type;
    }

    public void addField(NField field) {
        getType().addField(field);
    }

    public void addMethod(NMethod method) {
        getType().addMethod(method);
    }

    public NType createType(String typepath) throws NTypeCannotExtendException {
        return Type.getOrCreateChildType(typepath);
    }
}
