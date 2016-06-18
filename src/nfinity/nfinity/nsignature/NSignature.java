package nfinity.nfinity.nsignature;

import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.nsignature.request.NArgRequest;
import nfinity.nfinity.nsignature.request.NSigRequest;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;

/**
 * Created by Comic on 21/05/2016.
 */
public class NSignature {
    public String Name;

    public NType ReturnType;

    public List<NArg> Args;

    public NAccess Access;

    public NSignature(String name, NType returnType, NAccess access) {
        Name = name;

        ReturnType = returnType;

        Access = access;

        Args = new ArrayList<NArg>();
    }

    public NSignature(String name, NType returnType, NAccess access, NArg ... args) {
        Name = name;

        ReturnType = returnType;

        Access = access;

        if(args.length > 0) {
            Args = Arrays.asList(args);
        }
    }

/*    public NSignature(String name, NType returnType, NAccess access, NType ... types) {
        Name = name;

        ReturnType = returnType;

        Access = access;

        if(types.length > 0) {
            Args = new ArrayList<NArg>();

            for(NType type : types) {
                Args.add(new NArg(type, false));
            }
        }
    }*/

    public int nonOptionalSize() {
        int i = 0;

        for(NArg arg : Args) {
            if(arg.Optional) {
                break;
            }
            i++;
        }

        return i;
    }

    public NSignature degenerize(NType generic, NType replacement) {
        NSignature degenerized = new NSignature(Name, ReturnType, Access, Args.toArray(new NArg[Args.size()]));

        if(ReturnType == generic) {
            degenerized.ReturnType = replacement;
        }

        for(NArg arg : degenerized.Args) {
            if(arg.ArgField.Signature.ReturnType == generic) {
                arg.ArgField.Signature.ReturnType = replacement;
            }
        }

        return degenerized;
    }

/*    public boolean accepts(NSignature other) {
        if(ReturnType.acceptsTypeAssign(other.ReturnType)) {
            //Can't have a non-optional arg
            if(other.Args.size() > this.nonOptionalSize()) {
                return false;
            }

            for(int i = 1; i < other.Args.size(); i++) {
                if(!Args.get(i).accepts(other.Args.get(i).Type)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }*/

    public boolean acceptsRequest(NSigRequest request) {
        if(Name == request.MemberName) {
            for(NArgRequest argRequest : request.Args) {
                if(!StringUtils.isNullOrBlank(argRequest.Name)) {

                } else {

                }
            }

            return true;
        } else {
            return false;
        }
    }
}
