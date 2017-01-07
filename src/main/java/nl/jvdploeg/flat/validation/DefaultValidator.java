package nl.jvdploeg.flat.validation;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeCollector;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.MessageNode;
import nl.jvdploeg.nfa.TokenMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DefaultValidator implements Validator {

  private final TokenMatcher matcher;
  private final List<Message> messages = new ArrayList<>();

  public DefaultValidator(final TokenMatcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public List<Message> validate(final Model model, final List<Change> modelChanges) {
    if (isApplicable(modelChanges)) {
      final Model clone = new Model(model.getName() + "-" + getClass().getSimpleName(),
          Enforcement.LENIENT, model);
      ModelUtils.applyChanges(clone, modelChanges);
      verifyImpl(clone);
    }
    return Collections.unmodifiableList(messages);
  }

  protected void addMessage(Message message) {
    messages.add(message);
  }

  protected List<Change> createChanges(final List<Message> newMessages) {
    final ChangeCollector collector = new ChangeCollector();
    for (final Message addMessage : newMessages) {
      final Path addMessagePath = new Path(addMessage.getIdentifier());
      collector.addNode(addMessagePath, MessageNode.createNode(addMessage));
    }
    return collector.getChanges();
  }

  /**
   * Check if at least one change matches this {@link Validator}.
   *
   * @return True if at least one change matches this {@link Validator}.
   */
  protected boolean isApplicable(final List<Change> changes) {
    for (final Change change : changes) {
      final Path path = change.getPath();
      if (matcher.matches(path.getPath())) {
        return true;
      }
    }
    return false;
  }

  protected abstract void verifyImpl(final Model model);
}
