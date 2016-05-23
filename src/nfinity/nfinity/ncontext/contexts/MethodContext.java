package nfinity.nfinity.ncontext.contexts;

import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMember;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert description here
 *
 * @author Comic
 * @since 22/05/2016 2016
 */
public class MethodContext extends NContext {
    public NMethod MethodOwner;

    public List<NField> Fields = new ArrayList<NField>();

    public MethodContext() {
        Assembly = null;
        MethodOwner = null;
    }

    public MethodContext(NMethod method) {
    	Assembly = method.TypeOwner.Assembly;
    	MethodOwner = method;
    }

    public boolean canAccess(NMember member) {
        switch(member.Signature.Access) {
            case Member: {
                return MemberFields().contains(member);
            }
            case Public: {
                return true;
            }
            case Protected: {
                return MethodOwner.TypeOwner.isChildOf(member.TypeOwner);
            }
            case Private: {
                return MethodOwner.TypeOwner == member.TypeOwner;
            }
            default: {
                return false;
            }
        }
    }

    public List<NField> MemberFields() {
        return Fields;
    }

    public List<NMethod> Methods() {
        return MethodOwner.TypeOwner.TypeContext.Methods();
    }

    public List<NField> Fields() {
        return ListUtils.union(Fields, MethodOwner.TypeOwner.TypeContext.Fields());
    }

    public MethodContext dive() {
        return new SubContext(this);
    }
}
