package nl.jvdploeg.flat.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeCollector;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.job.Job;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.MessageNode;

/**
 * Verifies the {@link Model}. A maintains a (persistent) list of {@link Message}s. Changes to this
 * list are reflected as changes to an independent {@link Model}.
 */
public abstract class DefaultVerifier implements Job {

  public static final Path VERIFIER_MESSAGES = new Path("VerifierMessages");

  private final List<Message> existingMessages = new ArrayList<>();
  private List<Change> changes;

  public DefaultVerifier() {
  }

  @Override
  public void execute(final Model model) {
    final List<Message> newMessages = verify(model);
    changes = detectChanges(newMessages);
  }

  @Override
  public List<Change> getChanges() {
    return Collections.unmodifiableList(changes);
  }

  @Override
  public List<Job> getJobs() {
    return Collections.emptyList();
  }

  @Override
  public List<Message> getMessages() {
    return Collections.unmodifiableList(existingMessages);
  }

  protected List<Change> detectChanges(final List<Message> newMessages) {
    final ChangeCollector collector = new ChangeCollector();
    // existing messages
    final Iterator<Message> existingMessageIterator = existingMessages.iterator();
    while (existingMessageIterator.hasNext()) {
      final Message existingMessage = existingMessageIterator.next();
      final Path existingMessagePath = VERIFIER_MESSAGES
          .createChildPath(existingMessage.getIdentifier());
      // check if message still present
      final Message newMessage = DefaultMessage.findById(newMessages, existingMessage);
      if (newMessage != null) {
        // message still present, check for changes
        collector.addChanges(
            MessageNode.createChanges(existingMessagePath, existingMessage, newMessage));
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
      final Path addMessagePath = VERIFIER_MESSAGES.createChildPath(addMessage.getIdentifier());
      collector.addNode(addMessagePath, MessageNode.createNode(addMessage));
    }
    return collector.getChanges();
  }

  /**
   * Verify {@link Model} resulting in {@link Message}s.
   * 
   * @param model
   *          The {@link Model}.
   * @return The resulting {@link Message}s.
   */
  protected abstract List<Message> verify(final Model model);
}
