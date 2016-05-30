package nfinity.nfinity.ncontext.contexts;

import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMember;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.util.ListUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Insert description here
 *
 * @author Comic
 * @since 22/05/2016 2016
 */
public class SubContext extends MethodContext {
    public MethodContext ParentContext;

    public SubContext(MethodContext parent) {
    	Assembly = parent.Assembly;
    	ParentContext = parent;
    }

    @Override
    public NType getType() {
        return ParentContext.getType();
    }
    
    public List<NField> MemberFields() {
        return ListUtils.union(Fields, ParentContext.MemberFields());
    }

    public List<NMethod> Methods() {
        return ParentContext.Methods();
    }

    public List<NField> Fields() {
        return ListUtils.union(Fields, ParentContext.Fields());
    }
}
