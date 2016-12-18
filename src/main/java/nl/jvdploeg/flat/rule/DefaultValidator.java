package nl.jvdploeg.flat.rule;

import java.util.List;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeCollector;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.MessageNode;
import nl.jvdploeg.nfa.TokenMatcher;

public abstract class DefaultValidator implements Rule {

    private final TokenMatcher matcher;

    public DefaultValidator(final TokenMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public List<Change> evaluate(final Model model, final List<Change> modelChanges) {
        final Model clone = new Model(model.getName() + "-" + getClass().getSimpleName(), Enforcement.LENIENT, model);
        ModelUtils.applyChanges(clone, modelChanges);
        final List<Message> messages = verifyImpl(clone);
        final List<Change> valdidatorChanges = createChanges(messages);
        return valdidatorChanges;
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

    protected List<Change> createChanges(final List<Message> newMessages) {
        final ChangeCollector collector = new ChangeCollector();
        for (final Message addMessage : newMessages) {
            final Path addMessagePath = new Path(addMessage.getIdentifier());
            collector.addNode(addMessagePath, MessageNode.createNode(addMessage));
        }
        return collector.getChanges();
    }

    protected abstract List<Message> verifyImpl(final Model model);
}
