package nfinity.nfinity.nsignature;

import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nsignature.request.NArgRequest;
import nfinity.nfinity.ntype.NType;

import java.security.Signature;

/**
 * Created by Comic on 21/05/2016.
 */
public class NArg {
    NField ArgField;

    public boolean Optional;

    public NArg(NContext ownerContext, String name, NType type, boolean optional) {
        ArgField = (NField)ownerContext.addMember(new NSignature(name, type, NAccess.Member));
        Optional = optional;
    }

    public boolean acceptsRequest(NArgRequest argRequest) {
        return ArgField.Signature.ReturnType.acceptsCast(argRequest.Type) &&
                (ArgField.Signature.Name == argRequest.Name || argRequest.Name == "");
    }
}
