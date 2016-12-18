package nl.jvdploeg.flat.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.application.Car;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.Severity;
import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.NfaService;
import nl.jvdploeg.nfa.TokenMatcher;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LaneOneVerifier extends DefaultVerifier {

    private static final TokenMatcher CAR_LANE;

    static {
        final NfaFactory factory = NfaService.getInstance().createNfaFactory();
        final Nfa carLane = factory.sequence(Arrays.asList(factory.token(Car.NODE_CARS), factory.any(), factory.token(Car.NODE_LANE)));
        CAR_LANE = factory.optimize(carLane);
    }

    public LaneOneVerifier() {
        super(CAR_LANE);
    }

    @Override
    protected List<Message> verifyImpl(final Model model) {
        final List<Message> messages = new ArrayList<>();
        final Node cars = model.getNode(Car.CARS);
        final Map<String, Node> carsByName = cars.getChildren();
        for (final Entry<String, Node> carByName : carsByName.entrySet()) {
            final String name = carByName.getKey();
            final Node car = carByName.getValue();
            final int lane = Integer.parseInt(car.getChild(Car.NODE_LANE).getValue());
            if (lane > 1) {
                final String id = getClass().getSimpleName() + "-" + name;
                final Message message = new DefaultMessage(id, Severity.WARNING, "car {} is at lane {}", name, Integer.toString(lane));
                messages.add(message);
            }
        }
        return messages;
    }
}
