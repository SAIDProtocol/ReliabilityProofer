package edu.rutgers.winlab.reliabilityproofer;

/**
 *
 * @author Jiachen Chen
 */
public class EventPacketSent extends IEvent {

    private final Node receiver;
    private final Data data;

    public EventPacketSent(Node actor, Node receiver, Data data) {
        super(actor);
        this.receiver = receiver;
        this.data = data;
    }

    public Node getReceiver() {
        return receiver;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("Snd: %s->%s %s", getActor(), receiver, data);
    }

}
