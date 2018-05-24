package edu.rutgers.winlab.reliabilityproofer;

/**
 *
 * @author Jiachen Chen
 */
public class EventSubscribe extends IEvent {

    private final Node prevHop;
    private final boolean subscribe;

    public EventSubscribe(Node actor, Node prevHop, boolean subscribe) {
        super(actor);
        this.prevHop = prevHop;
        this.subscribe = subscribe;
    }

    public Node getPrevHop() {
        return prevHop;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    @Override
    public String toString() {
        return String.format("%sSub: %s->%s", subscribe ? "" : "Un", prevHop, getActor());
    }

}
