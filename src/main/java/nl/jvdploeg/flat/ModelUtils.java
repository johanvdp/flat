package nl.jvdploeg.flat;

import java.util.List;

/**
 * Model utilities.
 */
public abstract class ModelUtils {

  /**
   * Apply change to model.
   * 
   * @param model
   *          The model.
   * @param change
   *          The change.
   */
  public static void applyChange(final Model model, final Change change) {
    final ChangeAction action = change.getAction();
    final Path path = change.getPath();

    switch (action) {
      case SET:
        final String oldValue = change.getOldValue();
        final String newValue = change.getNewValue();
        // the oldValue of an incoming change is checked if provided
        if (oldValue != null) {
          final String currentValue = model.getValue(path);
          if (!oldValue.equals(currentValue)) {
            final String error = String.format(
                "can not change path %s value from %s to %s because current value is %s", path,
                oldValue, newValue, currentValue);
            throw new IllegalArgumentException(error);
          }
        }
        model.setValue(path, newValue);
        break;
      case ADD:
        model.add(path);
        break;
      case REMOVE:
        model.remove(path);
        break;
      default:
        // stop the madness
        final String error = String.format("change unknown action: %s", action);
        throw new IllegalArgumentException(error);
    }
  }

  /**
   * Apply multiple changes to model.
   * 
   * @param model
   *          The model.
   * @param changes
   *          The changes.
   */
  public static void applyChanges(final Model model, final List<Change> changes) {
    for (final Change change : changes) {
      applyChange(model, change);
    }
  }
}
