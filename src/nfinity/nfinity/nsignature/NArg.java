package nfinity.nfinity.nsignature;

import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nsignature.request.NArgRequest;
import nfinity.nfinity.ntype.NType;

/**
 * Created by Comic on 21/05/2016.
 */
public class NArg {
    NSignature Signature;

    public boolean Optional;

    public NArg(String name, NType type, boolean optional) {
        Signature = new NSignature(name, type, NAccess.Member);
        Optional = optional;
    }

    public NArg(NSignature signature, boolean optional) {
        Signature = signature;
        Optional = optional;
    }

    public boolean acceptsRequest(NArgRequest argRequest) {
        return Signature.ReturnType.acceptsTypeAssign(argRequest.Type) &&
                (Signature.Name == argRequest.Name || argRequest.Name == "");
    }

    public NField getArgField(NContext context) {
        return new NField(context, Signature);
    }
}
