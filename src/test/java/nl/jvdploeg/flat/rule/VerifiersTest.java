package nl.jvdploeg.flat.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.Car;
import nl.jvdploeg.flat.application.TestApplication;
import nl.jvdploeg.flat.message.AssertMessage;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Severity;

public class VerifiersTest {

    private Model model;
    private Verifiers verifiers;
    private TestApplication application;

    @Before
    public void before() {
        application = new TestApplication();
        model = application.createModel();
        verifiers = application.createVerifiers();
        defineModel();
    }

    @After
    public void after() throws IOException {
        application.close();
    }

    @Test
    public void testNoChanges() {
        final List<Change> changes = new ArrayList<>();

        verifiers.verify(model, changes);

        final Model status = verifiers.getStatus();
        Assert.assertEquals(0, status.getRoot().getChildren().size());
    }

    @Test
    public void testOneMessage() {
        final List<Change> changes = new ArrayList<>();
        changes.add(DefaultChange.set(Car.carLane("one"), "2"));

        verifiers.verify(model, changes);

        final Model status = verifiers.getStatus();
        Assert.assertEquals(1, status.getRoot().getChildren().size());
        AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING, "car {} is at lane {}", new String[] { "one", "2" }),
                status.getNode(new Path("LaneOneVerifier-one")));
    }

    @Test
    public void testTwoMessageModifyOne() {
        final List<Change> changes = new ArrayList<>();
        changes.add(DefaultChange.set(Car.carLane("one"), "2"));
        changes.add(DefaultChange.set(Car.carLane("other"), "3"));

        verifiers.verify(model, changes);
        ModelUtils.applyChanges(model, changes);

        changes.clear();
        changes.add(DefaultChange.set(Car.carLane("other"), "2"));

        verifiers.verify(model, changes);

        final Model messages = verifiers.getStatus();
        Assert.assertEquals(2, messages.getRoot().getChildren().size());
        AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING, "car {} is at lane {}", new String[] { "one", "2" }),
                messages.getNode(new Path("LaneOneVerifier-one")));
        AssertMessage.assertEquals(
                new DefaultMessage("LaneOneVerifier-other", Severity.WARNING, "car {} is at lane {}", new String[] { "other", "2" }),
                messages.getNode(new Path("LaneOneVerifier-other")));
    }

    @Test
    public void testTwoMessages() {
        final List<Change> changes = new ArrayList<>();
        changes.add(DefaultChange.set(Car.carLane("one"), "2"));
        changes.add(DefaultChange.set(Car.carLane("other"), "3"));

        verifiers.verify(model, changes);

        final Model status = verifiers.getStatus();
        Assert.assertEquals(2, status.getRoot().getChildren().size());
        AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING, "car {} is at lane {}", new String[] { "one", "2" }),
                status.getNode(new Path("LaneOneVerifier-one")));
        AssertMessage.assertEquals(
                new DefaultMessage("LaneOneVerifier-other", Severity.WARNING, "car {} is at lane {}", new String[] { "other", "3" }),
                status.getNode(new Path("LaneOneVerifier-other")));
    }

    @Test
    public void testTwoMessagesRemoveOne() {
        final List<Change> changes = new ArrayList<>();
        changes.add(DefaultChange.set(Car.carLane("one"), "2"));
        changes.add(DefaultChange.set(Car.carLane("other"), "3"));

        verifiers.verify(model, changes);
        ModelUtils.applyChanges(model, changes);

        changes.clear();
        changes.add(DefaultChange.set(Car.carLane("other"), "1"));

        verifiers.verify(model, changes);

        final Model status = verifiers.getStatus();
        Assert.assertEquals(1, status.getRoot().getChildren().size());
        AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING, "car {} is at lane {}", new String[] { "one", "2" }),
                status.getNode(new Path("LaneOneVerifier-one")));
    }

    protected void defineModel() {
        model.add(Car.car("one"));
        model.add(Car.carLane("one"));
        model.setValue(Car.carLane("one"), "1");
        model.add(Car.car("other"));
        model.add(Car.carLane("other"));
        model.setValue(Car.carLane("other"), "1");
    }
}
