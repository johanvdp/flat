package nl.jvdploeg.flat.job;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.Car;
import nl.jvdploeg.flat.application.Road;
import nl.jvdploeg.flat.application.TestApplication;
import nl.jvdploeg.flat.message.AssertMessage;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Severity;

public class JobTest {

    private static final String CAR_ONE = "one";
    private static final String CAR_OTHER = "other";

    private TestApplication application;
    private Model model;
    private Model verification;

    @Before
    public void before() {
        application = new TestApplication();
        application.initialize();
        model = application.getModel();
        verification = application.getVerifiersModel();

        application.execute(new AddCarJob(CAR_ONE));
        application.execute(new AddCarJob(CAR_OTHER));
    }

    @Test
    public void testMoveLeftThreeTimes() {
        boolean successful = application.execute(new MoveLeftJob(Car.car(CAR_ONE)));

        Assert.assertTrue(successful);
        Assert.assertEquals("2", model.getNode(Car.carLane(CAR_ONE)).getValue());
        AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING, "car {} is at lane {}", new String[] { CAR_ONE, "2" }),
                verification.getNode(new Path("LaneOneVerifier-one")));

        successful = application.execute(new MoveLeftJob(Car.car(CAR_ONE)));

        Assert.assertTrue(successful);
        Assert.assertEquals("3", model.getNode(Car.carLane(CAR_ONE)).getValue());
        AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING, "car {} is at lane {}", new String[] { CAR_ONE, "3" }),
                verification.getNode(new Path("LaneOneVerifier-one")));

        successful = application.execute(new MoveLeftJob(Car.car(CAR_ONE)));

        Assert.assertFalse(successful);
        Assert.assertEquals("3", model.getNode(Car.carLane(CAR_ONE)).getValue());
        AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING, "car {} is at lane {}", new String[] { CAR_ONE, "3" }),
                verification.getNode(new Path("LaneOneVerifier-one")));
    }

    @Test
    public void testNop() {
        final boolean successful = application.execute(new NopJob());

        Assert.assertTrue(successful);
        // model is unchanged
        Assert.assertEquals("3", model.getNode(Road.ROAD_LANES).getValue());
        Assert.assertEquals("1", model.getNode(Car.carLane(CAR_ONE)).getValue());
        Assert.assertEquals("1", model.getNode(Car.carLane(CAR_OTHER)).getValue());
    }

    @Test
    public void testSwerveOne() {
        final boolean successful = application.execute(new SwerveJob(Car.car(CAR_ONE)));

        Assert.assertTrue(successful);
        Assert.assertEquals("3", model.getNode(Road.ROAD_LANES).getValue());
        Assert.assertEquals("2", model.getNode(Car.carLane(CAR_ONE)).getValue());
        Assert.assertEquals("1", model.getNode(Car.carLane(CAR_OTHER)).getValue());
    }
}
