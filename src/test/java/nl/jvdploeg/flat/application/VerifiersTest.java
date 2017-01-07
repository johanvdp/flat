package nl.jvdploeg.flat.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.message.AssertMessage;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Severity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VerifiersTest {

  private Model model;
  private LaneOneVerifier verifier;
  private TestApplication application;

  @After
  public void after() throws IOException {
    application.close();
  }

  @Before
  public void before() {
    application = new TestApplication();
    model = application.createModel();
    verifier = new LaneOneVerifier();
    defineModel();
  }

  @Test
  public void testNoChanges() {

    verifier.execute(model);
    final List<Change> changes = verifier.getChanges();
    ModelUtils.applyChanges(model, changes);

    Assert.assertEquals(0, getVerifiersNode().getChildren().size());
  }

  @Test
  public void testOneMessage() {
    List<Change> changes = new ArrayList<>();
    changes.add(DefaultChange.set(TestApplication.carLane("one"), "2"));
    ModelUtils.applyChanges(model, changes);

    verifier.execute(model);
    changes = verifier.getChanges();
    ModelUtils.applyChanges(model, changes);

    Assert.assertEquals(1, getVerifiersNode().getChildren().size());
    AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING,
        "car {} is at lane {}", new String[] { "one", "2" }),
        getVerifierNode("LaneOneVerifier-one"));
  }

  @Test
  public void testTwoMessageModifyOne() {
    List<Change> changes = new ArrayList<>();
    changes.add(DefaultChange.set(TestApplication.carLane("one"), "2"));
    changes.add(DefaultChange.set(TestApplication.carLane("other"), "3"));
    ModelUtils.applyChanges(model, changes);

    verifier.execute(model);
    changes = verifier.getChanges();
    ModelUtils.applyChanges(model, changes);

    changes = new ArrayList<>();
    changes.add(DefaultChange.set(TestApplication.carLane("other"), "2"));
    ModelUtils.applyChanges(model, changes);

    verifier.execute(model);
    changes = verifier.getChanges();
    ModelUtils.applyChanges(model, changes);

    Assert.assertEquals(2, getVerifiersNode().getChildren().size());
    AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING,
        "car {} is at lane {}", new String[] { "one", "2" }),
        getVerifierNode("LaneOneVerifier-one"));
    AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-other", Severity.WARNING,
        "car {} is at lane {}", new String[] { "other", "2" }),
        getVerifierNode("LaneOneVerifier-other"));
  }

  @Test
  public void testTwoMessages() {
    List<Change> changes = new ArrayList<>();
    changes.add(DefaultChange.set(TestApplication.carLane("one"), "2"));
    changes.add(DefaultChange.set(TestApplication.carLane("other"), "3"));
    ModelUtils.applyChanges(model, changes);

    verifier.execute(model);
    changes = verifier.getChanges();
    ModelUtils.applyChanges(model, changes);

    Assert.assertEquals(2, getVerifiersNode().getChildren().size());
    AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING,
        "car {} is at lane {}", new String[] { "one", "2" }),
        getVerifierNode("LaneOneVerifier-one"));
    AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-other", Severity.WARNING,
        "car {} is at lane {}", new String[] { "other", "3" }),
        getVerifierNode("LaneOneVerifier-other"));
  }

  @Test
  public void testTwoMessagesRemoveOne() {
    List<Change> changes = new ArrayList<>();
    changes.add(DefaultChange.set(TestApplication.carLane("one"), "2"));
    changes.add(DefaultChange.set(TestApplication.carLane("other"), "3"));
    ModelUtils.applyChanges(model, changes);

    verifier.execute(model);
    changes = verifier.getChanges();
    ModelUtils.applyChanges(model, changes);

    changes = new ArrayList<>();
    changes.add(DefaultChange.set(TestApplication.carLane("other"), "1"));
    ModelUtils.applyChanges(model, changes);

    verifier.execute(model);
    changes = verifier.getChanges();
    ModelUtils.applyChanges(model, changes);

    Assert.assertEquals(1, getVerifiersNode().getChildren().size());
    AssertMessage.assertEquals(new DefaultMessage("LaneOneVerifier-one", Severity.WARNING,
        "car {} is at lane {}", new String[] { "one", "2" }),
        getVerifierNode("LaneOneVerifier-one"));
  }

  private Node getVerifierNode(String name) {
    return model.getNode(getVerifierPath(name));
  }

  private Path getVerifierPath(String name) {
    return DefaultVerifier.VERIFIER_MESSAGES.createChildPath(name);
  }

  private Node getVerifiersNode() {
    return model.getNode(DefaultVerifier.VERIFIER_MESSAGES);
  }

  protected void defineModel() {
    model.add(TestApplication.car("one"));
    model.add(TestApplication.carLane("one"));
    model.setValue(TestApplication.carLane("one"), "1");
    model.add(TestApplication.car("other"));
    model.add(TestApplication.carLane("other"));
    model.setValue(TestApplication.carLane("other"), "1");
  }
}
