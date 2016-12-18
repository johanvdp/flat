package nl.jvdploeg.flat.application;

import java.io.IOException;
import java.util.Arrays;

import org.reactivestreams.Publisher;

import io.reactivex.functions.Consumer;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.rule.LaneOneVerifier;
import nl.jvdploeg.flat.rule.LaneValidator;
import nl.jvdploeg.flat.rule.Validators;
import nl.jvdploeg.flat.rule.Verifiers;

public class TestApplication extends AbstractApplication {

    public TestApplication() {
    }

    @Override
    public Model createModel() {
        final Model model = new Model(TestApplication.class.getSimpleName(), Enforcement.STRICT);
        model.add(Road.ROAD);
        model.add(Road.ROAD_LANES);
        model.setValue(Road.ROAD_LANES, "3");
        model.add(Car.CARS);
        return model;
    }

    @Override
    public Validators createValidators() {
        final Validators validators = new Validators();
        validators.setValidators(Arrays.asList(new LaneValidator()));
        return validators;
    }

    @Override
    public Verifiers createVerifiers() {
        final Verifiers verifiers = new Verifiers();
        verifiers.setVerifiers(Arrays.asList(new LaneOneVerifier()));
        return verifiers;
    }

    @Override
    public void open() throws Exception {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public Consumer<Command> getInput() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Publisher<Response> getOutput() {
        throw new UnsupportedOperationException();
    }
}
