// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.util;

import java.util.List;

import nl.jvdploeg.exception.Checks;
import nl.jvdploeg.exception.ErrorBuilder;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeType;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.Version;

public abstract class ModelUtils {

  public static void applyChange(final Model<?> model, final Change change) {
    Checks.ARGUMENT.notNull(model, "model");
    Checks.ARGUMENT.notNull(change, "change");

    final ChangeType action = change.getAction();
    switch (action) {
      case SET:
        applySet(model, change);
        break;
      case ADD:
        applyAdd(model, change);
        break;
      case REMOVE:
        applyRemove(model, change);
        break;
      default:
        Checks.ARGUMENT.unexpected(action, "change");
    }
  }

  public static void applyChanges(final Model<?> model, final List<Change> changes) {
    Checks.ARGUMENT.notNull(changes, "changes");
    for (final Change change : changes) {
      applyChange(model, change);
    }
  }

  private static void applyAdd(final Model<?> model, final Change change) {
    final Path path = change.getPath();
    model.add(path);
  }

  private static void applyRemove(final Model<?> model, final Change change) {
    final Path path = change.getPath();
    model.remove(path);
  }

  private static void applySet(final Model<?> model, final Change change) {
    final Path changePath = change.getPath();
    final Version changeVersion = change.getVersion();
    final String changeValue = change.getValue();
    final Version modelVersion = model.getVersion(changePath);
    // the version of an incoming change is checked if provided
    if (changeVersion != null && !changeVersion.equals(modelVersion)) {
      throw new ErrorBuilder() //
          .method("applySet") //
          .message("can not change value because versions do not match") //
          .identity("model", model) //
          .field("changePath", changePath.toString()) //
          .field("changeValue", changeValue) //
          .field("changeVersion", changeVersion.toString()) //
          .field("modelVersion", modelVersion.toString()) //
          .build();
    }
    model.setValue(changePath, changeValue);
  }

  private ModelUtils() {
  }
}
