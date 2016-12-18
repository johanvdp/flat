package nl.jvdploeg.flat.socket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.jvdploeg.flat.CollectingConsumer;
import nl.jvdploeg.flat.CollectingSubscriber;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.Command;
import nl.jvdploeg.flat.application.DefaultCommand;
import nl.jvdploeg.flat.application.DefaultResponse;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.Severity;
import nl.jvdploeg.flat.socket.client.ApplicationClient;
import nl.jvdploeg.flat.socket.server.ApplicationServer;
import nl.jvdploeg.rx.DefaultPublisher;

public class ApplicationClientServerTest {

    private static final int COUNT = 100;
    public static final String WEBSOCKET_URL = "ws://127.0.0.1:8080/websocket";

    private static TestApplication serverApplication;
    private static ApplicationServer server;
    private static ApplicationClient client;
    private static CollectingSubscriber<Response> clientResponseCollector;

    @BeforeClass
    public static void beforeClass() throws SSLException, URISyntaxException {
        // server
        serverApplication = TestApplication.create();
        server = new ApplicationServer(serverApplication, 8080);
        startThread(server, "ApplicationServer");
        // client
        client = new ApplicationClient(new URI(WEBSOCKET_URL));
        clientResponseCollector = new CollectingSubscriber<Response>();
        client.getOutput().subscribe(clientResponseCollector);
        client.open();
    }

    @AfterClass
    public static void afterClass() throws IOException {
        client.close();
    }

    @Test(timeout = 100000)
    public void testChanges() throws Exception {

        // change model
        final Model model = serverApplication.getModel();
        model.add(new Path("A"));
        model.add(new Path(new String[] { "A", "B" }));

        // wait for model changes to replicate to client
        final Model copy = client.getModel();
        while (copy.getRoot().findChild("A") == null || copy.getRoot().findChild("A").findChild("B") == null) {
            Thread.yield();
        }

        // apply model changes
        final Thread thread = new Thread(() -> {
            for (int counter = 1; counter <= COUNT; counter++) {
                model.setValue(new Path(new String[] { "A", "B" }), Integer.toString(counter));
            }
        });
        thread.start();

        // wait for changes on client copy
        final String count = Integer.toString(COUNT);
        while (true) {
            final Node node = copy.getNode(new Path(new String[] { "A", "B" }));
            if (node != null && count.equals(node.getValue())) {
                break;
            }
        }
    }

    @Test(timeout = 100000)
    public void testCommand() throws Exception {

        // send command
        final HashMap<String, String> parameters = new HashMap<>();
        parameters.put("A", "a");
        parameters.put("B", "b");
        final DefaultCommand clientCommand = new DefaultCommand("Command", false, parameters);
        client.getInput().accept(clientCommand);

        final CollectingConsumer<Command> input = (CollectingConsumer<Command>) serverApplication.getInput();
        while (input.getCollection().isEmpty()) {
            // try again
            Thread.yield();
        }
        Assert.assertEquals(1, input.getCollection().size());
        final Command serverCommand = input.getCollection().get(0);
        Assert.assertEquals(clientCommand, serverCommand);
    }

    @Test(timeout = 100000)
    public void testResponse() throws Exception {

        // send response
        final List<Message> messages = new ArrayList<>();
        messages.add(new DefaultMessage("id", Severity.INFO, "test {}", new String[] { "test" }));
        final DefaultResponse serverResponse = new DefaultResponse("Response", messages);
        final DefaultPublisher<Response> serverPublisher = (DefaultPublisher<Response>) serverApplication.getOutput();
        serverPublisher.publishNext(serverResponse);

        while (clientResponseCollector.getCollection().isEmpty()) {
            // try again
            Thread.yield();
        }
        Assert.assertEquals(1, clientResponseCollector.getCollection().size());
        final Response clientResponse = clientResponseCollector.getCollection().get(0);
        Assert.assertEquals(serverResponse, clientResponse);
    }

    private static void startThread(final Runnable server, final String name) {
        final Thread serverThread = new Thread(server, name);
        serverThread.setDaemon(true);
        serverThread.start();
    }

}
