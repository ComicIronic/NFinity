package nfinity.nfinity.ncontext.contexts;

import nfinity.nfinity.Nterpreter;
import nfinity.nfinity.exceptions.NTypeCannotExtendException;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMember;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.ntype.NType;
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
    	Assembly = method.Owner.getType().Assembly;
    	MethodOwner = method;
    }

    @Override
    public NType getType() {
        return MethodOwner.Owner.getType();
    }

    public void addField(NField field) {
        Fields.add(field);
    }

    public void addMethod(NMethod method) {
        Nterpreter.error(Nterpreter.FilePath, Nterpreter.LineNumber, "Methods cannot be declared inside other code!");
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
                return MethodOwner.Owner.getType().isChildOf(member.Owner.getType());
            }
            case Private: {
                return MethodOwner.Owner.getType() == member.Owner.getType();
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
        return MethodOwner.Owner.getType().TypeContext.Methods();
    }

    public List<NField> Fields() {
        return ListUtils.paired_union(Fields, MethodOwner.Owner.getType().TypeContext.Fields());
    }

    public MethodContext dive() {
        return new SubContext(this);
    }

    public NType createType(String typepath) throws NTypeCannotExtendException {
        throw new NTypeCannotExtendException("Cannot extend a type inside code.");
    }
}
