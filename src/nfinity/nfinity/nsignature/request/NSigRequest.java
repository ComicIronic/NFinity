package nfinity.nfinity.nsignature.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert description here
 *
 * @author Comic
 * @since 05/06/2016 2016
 */
public class NSigRequest {
    public String MemberName;

    public List<NArgRequest> Args = new ArrayList<NArgRequest>();

    public boolean isField() {
        return _isfield;
    }

    private boolean _isfield = false;

    public NSigRequest(String name) {
        MemberName = name;
        _isfield = true;
    }

    public NSigRequest(String name, NArgRequest ... args) {
        MemberName = name;
        Args = new ArrayList<NArgRequest>() {};
    }
}
