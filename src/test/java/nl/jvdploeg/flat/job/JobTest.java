package nl.jvdploeg.flat.job;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.flat.application.TestApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JobTest {

  private static final String CAR_ONE = "one";
  private static final String CAR_OTHER = "other";

  private TestApplication application;
  private Model model;

  @Before
  public void before() throws Exception {
    application = new TestApplication();
    application.open();
    model = application.getModel();

    Response response = application.execute(new AddCarJob(CAR_ONE));
    Assert.assertTrue(response.isSuccessful());
    response = application.execute(new AddCarJob(CAR_OTHER));
    Assert.assertTrue(response.isSuccessful());
  }

  @Test
  public void testMoveLeftThreeTimes() {
    Response response = application.execute(new MoveLeftJob(TestApplication.car(CAR_ONE)));

    Assert.assertTrue(response.isSuccessful());
    Assert.assertEquals("2", model.getNode(TestApplication.carLane(CAR_ONE)).getValue());

    response = application.execute(new MoveLeftJob(TestApplication.car(CAR_ONE)));

    Assert.assertTrue(response.isSuccessful());
    Assert.assertEquals("3", model.getNode(TestApplication.carLane(CAR_ONE)).getValue());

    response = application.execute(new MoveLeftJob(TestApplication.car(CAR_ONE)));

    Assert.assertFalse(response.isSuccessful());
    Assert.assertEquals("3", model.getNode(TestApplication.carLane(CAR_ONE)).getValue());
  }

  @Test
  public void testNop() {
    final Response response = application.execute(new NopJob());

    Assert.assertTrue(response.isSuccessful());
    // model is unchanged
    Assert.assertEquals("3", model.getNode(TestApplication.ROAD_LANES).getValue());
    Assert.assertEquals("1", model.getNode(TestApplication.carLane(CAR_ONE)).getValue());
    Assert.assertEquals("1", model.getNode(TestApplication.carLane(CAR_OTHER)).getValue());
  }

  @Test
  public void testSwerveOne() {
    final Response response = application.execute(new SwerveJob(TestApplication.car(CAR_ONE)));

    Assert.assertTrue(response.isSuccessful());
    Assert.assertEquals("3", model.getNode(TestApplication.ROAD_LANES).getValue());
    Assert.assertEquals("2", model.getNode(TestApplication.carLane(CAR_ONE)).getValue());
    Assert.assertEquals("1", model.getNode(TestApplication.carLane(CAR_OTHER)).getValue());
  }
}
