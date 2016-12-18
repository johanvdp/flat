package nl.jvdploeg.flat;

import java.util.Map.Entry;
import java.util.Set;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.jvdploeg.rx.DefaultPublisher;

public class ModelPublisher implements Publisher<Change> {

    private static final Logger LOG = LoggerFactory.getLogger(ModelPublisher.class);

    private final Model model;
    private final DefaultPublisher<Change> publisher = new DefaultPublisher<>();

    public ModelPublisher(final Model model) {
        this.model = model;
    }

    /**
     * Publish complete.
     */
    public void publishComplete() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} publishComplete", model.getName());
        }
        publisher.publishComplete();
    }

    /**
     * Publish error.
     *
     * @param t
     *            The throwable.
     */
    public void publishError(final Throwable t) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} publishError", model.getName());
        }
        publisher.publishError(t);
    }

    /**
     * Publish next.
     *
     * @param change
     *            The change.
     */
    public void publishNext(final Change change) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} publishNext", model.getName());
        }
        publisher.publishNext(change);
    }

    @Override
    public void subscribe(final Subscriber<? super Change> subscriber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} subscribe", model.getName());
        }
        publisher.subscribe(subscriber);
        // report the current state as changes
        publish(new Path(), model.getRoot());
    }

    private void publish(final Path path, final Node node) {
        final String value = node.getValue();
        if (path.getLength() > 0) {
            publishNext(DefaultChange.add(path));
            if (value != null) {
                publishNext(DefaultChange.set(path, value));
            }
        }
        final Set<Entry<String, Node>> children = node.getChildren().entrySet();
        for (final Entry<String, Node> child : children) {
            final String childName = child.getKey();
            final Node childNode = child.getValue();
            final Path childPath = path.createChildPath(childName);
            publish(childPath, childNode);
        }
    }
}
