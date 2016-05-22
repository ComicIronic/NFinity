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
    public static NType Null = new Null();

    public static NType World = new World();

    public static NType Datum = new Datum();

    public static NType Num = new Num();

    public static NType String = new NString();

    public static NType Bool = new NBool();

    public static NType NList = new NList();

    public static NType Atom = new Atom();

    public static NType Movable = new Movable();

    public static NType Area = new Area();

    public static NType Turf = new Turf();

    public static NType Obj = new Obj();

    public static NType Mob = new Mob();

    public static List<NType> Types = new ArrayList<NType>();

    public static NContext WorldContext = new WorldContext();

    static {
        Types.addAll(Arrays.asList(Datum, Num, String, Bool, NList, Atom, Movable, Area, Turf, Obj, Mob));
    }
}
