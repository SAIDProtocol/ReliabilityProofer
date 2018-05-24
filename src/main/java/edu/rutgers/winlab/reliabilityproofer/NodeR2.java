package edu.rutgers.winlab.reliabilityproofer;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Jiachen Chen
 */
public class NodeR2 extends Node {

    private Node r3;
    private final Stack<Object[]> statusStack = new Stack<>();

    private boolean r3Subscribed = false;
    private ArrayDeque<Data> queue = new ArrayDeque<>();

    public NodeR2() {
        super("R2");
    }

    public Node getR3() {
        return r3;
    }

    public void setR3(Node r3) {
        this.r3 = r3;
    }

    @Override
    public void act(IEvent e) {
        EventPacketSent eps = (EventPacketSent) e;
        Data d = queue.poll();
        assert d == eps.getData() : String.format("Cannot send packet %s since it's not queue head (%s)", eps.getData(), d);
        r3.handleDataArrive(this, eps.getData());
    }

    @Override
    public void pushState() {
        statusStack.push(new Object[]{r3Subscribed, new ArrayDeque<>(queue)});
    }

    @Override
    public void popState() {
        Object[] status = statusStack.pop();
        r3Subscribed = (Boolean) status[0];
        queue = new ArrayDeque<>((ArrayDeque<Data>) status[1]);
    }

    @Override
    public void handleDataArrive(Node src, Data d) {
        if (r3Subscribed) {
            queue.offer(d);
        }
    }

    @Override
    public void handleSubscribe(Node other, boolean subscribe) {
        assert other == r3 && subscribe : "Only accepts subscribe from r3";
        r3Subscribed = true;
    }

    @Override
    public void addPossibleEvents(List<IEvent> potentials) {
        Data d = queue.peek();
        if (d != null) {
            potentials.add(new EventPacketSent(this, r3, d));
        }
    }

}
