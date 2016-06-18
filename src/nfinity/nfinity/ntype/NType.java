package nfinity.nfinity.ntype;

import nfinity.nfinity.exceptions.NTypeCannotExtendException;
import nfinity.nfinity.exceptions.NTypeNotFoundException;
import nfinity.nfinity.ntype.core.Null;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.ncontext.contexts.TypeContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Comic on 21/05/2016.
 */
public class NType {
	public static NType Null = new Null(null, null);
	
    public NType ParentType;

    public String TypeName;

    public NContext TypeContext;

    public List<NMethod> Methods = new ArrayList<NMethod>();
    public List<NField> Fields = new ArrayList<NField>();

    public boolean Abstract = false;

    public boolean Final = false;
    
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

    public boolean acceptsTypeAssign(NType other) {
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

    public NType getChildType(String typepath) throws NTypeNotFoundException {
        int first_separator = typepath.indexOf("/");

        String baseName = typepath;

        NType baseType = null;

        //If true, we skip searching steps because we can't find children of a new type
        boolean created = false;

        if(first_separator != -1) {
            baseName = typepath.substring(0, first_separator);
        }

        for(NType child : children()) {
            if(child.TypeName == baseName) {
                return child.getChildType(typepath.substring(first_separator + 1));
            }
        }

        throw new NTypeNotFoundException(typepath() + "/" + baseName);
    }

    public NType getOrCreateChildType(String typepath) throws NTypeCannotExtendException {
        try {
            return getChildType(typepath);
        }
        catch (NTypeNotFoundException e) {
            //This is technically handled below, but let's save legwork here
            if(Final) {
                throw new NTypeCannotExtendException(typepath());
            }

            int first_separator = typepath.indexOf("/");

            String baseName = typepath;

            NType baseType = null;

            if(first_separator != -1) {
                baseName = typepath.substring(0, first_separator);
            }

            for (NType type : children()) {
                if (type.TypeName == baseName) {
                    baseType = type;
                }
            }

            if(baseType == null) {
                baseType = new NType(this, Assembly);
                baseType.TypeName = baseName;
                Assembly.Types.add(baseType);
            }

            return baseType.getOrCreateChildType(typepath.substring(first_separator + 1));
        }
    }

    public List<NType> children() {
        return Assembly.childrenOf(this);
    }

    /**
     * Gets the number of types deep this type is - 1 is base
     * @return
     */
    public int depth() {
        return parentage().size();
    }

    /**
     * Creates the formatted typepath for this type
     * @return
     */
    public String typepath() {
        Stack<String> typenames = new Stack<String>();


        for(NType type : this.parentage()) {
            typenames.push(type.TypeName);
        }

        StringBuilder pathBuilder = new StringBuilder();


        while(!typenames.isEmpty()) {
            pathBuilder.append('/').append(typenames.pop());
        }

        return pathBuilder.toString();
    }

    /**
     * Returns the list of parent types leading to this type - this type is included
     * @return
     */
    public List<NType> parentage() {
        List<NType> parentType = new ArrayList<NType>();
        NType parent = this;

        while(parent != NType.Null) {
            parentType.add(parent);
            parent = parent.ParentType;
        }

        return parentType;
    }
}
