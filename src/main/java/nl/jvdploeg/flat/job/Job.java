package nl.jvdploeg.flat.job;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.message.Message;

import java.util.List;

public interface Job {

  /**
   * Execute the {@link Job} given the provided {@link Model}.
   */
  void execute(Model model);

  /**
   * Get resulting {@link Change}s.
   */
  List<Change> getChanges();

  /**
   * Get resulting {@link Job}s.
   */
  List<Job> getJobs();

  /**
   * Get resulting {@link Message}s.
   */
  List<Message> getMessages();
}
