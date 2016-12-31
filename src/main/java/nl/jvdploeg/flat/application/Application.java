package nl.jvdploeg.flat.application;

import io.reactivex.functions.Consumer;
import nl.jvdploeg.flat.Model;
import org.reactivestreams.Publisher;

import java.io.Closeable;

public interface Application extends Closeable {

  Consumer<Command> getInput();

  Model getModel();

  Publisher<Response> getOutput();

  void open() throws Exception;
}
