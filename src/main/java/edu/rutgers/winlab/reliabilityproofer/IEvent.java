package edu.rutgers.winlab.reliabilityproofer;

/**
 *
 * @author Jiachen Chen
 */
public class IEvent {

    private final Node actor;

    public IEvent(Node actor) {
        this.actor = actor;
    }

    public Node getActor() {
        return actor;
    }

}
