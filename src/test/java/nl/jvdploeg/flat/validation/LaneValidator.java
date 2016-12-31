package nl.jvdploeg.flat.validation;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.application.TestApplication;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.Severity;
import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.NfaService;
import nl.jvdploeg.nfa.TokenMatcher;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LaneValidator extends DefaultValidator {

  private static final TokenMatcher ROAD_LANES_OR_CAR_LANE;

  static {
    final NfaFactory factory = NfaService.getInstance().createNfaFactory();
    final Nfa roadLanes = factory.sequence(Arrays.asList(factory.token(TestApplication.NODE_ROAD),
        factory.token(TestApplication.NODE_LANES)));
    final Nfa carLane = factory.sequence(Arrays.asList(factory.token(TestApplication.NODE_CARS),
        factory.any(), factory.token(TestApplication.NODE_LANE)));
    final Nfa roadLanesOrCarLane = factory.or(roadLanes, carLane);
    ROAD_LANES_OR_CAR_LANE = factory.optimize(roadLanesOrCarLane);
  }

  public LaneValidator() {
    super(ROAD_LANES_OR_CAR_LANE);
  }

  @Override
  protected void verifyImpl(final Model model) {
    final int roadLanes = Integer.parseInt(model.getNode(TestApplication.ROAD_LANES).getValue());
    final Node carsNode = model.getNode(TestApplication.CARS);
    final Map<String, Node> carNodesByName = carsNode.getChildren();
    for (final Entry<String, Node> carNodeByName : carNodesByName.entrySet()) {
      final String carName = carNodeByName.getKey();
      final Node carNode = carNodeByName.getValue();
      final int carLane = Integer.parseInt(carNode.getChild(TestApplication.NODE_LANE).getValue());
      if (carLane < 1 || carLane > roadLanes) {
        final String id = getClass().getSimpleName() + "-" + carName;
        final Message message = new DefaultMessage(id, Severity.ERROR,
            "road has {} lanes, car {} can not be at lane {}", Integer.toString(roadLanes), carName,
            Integer.toString(carLane));
        addMessage(message);
      }
    }
  }
}
