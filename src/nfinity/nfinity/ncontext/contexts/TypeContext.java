package nfinity.nfinity.ncontext.contexts;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.ntype.NType;

import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class TypeContext extends NContext {
    public NType Type;

    public TypeContext(NType type) {
        Type = type;
    }

    public List<NMethod> Methods() {
        return NAssembly.World.getMethods();
    }

    public List<NField> Fields() {
        return NAssembly.World.getFields();
    }
}
