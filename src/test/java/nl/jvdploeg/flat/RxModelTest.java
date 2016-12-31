package nl.jvdploeg.flat;

import io.reactivex.Observable;
import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.NfaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RxModelTest {

  private Model model;
  private Observable<Change> observableModel;

  @Before
  public void before() {
    model = new Model(RxModelTest.class.getSimpleName(), Enforcement.STRICT);
    model.add(new Path(new String[] { "A" }));
    model.setValue(new Path(new String[] { "A" }), "a");
    model.add(new Path(new String[] { "A", "B" }));
    model.setValue(new Path(new String[] { "A", "B" }), "b");

    observableModel = Observable.fromPublisher(model.getPublisher());
  }

  @Test
  public void testColdObservable() {
    final CollectingConsumer<Change> consumer = new CollectingConsumer<>();
    observableModel.subscribe(consumer);

    Assert.assertEquals(4, consumer.getCollection().size());
    Assert.assertEquals(DefaultChange.add(new Path(new String[] { "A" })),
        consumer.getCollection().get(0));
    Assert.assertEquals(DefaultChange.set(new Path(new String[] { "A" }), "a"),
        consumer.getCollection().get(1));
    Assert.assertEquals(DefaultChange.add(new Path(new String[] { "A", "B" })),
        consumer.getCollection().get(2));
    Assert.assertEquals(DefaultChange.set(new Path(new String[] { "A", "B" }), "b"),
        consumer.getCollection().get(3));
  }

  @Test
  public void testFilteredCopy() {
    final Model copy = new Model("filteredCopy", Enforcement.LENIENT);
    final ModelChanger copyObserver = new ModelChanger(copy);

    // filter accepts node [A,B] and further
    final NfaFactory nfaFactory = NfaService.getInstance().createNfaFactory();
    final Nfa abAny = nfaFactory.sequence(Arrays.asList(nfaFactory.token("A"),
        nfaFactory.token("B"), nfaFactory.zeroOrMore(nfaFactory.any())));
    final PathFilter predicate = new PathFilter(abAny);
    final Observable<Change> filteredModel = observableModel.filter(predicate);
    filteredModel.subscribe(copyObserver);

    // live update from model to filter to copy
    model.add(new Path(new String[] { "A", "C" }));
    model.setValue(new Path(new String[] { "A", "C" }), "C");

    // the node exists because it is part of the desired hierarchy
    // but a value is never set
    Assert.assertEquals(null, copy.getNode(new Path(new String[] { "A" })).getValue());
    Assert.assertEquals("b", copy.getNode(new Path(new String[] { "A", "B" })).getValue());
    try {
      copy.getNode(new Path(new String[] { "A", "C" }));
      Assert.fail("the path is not accepted by the filter");
    } catch (final IllegalArgumentException expected) {
      // expected
    }
  }

  @Test
  public void testModelCopy() {
    final Model copy = new Model("testModelCopy", Enforcement.LENIENT);
    final ModelChanger copyObserver = new ModelChanger(copy);
    observableModel.subscribe(copyObserver);

    Assert.assertEquals("a", copy.getNode(new Path(new String[] { "A" })).getValue());
    Assert.assertEquals("b", copy.getNode(new Path(new String[] { "A", "B" })).getValue());
  }
}
