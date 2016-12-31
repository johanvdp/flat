package nl.jvdploeg.flat.application;

import io.reactivex.functions.Consumer;
import nl.jvdploeg.flat.CollectingConsumer;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.validation.LaneValidator;
import nl.jvdploeg.flat.validation.Validators;
import nl.jvdploeg.rx.DefaultPublisher;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.util.Arrays;

public class TestApplication extends AbstractApplication {

  public static final String NODE_LANES = "lanes";
  public static final String NODE_ROAD = "road";
  public static final String NODE_CARS = "cars";
  public static final String NODE_LANE = "lane";
  public static final Path CARS = new Path(new String[] { NODE_CARS });
  public static final Path ROAD = new Path(new String[] { NODE_ROAD });
  public static final Path ROAD_LANES = new Path(new String[] { NODE_ROAD, NODE_LANES });

  public static Path car(final String name) {
    return new Path(new String[] { NODE_CARS, name });
  }

  public static Path carLane(final String name) {
    return new Path(new String[] { NODE_CARS, name, NODE_LANE });
  }

  public static TestApplication create() {
    final TestApplication application = new TestApplication();
    application.setModel(new Model("original", Enforcement.STRICT));
    application.setInput(new CollectingConsumer<>());
    application.setOutput(new DefaultPublisher<>());
    return application;
  }

  private Publisher<Response> output;
  private Consumer<Command> input;
  private boolean opened;
  private boolean closed;

  public TestApplication() {
  }

  @Override
  public void close() throws IOException {
    if (closed) {
      throw new IllegalStateException("should not call close more than once");
    }
    closed = true;
  }

  @Override
  public Model createModel() {
    final Model model = new Model(TestApplication.class.getSimpleName(), Enforcement.LENIENT);
    model.add(ROAD);
    model.add(ROAD_LANES);
    model.setValue(ROAD_LANES, "3");
    model.add(CARS);
    model.add(DefaultVerifier.VERIFIER_MESSAGES);
    return model;
  }

  @Override
  public Validators createValidators() {
    final Validators validators = new Validators();
    validators.setValidators(Arrays.asList(new LaneValidator()));
    return validators;
  }

  @Override
  public Consumer<Command> getInput() {
    return input;
  }

  @Override
  public Publisher<Response> getOutput() {
    return output;
  }

  @Override
  public void open() throws Exception {
    if (opened) {
      throw new IllegalStateException("should not call open more than once");
    }
    if (closed) {
      throw new IllegalStateException("should not call open when closed");
    }
    opened = true;
  }

  public void setInput(final Consumer<Command> input) {
    this.input = input;
  }

  public void setModel(final Model model) {
    this.model = model;
  }

  public void setOutput(final Publisher<Response> output) {
    this.output = output;
  }
}
