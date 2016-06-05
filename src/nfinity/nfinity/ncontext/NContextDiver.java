package nfinity.nfinity.ncontext;

import java.util.Stack;

/**
 * Insert description here
 *
 * @author Comic
 * @since 04/06/2016 2016
 */
public class NContextDiver {
    public Stack<NContext> Contexts = new Stack<NContext>();

    public NContextDiver(NContext startingContext) {
        diveInto(startingContext);
    }

    public void diveInto (NContext subcontext) {
        Contexts.push(subcontext);
    }

    public void riseUp() {
        Contexts.pop();
    }

    public NContext currentContext() {
        return Contexts.elementAt(0);
    }
}
