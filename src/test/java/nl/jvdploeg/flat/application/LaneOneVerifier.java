package nl.jvdploeg.flat.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.Severity;

public class LaneOneVerifier extends DefaultVerifier {

  public LaneOneVerifier() {
    super();
  }

  @Override
  protected List<Message> verify(final Model model) {
    final List<Message> messages = new ArrayList<>();
    final Node cars = model.getNode(TestApplication.CARS);
    final Map<String, Node> carsByName = cars.getChildren();
    for (final Entry<String, Node> carByName : carsByName.entrySet()) {
      final String name = carByName.getKey();
      final Node car = carByName.getValue();
      final int lane = Integer.parseInt(car.getChild(TestApplication.NODE_LANE).getValue());
      if (lane > 1) {
        final String id = getClass().getSimpleName() + "-" + name;
        final Message message = new DefaultMessage(id, Severity.WARNING, "car {} is at lane {}",
            name, Integer.toString(lane));
        messages.add(message);
      }
    }
    return messages;
  }
}
