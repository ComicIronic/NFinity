package nfinity.nfinity.nassembly;

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
        
        Any = new Any(Null, this);

        NList = new NList(Null, this);

        Atom = new Atom(Datum, this);

        Movable = new Movable(Atom, this);

        Area = new Area(Atom, this);

        Turf = new Turf(Atom, this);

        Obj = new Obj(Movable, this);

        Mob = new Mob(Movable, this);
    	
        Types.addAll(Arrays.asList(Datum, Num, String, Bool, NList, Atom, Movable, Area, Turf, Obj, Mob));
    }
}
