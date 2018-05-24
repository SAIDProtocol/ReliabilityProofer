package edu.rutgers.winlab.reliabilityproofer;

import java.util.List;

/**
 *
 * @author Jiachen Chen
 */
public abstract class Node {

    private final String name;

    public Node(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public abstract void addPossibleEvents(List<IEvent> potentials);

    public abstract void act(IEvent e);

    public abstract void pushState();

    public abstract void popState();

    public abstract void handleDataArrive(Node src, Data d);

    public abstract void handleSubscribe(Node other, boolean subscribe);

    @Override
    public String toString() {
        return getName();
    }

}
