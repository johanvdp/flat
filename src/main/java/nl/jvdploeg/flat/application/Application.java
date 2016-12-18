package nl.jvdploeg.flat.application;

import java.io.Closeable;

import org.reactivestreams.Publisher;

import io.reactivex.functions.Consumer;
import nl.jvdploeg.flat.Model;

public interface Application extends Closeable {

    void open() throws Exception;

    Model getModel();

    Consumer<Command> getInput();

    Publisher<Response> getOutput();
}
