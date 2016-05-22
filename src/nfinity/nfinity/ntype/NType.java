package nfinity.nfinity.ntype;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.ncontext.contexts.TypeContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class NType {
    public NType ParentType;

    public NContext TypeContext = new TypeContext(this);

    public List<NMethod> Methods = new ArrayList<NMethod>();
    public List<NField> Fields = new ArrayList<NField>();

    public NType() {
        ParentType = null;
    }

    public NType(NType parentType) {
        ParentType = parentType;
    }

    public List<NMethod> getMethods() {
        List<NMethod> allMethods = Methods;

        if(ParentType != null) {
            allMethods.addAll(ParentType.getMethods());
        }

        return allMethods;
    }

    public List<NField> getFields() {
        List<NField> allFields = Fields;

        if(ParentType != null) {
            allFields.addAll(ParentType.getFields());
        }

        return allFields;
    }

    public boolean acceptsCast(NType other) {
        return other.isChildOf(this);
    }

    public boolean isChildOf(NType possibleParent) {
        NType castType = this;

        while(castType != NAssembly.Null) {
            if(castType == possibleParent) {
                return true;
            }

            castType = castType.ParentType;
        }
        return false;
    }
}
