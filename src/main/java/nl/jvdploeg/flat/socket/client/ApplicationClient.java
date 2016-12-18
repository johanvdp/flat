package nl.jvdploeg.flat.socket.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.SSLException;

import org.reactivestreams.Publisher;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelChanger;
import nl.jvdploeg.flat.application.Application;
import nl.jvdploeg.flat.application.Command;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.flat.socket.RxJsonDecoder;
import nl.jvdploeg.flat.socket.RxJsonEncoder;
import nl.jvdploeg.rx.DefaultPublisher;
import nl.jvdploeg.rx.DefaultPublisherConsumer;
import nl.jvdploeg.rx.TypeFilter;

public class ApplicationClient implements Application {

    private final Model model;
    private final WebSocketClient webClient;
    private final DefaultPublisher<Response> responsePublisher;
    private final DefaultPublisher<Command> commandPublisher;
    private final DefaultPublisherConsumer<Command> commandConsumer;

    public ApplicationClient(final URI uri) throws SSLException, URISyntaxException {
        model = new Model(WebSocketClient.class.getSimpleName(), Enforcement.LENIENT);
        commandPublisher = new DefaultPublisher<>();
        commandConsumer = new DefaultPublisherConsumer<>(commandPublisher);
        responsePublisher = new DefaultPublisher<>();
        webClient = new WebSocketClient(uri);
    }

    @Override
    public void close() throws IOException {
        webClient.close();
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void open() throws SSLException, URISyntaxException {
        // decoder
        final Observable<String> webObservable = Observable.fromPublisher(webClient);
        final RxJsonDecoder decoder = new RxJsonDecoder();
        webObservable.subscribe(decoder);
        final Observable<Object> decoderObservable = Observable.fromPublisher(decoder);
        // change
        final TypeFilter<Object, Change> changeFilter = new TypeFilter<Object, Change>(Change.class);
        final Observable<Change> changeObservable = Observable.fromPublisher(changeFilter);
        final ModelChanger modelChanger = new ModelChanger(model);
        changeObservable.subscribe(modelChanger);
        decoderObservable.subscribe(changeFilter);
        // response
        final TypeFilter<Object, Response> responseFilter = new TypeFilter<Object, Response>(Response.class);
        final Observable<Response> responseObservable = Observable.fromPublisher(responseFilter);
        responseObservable.subscribe(new DefaultPublisherConsumer<>(responsePublisher));

        final RxJsonEncoder encoder = new RxJsonEncoder();

        final Observable<String> encoderObservable = Observable.fromPublisher(encoder);
        final Observable<Command> commandObservable = Observable.fromPublisher(commandPublisher);

        decoderObservable.subscribe(responseFilter);
        commandObservable.subscribe(encoder);
        encoderObservable.subscribe(webClient);
        webClient.open();

    }

    @Override
    public Consumer<Command> getInput() {
        return commandConsumer;
    }

    @Override
    public Publisher<Response> getOutput() {
        return responsePublisher;
    }
}
