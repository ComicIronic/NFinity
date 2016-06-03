package nfinity.nfinity.nassembly;

import nfinity.nfinity.exceptions.NTypeCannotExtendException;
import nfinity.nfinity.exceptions.NTypeNotFoundException;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.ncontext.contexts.WorldContext;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.core.*;
import nfinity.nfinity.ntype.generic.list.NList;
import nfinity.nfinity.ntype.primitive.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class NAssembly {
    public NType Null;

    public NType World;

    public NType Datum;

    public NType Num;

    public NType String;

    public NType Bool;

    public NType Typepath;
    
    public NType Any;

    public NType NList;

    public NType Atom;

    public NType Movable;

    public NType Area;

    public NType Turf;

    public NType Obj;

    public NType Mob;

    public List<NType> Types = new ArrayList<NType>();

    public NContext WorldContext;
    
    {
    	reset();
    }

    public void reset() {
    	Types.clear();
    	
    	WorldContext = new WorldContext(this);
    	
    	Null = NType.Null;
    	
    	World = new World(Null, this);
    	
        Datum = new Datum(Null, this);

        Num = new NNum(Null, this);

        String = new NString(Null, this);

        Bool = new NBool(Null, this);

        Typepath = new NTypepath(Null, this);
        
        Any = new Any(Null, this);

        NList = new NList(Null, this);

        Atom = new Atom(Datum, this);

        Movable = new Movable(Atom, this);

        Area = new Area(Atom, this);

        Turf = new Turf(Atom, this);

        Obj = new Obj(Movable, this);

        Mob = new Mob(Movable, this);
    	
        Types.addAll(Arrays.asList(Datum, Num, String, Bool, Typepath, NList, Atom, Movable, Area, Turf, Obj, Mob));
    }

    /**
     * Finds the type of a properly given path, or creates the type
     * @param typepath
     * @return
     */
    public NType getTypeOrCreateInPath(String typepath) throws NTypeCannotExtendException {
        try {
            return getTypeInPath(typepath);
        } catch (NTypeNotFoundException e) {
        }

        int first_separator = typepath.indexOf("/");

        String baseName = typepath;

        NType baseType = null;

        //If true, we skip searching steps because we can't find children of a new type
        boolean created = false;

        if(first_separator != -1) {
            baseName = typepath.substring(0, first_separator);
        }

        for (NType type : this.Types) {
            if (type.TypeName == baseName) {
                baseType = type;
            }
        }

        if(baseType == null) {
            //All new user-defined types inherit datum
            baseType = new NType(Datum, this);
            baseType.TypeName = baseName;
            created = true;
        }

        String[] pathparts = typepath.split("/");

        if(baseType.TypeName != typepath) {
            return getChildTypeOrCreateInPath(baseType, typepath);
        } else {
            return baseType;
        }
    }

    public NType getChildTypeOrCreateInPath(NType parentType, String typepath) throws NTypeCannotExtendException {
        try {
            return getChildTypeInPath(parentType, typepath);
        } catch (NTypeNotFoundException e) {
        }

        //This is technically handled below, but let's save legwork here
        if(parentType.Final) {
            throw new NTypeCannotExtendException(parentType.typepath());
        }

        String[] pathparts = typepath.split("/");

        NType currentType = parentType;

        boolean created = false;

        for(int step = 1; step < pathparts.length; step++) {
            String typename = pathparts[step];

            if(!created) {
                try {
                    currentType = getChildTypeInPath(currentType, pathparts[step]);

                } catch (NTypeNotFoundException e) {

                    if(currentType.Final) {
                        throw new NTypeCannotExtendException(currentType.typepath());
                    }

                    //Creates the child type
                    currentType = new NType(currentType, this);
                    currentType.TypeName = pathparts[step];
                    created = true;
                }
            } else {
                currentType = new NType(currentType, this);
                currentType.TypeName = pathparts[step];
            }
        }

        return currentType;
    }

    /**
     * Finds the type of a properly given path
     * @param typepath
     * @return
     * @throws NTypeNotFoundException
     */
    public NType getTypeInPath(String typepath) throws NTypeNotFoundException {
        int first_separator = typepath.indexOf("/");

        String baseName = typepath;

        NType baseType = null;

        if(first_separator != -1) {
            baseName = typepath.substring(0, first_separator);
        }

        for (NType type : this.Types) {
            if (type.TypeName == baseName) {
                baseType = type;
            }
        }

        if(baseType == null) {
            throw new NTypeNotFoundException(typepath);
        }

        if(baseName != typepath) {
            return getChildTypeInPath(baseType, typepath.substring(first_separator + 1));
        } else {
            return baseType;
        }
    }

    /**
     * Gets the type of the child of the given type with the relative path
     * @param parentType
     * @param path
     * @return
     * @throws NTypeNotFoundException
     */
    public NType getChildTypeInPath(NType parentType, String path) throws NTypeNotFoundException {
        String[] pathparts = path.split("/");

        NType currentType = parentType;

        for(int step = 1; step < pathparts.length; step++) {
            String typename = pathparts[step];

            NType found = null;

            for(NType child : childrenOf(currentType)) {
                if(child.TypeName == typename) {
                    found = child;
                    break;
                }
            }

            if(found == null) {
                throw new NTypeNotFoundException(parentType.typepath() + "/" + path);
            }

            currentType = found;
        }

        return currentType;
    }

    public List<NType> childrenOf(NType parent) {
        List<NType> children = new ArrayList<NType>();

        for(NType type : Types) {
            if(type.ParentType == parent) {
                children.add(type);
            }
        }

        return children;
    }
}
