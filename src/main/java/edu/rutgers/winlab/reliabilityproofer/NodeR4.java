package edu.rutgers.winlab.reliabilityproofer;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Jiachen Chen
 */
public class NodeR4 extends Node {

    private Node r3;
    private final Stack<Object[]> statusStack = new Stack<>();

    private boolean r3UnSubscribed = false;
    private ArrayDeque<Data> queue = new ArrayDeque<>();

    public NodeR4() {
        super("R4");
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
        assert d == eps.getData() : String.format("Cannot send %s, since it is not queue head (%s)", eps.getData(), d);
        r3.handleDataArrive(this, eps.getData());
    }

    @Override
    public void pushState() {
        statusStack.push(new Object[]{r3UnSubscribed, new ArrayDeque<>(queue)});
//        System.out.printf("R4 push state, queue len: %d%n", queue.size());
    }

    @Override
    public void popState() {
        Object[] status = statusStack.pop();
        r3UnSubscribed = (Boolean) status[0];
        queue = new ArrayDeque<>((ArrayDeque<Data>) status[1]);
//        System.out.printf("R4 pop state, queue len: %d%n", queue.size());
    }

    @Override
    public void handleDataArrive(Node src, Data d) {
        if (!r3UnSubscribed) {
//            System.out.printf("R4 handle %s, queue: %d%n", d, queue.size());
            queue.offer(d);
//            System.out.printf("R4 finish handle %s, queue: %d%n", d, queue.size());
        }
    }

    @Override
    public void handleSubscribe(Node other, boolean subscribe) {
        assert other == r3 && !subscribe : "R4 can only handle UnSubscribe from R3";
        r3UnSubscribed = true;
    }

    @Override
    public void addPossibleEvents(List<IEvent> potentials) {
        if (!r3UnSubscribed) {
            Data d = queue.peek();
            if (d != null) {
                potentials.add(new EventPacketSent(this, r3, d));
            }
        }
    }

}
