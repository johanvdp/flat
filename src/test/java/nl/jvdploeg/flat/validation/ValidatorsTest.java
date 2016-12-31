package nl.jvdploeg.flat.validation;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.application.TestApplication;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.Severity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidatorsTest {

  private Model model;
  private Validators validators;
  private TestApplication application;

  @After
  public void after() throws IOException {
    application.close();
  }

  @Before
  public void before() {
    application = new TestApplication();
    model = application.createModel();
    validators = application.createValidators();
    extendModel();
  }

  @Test
  public void testCarLaneApplicable() {
    final List<Change> changes = new ArrayList<>();
    changes.add(DefaultChange.set(TestApplication.carLane("one"), "4"));

    final List<Message> messages = validators.validate(model, changes);

    Assert.assertEquals(1, messages.size());
    Message message = messages.get(0);
    Assert.assertEquals("LaneValidator-one", message.getIdentifier());
    Assert.assertEquals(Severity.ERROR, message.getSeverity());
    Assert.assertEquals("road has {} lanes, car {} can not be at lane {}", message.getMessage());
    Assert.assertEquals(3, message.getArguments().length);
    Assert.assertEquals("3", message.getArguments()[0]);
    Assert.assertEquals("one", message.getArguments()[1]);
    Assert.assertEquals("4", message.getArguments()[2]);
  }

  @Test
  public void testNoChanges() {
    final List<Change> changes = new ArrayList<>();

    final List<Message> messages = validators.validate(model, changes);

    Assert.assertEquals(0, messages.size());
  }

  @Test
  public void testRoadLanesApplicable() {
    final List<Change> changes = new ArrayList<>();
    model.setValue(TestApplication.carLane("one"), "4");
    changes.add(DefaultChange.set(TestApplication.ROAD_LANES, "2"));

    final List<Message> messages = validators.validate(model, changes);

    Assert.assertEquals(1, messages.size());
    Message message = messages.get(0);
    Assert.assertEquals("LaneValidator-one", message.getIdentifier());
    Assert.assertEquals(Severity.ERROR, message.getSeverity());
    Assert.assertEquals("road has {} lanes, car {} can not be at lane {}", message.getMessage());
    Assert.assertEquals(3, message.getArguments().length);
    Assert.assertEquals("2", message.getArguments()[0]);
    Assert.assertEquals("one", message.getArguments()[1]);
    Assert.assertEquals("4", message.getArguments()[2]);
  }

  protected void extendModel() {
    model.add(TestApplication.car("one"));
    model.add(TestApplication.carLane("one"));
    model.setValue(TestApplication.carLane("one"), "1");

    model.add(TestApplication.car("other"));
    model.add(TestApplication.carLane("other"));
    model.setValue(TestApplication.carLane("other"), "1");
  }
}
