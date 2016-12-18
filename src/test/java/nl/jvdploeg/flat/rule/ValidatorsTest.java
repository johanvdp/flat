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
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.Car;
import nl.jvdploeg.flat.application.Road;
import nl.jvdploeg.flat.application.TestApplication;
import nl.jvdploeg.flat.message.MessageNode;

public class ValidatorsTest {

    private Model model;
    private Validators validators;
    private TestApplication application;

    @Before
    public void before() {
        application = new TestApplication();
        model = application.createModel();
        validators = application.createValidators();
        extendModel();
    }

    @After
    public void after() throws IOException {
        application.close();
    }

    @Test
    public void testCarLaneApplicable() {
        final List<Change> changes = new ArrayList<>();
        changes.add(DefaultChange.set(Car.carLane("one"), "4"));

        final Model status = validators.validate(model, changes);

        Assert.assertEquals(1, status.getRoot().getChildren().size());
        Assert.assertEquals("LaneValidator-one", status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ID)).getValue());
        Assert.assertEquals("ERROR", status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.SEVERITY)).getValue());
        Assert.assertEquals("road has {} lanes, car {} can not be at lane {}",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.MESSAGE)).getValue());
        Assert.assertEquals("3",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ARGUMENTS).createChildPath("0")).getValue());
        Assert.assertEquals("one",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ARGUMENTS).createChildPath("1")).getValue());
        Assert.assertEquals("4",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ARGUMENTS).createChildPath("2")).getValue());
    }

    @Test
    public void testNoChanges() {
        final List<Change> changes = new ArrayList<>();

        final Model status = validators.validate(model, changes);

        Assert.assertEquals(0, status.getRoot().getChildren().size());
    }

    @Test
    public void testRoadLanesApplicable() {
        final List<Change> changes = new ArrayList<>();
        model.setValue(Car.carLane("one"), "4");
        changes.add(DefaultChange.set(Road.ROAD_LANES, "2"));

        final Model status = validators.validate(model, changes);

        Assert.assertEquals(1, status.getRoot().getChildren().size());
        Assert.assertEquals("LaneValidator-one", status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ID)).getValue());
        Assert.assertEquals("ERROR", status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.SEVERITY)).getValue());
        Assert.assertEquals("road has {} lanes, car {} can not be at lane {}",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.MESSAGE)).getValue());
        Assert.assertEquals("2",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ARGUMENTS).createChildPath("0")).getValue());
        Assert.assertEquals("one",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ARGUMENTS).createChildPath("1")).getValue());
        Assert.assertEquals("4",
                status.getNode(new Path("LaneValidator-one").createChildPath(MessageNode.ARGUMENTS).createChildPath("2")).getValue());
    }

    protected void extendModel() {
        model.add(Car.car("one"));
        model.add(Car.carLane("one"));
        model.setValue(Car.carLane("one"), "1");

        model.add(Car.car("other"));
        model.add(Car.carLane("other"));
        model.setValue(Car.carLane("other"), "1");
    }
}
