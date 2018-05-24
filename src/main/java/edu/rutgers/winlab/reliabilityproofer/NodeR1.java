package edu.rutgers.winlab.reliabilityproofer;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Jiachen Chen
 */
public class NodeR1 extends Node {

    private final Data[] toSends;
    private Node r2 = null, r4 = null;

    private ArrayDeque<EventPacketSent> toSendsR2 = new ArrayDeque<>();
    private ArrayDeque<EventPacketSent> toSendsR4 = new ArrayDeque<>();
    private final Stack<Object[]> statusStack = new Stack<>();

    public NodeR1(Data[] toSends) {
        super("R1");
        this.toSends = toSends;
    }

    public void setR2(Node r2) {
        assert this.r2 == null : "Cannot re set R2";
        this.r2 = r2;
        for (Data toSend : toSends) {
            toSendsR2.offer(new EventPacketSent(this, r2, toSend));
        }
    }

    public void setR4(Node r4) {
        assert this.r4 == null : "Cannot re set R4";
        this.r4 = r4;
        toSendsR4.offer(new EventPacketSent(this, r4, new DataMark()));
        for (Data toSend : toSends) {
            toSendsR4.offer(new EventPacketSent(this, r4, toSend));
        }
    }

    @Override
    public void addPossibleEvents(List<IEvent> potentials) {
        EventPacketSent paR2 = toSendsR2.peek();
        if (paR2 != null) {
            potentials.add(paR2);
        }
        EventPacketSent paR4 = toSendsR4.peek();
        if (paR4 != null) {
            potentials.add(paR4);
        }
    }

    @Override
    public void act(IEvent e) {
        EventPacketSent eps = (EventPacketSent) e;
        if (eps == toSendsR2.peek()) {
            r2.handleDataArrive(this, eps.getData());
            toSendsR2.removeFirst();
        } else if (eps == toSendsR4.peek()) {
            r4.handleDataArrive(this, eps.getData());
            toSendsR4.removeFirst();
        }
    }

    @Override
    public void pushState() {
        statusStack.push(new Object[]{new ArrayDeque<>(toSendsR2), new ArrayDeque<>(toSendsR4)});
    }

    @Override
    public void popState() {
        Object[] states = statusStack.pop();
        toSendsR2 = new ArrayDeque<>((ArrayDeque<EventPacketSent>) states[0]);
        toSendsR4 = new ArrayDeque<>((ArrayDeque<EventPacketSent>) states[1]);
    }

    @Override
    public void handleDataArrive(Node src, Data d) {
        assert false : String.format("R1 should not receive %s from %s", d, src);
    }

    @Override
    public void handleSubscribe(Node other, boolean subscribe) {
        assert false : String.format("R1 should not handle %sSubscribe from %s", subscribe ? "" : "Un", other);
    }

}
