package nl.jvdploeg.flat.socket;

import java.io.IOException;

import org.reactivestreams.Publisher;

import io.reactivex.functions.Consumer;
import nl.jvdploeg.flat.CollectingConsumer;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.application.Application;
import nl.jvdploeg.flat.application.Command;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.rx.DefaultPublisher;

public class TestApplication implements Application {

    private Model model;
    private Publisher<Response> output;
    private Consumer<Command> input;
    private boolean opened;
    private boolean closed;

    public TestApplication() {
    }

    public void setModel(final Model model) {
        this.model = model;
    }

    public void setOutput(final Publisher<Response> output) {
        this.output = output;
    }

    public void setInput(final Consumer<Command> input) {
        this.input = input;
    }

    @Override
    public Model getModel() {
        return model;
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
    public void close() throws IOException {
        if (closed) {
            throw new IllegalStateException("should not call close more than once");
        }
        closed = true;
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

    public static TestApplication create() {
        final TestApplication application = new TestApplication();
        application.setModel(new Model("original", Enforcement.STRICT));
        application.setInput(new CollectingConsumer<>());
        application.setOutput(new DefaultPublisher<>());
        return application;
    }
}
