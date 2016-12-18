package nl.jvdploeg.flat.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeCollector;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.MessageNode;
import nl.jvdploeg.nfa.TokenMatcher;

public abstract class DefaultVerifier implements Rule {

    private final List<Message> existingMessages = new ArrayList<>();
    private final TokenMatcher matcher;

    public DefaultVerifier(final TokenMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public List<Change> evaluate(final Model model, final List<Change> modelChanges) {
        final Model clone = new Model(model.getName() + "-" + getClass().getSimpleName(), Enforcement.LENIENT, model);
        ModelUtils.applyChanges(clone, modelChanges);
        final List<Message> newMessages = verifyImpl(clone);
        final List<Change> verifierChanges = updateExistingMessages(newMessages);
        return verifierChanges;
    }

    @Override
    public boolean isApplicable(final List<Change> changes) {
        for (final Change change : changes) {
            final Path path = change.getPath();
            if (matcher.matches(path.getPath())) {
                return true;
            }
        }
        return false;
    }

    protected List<Change> updateExistingMessages(final List<Message> newMessages) {
        final ChangeCollector collector = new ChangeCollector();
        // existing messages
        final Iterator<Message> existingMessageIterator = existingMessages.iterator();
        while (existingMessageIterator.hasNext()) {
            final Message existingMessage = existingMessageIterator.next();
            final Path existingMessagePath = new Path(existingMessage.getIdentifier());
            // check if message still present
            final Message newMessage = DefaultMessage.findById(newMessages, existingMessage);
            if (newMessage != null) {
                // message still present, check for changes
                collector.addChanges(MessageNode.createChanges(existingMessagePath, existingMessage, newMessage));
                // message is not new
                newMessages.remove(newMessage);
            } else {
                // message no longer present, remove existing message
                collector.removeNode(existingMessagePath);
            }
        }
        // new messages
        for (final Message addMessage : newMessages) {
            existingMessages.add(addMessage);
            final Path addMessagePath = new Path(addMessage.getIdentifier());
            collector.addNode(addMessagePath, MessageNode.createNode(addMessage));
        }
        return collector.getChanges();
    }

    protected abstract List<Message> verifyImpl(final Model model);
}
