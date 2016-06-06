package nfinity.nfinity.ncontext;

import nfinity.nfinity.exceptions.NTypeCannotExtendException;
import nfinity.nfinity.nmember.*;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.nsignature.request.NSigRequest;
import nfinity.nfinity.ntype.NType;

import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public abstract class NContext {
    public NAssembly Assembly;

    public abstract List<NField> Fields();

    public abstract List<NMethod> Methods();

    public abstract boolean canAccess(NMember member);

    public abstract NType getType();

    public NContext() {
        Assembly = null;
    }

    public NContext(NAssembly assembly) {
        Assembly = assembly;
    }

    public abstract NType createType(String typepath) throws NTypeCannotExtendException;

    public NMember addMember(NSignature signature) {
        if(signature.Args != null) {
            NMethod method = new NMethod(this, signature);
            Methods().add(method);
            return method;
        } else {
            NField field = new NField(this, signature);
            Fields().add(field);
            return field;
        }
    }

    public abstract NMember resolveMember(NSigRequest request);
}
