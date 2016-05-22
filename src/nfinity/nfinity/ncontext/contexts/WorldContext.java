package nfinity.nfinity.ncontext.contexts;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.*;
import nfinity.nfinity.ntype.NType;

import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class WorldContext extends NContext {
    public List<NMethod> Methods() {
        return NAssembly.World.getMethods();
    }

    public List<NField> Fields() {
        return NAssembly.World.getFields();
    }

    public boolean canAccess(NMember member) {
        return Methods().contains(member) || Fields().contains(member);
    }
}
