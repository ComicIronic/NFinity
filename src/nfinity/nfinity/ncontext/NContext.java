package nfinity.nfinity.ncontext;

import nfinity.nfinity.nmember.*;
import nfinity.nfinity.nassembly.NAssembly;

import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public abstract class NContext {
    public NAssembly Assembly;

    public abstract List<NField> Fields();

    public abstract List<NMethod> Methods();

    public abstract boolean canAccess(NMember member);

    public NContext() {
        Assembly = null;
    }

    public NContext(NAssembly assembly) {
        Assembly = assembly;
    }
}
