package nfinity.nfinity.ntype;

import nfinity.nfinity.ntype.core.Null;
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
	public static NType Null = new Null(null, null);
	
    public NType ParentType;

    public NContext TypeContext;

    public List<NMethod> Methods = new ArrayList<NMethod>();
    public List<NField> Fields = new ArrayList<NField>();
    
    public NAssembly Assembly;

    public NType() {
        ParentType = null;
    }

    public NType(NType parentType, NAssembly assembly) {
        ParentType = parentType;
        Assembly = assembly;
        
        TypeContext = new TypeContext(Assembly, this);
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

    public void addField(NField field) {
        Fields.add(field);
    }

    public void addMethod(NMethod method) {
        Methods.add(method);
    }

    public boolean acceptsCast(NType other) {
        return other.isChildOf(this);
    }

    public boolean isChildOf(NType possibleParent) {
        NType castType = this;

        while(castType != NType.Null) {
            if(castType == possibleParent) {
                return true;
            }

            castType = castType.ParentType;
        }
        return false;
    }
}
