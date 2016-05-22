package nfinity.nfinity.ntype.core;

import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.generic.Nullable;

/**
 * Created by Comic on 21/05/2016.
 */
public class Null extends NType {
    public boolean canCastTo(NType other) {
        return (other instanceof Nullable);
    }
}
