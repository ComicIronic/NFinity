package nfinity.nfinity.nsignature.request;

import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.core.Null;

/**
 * Insert description here
 *
 * @author Comic
 * @since 05/06/2016 2016
 */
public class NArgRequest {
    public static NArgRequest Empty = new NArgRequest(Null.Null);

    public String Name;

    public NType Type;

    public NArgRequest(NType type) {
        Type = type;
    }

    public NArgRequest(String name, NType type) {
        Name = name;
        Type = type;
    }
}
