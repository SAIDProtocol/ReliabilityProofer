package edu.rutgers.winlab.reliabilityproofer;

import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Jiachen Chen
 */
public class NodeR3 extends Node {

    private Node r2, r4;
    private EventSubscribe subR2, unsubR4;

    private boolean markReceived = false,
            subscribedToR2 = false,
            unsubscribedFromR4 = false,
            receivedFromR2 = false;
    private HashSet<Data> received = new HashSet<>();

    private final Stack<Object[]> statusStack = new Stack<>();

    public NodeR3() {
        super("R3");
    }

    public Node getR2() {
        return r2;
    }

    public void setR2(Node r2) {
        this.r2 = r2;
        subR2 = new EventSubscribe(this, r2, true);
    }

    public Node getR4() {
        return r4;
    }

    public void setR4(Node r4) {
        this.r4 = r4;
        unsubR4 = new EventSubscribe(this, r4, false);
    }

    @Override
    public void addPossibleEvents(List<IEvent> potentials) {
        if (markReceived) {
            if (subscribedToR2) {
                if (receivedFromR2) {
                    if (unsubscribedFromR4) {
                        // finished everything, do nothing
                    } else {
                        // received from R2, unsubscribe from R4
                        potentials.add(unsubR4);
                    }
                } else {
                    // subscribed to r2, but not received from r2, do nothing
                }
            } else {
                //subscribe to r2
                potentials.add(subR2);
            }
        } else {
            //do nothing before receiving mark
        }
    }

    @Override
    public void act(IEvent e) {
        EventSubscribe es = (EventSubscribe) e;
        if (es == subR2) {
            subscribedToR2 = true;
            r2.handleSubscribe(this, true);
        } else {
            unsubscribedFromR4 = true;
            r4.handleSubscribe(this, false);
        }
    }

    public int getDataReceivedCount() {
        return received.size();
    }

    @Override
    public void pushState() {
        statusStack.push(new Object[]{markReceived, subscribedToR2, receivedFromR2, unsubscribedFromR4, new HashSet<>(received)});
    }

    @Override
    public void popState() {
        Object[] data = statusStack.pop();
        markReceived = (Boolean) data[0];
        subscribedToR2 = (Boolean) data[1];
        receivedFromR2 = (Boolean) data[2];
        unsubscribedFromR4 = (Boolean) data[3];
        received = new HashSet<>((HashSet<Data>) data[4]);
    }

    @Override
    public void handleDataArrive(Node src, Data d) {
        if (d instanceof DataMark) {
            markReceived = true;
        } else {
            received.add(d);
            if (src == r2) {
                receivedFromR2 = true;
            }
        }
    }

    @Override
    public void handleSubscribe(Node other, boolean subscribe) {
        assert false : "R3 does not handle subscrptions";
    }

}
