package edu.rutgers.winlab.reliabilityproofer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jiachen
 */
public class INodeTest {

    public INodeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    static final Data[] PACKETS = new Data[]{new Data("P1"), new Data("P2"), new Data("P3"), new Data("P4")};
    static final int[] COUNTS = new int[PACKETS.length + 1];

    @Test
    public void testSomeMethod() {
        NodeR1 r1 = new NodeR1(PACKETS);
        NodeR2 r2 = new NodeR2();
        NodeR3 r3 = new NodeR3();
        NodeR4 r4 = new NodeR4();
        r1.setR2(r2);
        r1.setR4(r4);
        r2.setR3(r3);
        r3.setR2(r2);
        r3.setR4(r4);
        r4.setR3(r3);
        runNextEvent(Arrays.asList(r1, r2, r3, r4), new Stack<>());
        System.out.printf("Counts=%s%n", Arrays.toString(COUNTS));
    }

    private void runNextEvent(List<Node> nodes, Stack<IEvent> pastEvents) {
        List<IEvent> potentials = new ArrayList<>();
        nodes.forEach(n -> n.addPossibleEvents(potentials));
        if (!potentials.isEmpty()) {
            potentials.forEach(potential -> {
                nodes.forEach(r -> r.pushState());
                pastEvents.push(potential);
                potential.getActor().act(potential);
                runNextEvent(nodes, pastEvents);
                nodes.forEach(r -> r.popState());
                pastEvents.pop();
            });
        } else {
            int c = ((NodeR3) nodes.get(2)).getDataReceivedCount();
            COUNTS[c]++;
//            if (c != packets.length) {
//                System.out.println("===================");
//                pastEvents.forEach(System.out::println);
//            }
        }
    }

}
