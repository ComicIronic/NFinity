package nfinity.nfinity.ncontext.contexts;

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

    public TypeContext(NType type) {
        Type = type;
    }

    public List<NMethod> Methods() {
        return ListUtils.union(NAssembly.WorldContext.Methods(), Type.getMethods());
    }

    public List<NField> Fields() {
        return ListUtils.union(NAssembly.WorldContext.Fields(), Type.getFields());
    }

    public boolean canAccess(NMember member) {
        switch(member.Signature.Access) {
            case Public: {
                return true;
            }
            case Protected: {
                return Type.isChildOf(member.TypeOwner);
            }
            case Private: {
                return Type == member.TypeOwner;
            }
            default: {
                return false;
            }
        }
    }
}
