package nl.jvdploeg.flat.socket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.application.Command;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.flat.message.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings({ "unchecked" })
public class JsonEncoder {

  /**
   * Encode change.
   * 
   * @param change
   *          The change.
   * @return As json.
   */
  public static JSONObject encode(final Change change) {
    final JSONObject obj = new JSONObject();
    obj.put("type", "Change");
    obj.put("frame", JsonEncoder.toJson(change));
    return obj;
  }

  /**
   * Encode command.
   * 
   * @param command
   *          The command.
   * @return As json.
   */
  public static JSONObject encode(final Command command) {
    final JSONObject obj = new JSONObject();
    obj.put("type", "Command");
    obj.put("frame", JsonEncoder.toJson(command));
    return obj;
  }

  /**
   * Encode object.
   * 
   * @param object
   *          The object.
   * @return As json.
   */
  public static JSONObject encode(final Object object) {
    if (object instanceof Change) {
      return encode((Change) object);
    } else if (object instanceof Command) {
      return encode((Command) object);
    } else if (object instanceof Response) {
      return encode((Response) object);
    }
    throw new IllegalArgumentException("unsupported object type: " + object);
  }

  /**
   * Encode response.
   * 
   * @param response
   *          The response.
   * @return As json.
   */
  public static JSONObject encode(final Response response) {
    final JSONObject obj = new JSONObject();
    obj.put("type", "Response");
    obj.put("frame", JsonEncoder.toJson(response));
    return obj;
  }

  private static JSONObject toJson(final Change change) {
    final JSONObject obj = new JSONObject();
    obj.put("action", change.getAction().name());
    final JSONArray pathArray = new JSONArray();
    pathArray.addAll(Arrays.asList(change.getPath().getPath()));
    obj.put("path", pathArray);
    obj.put("oldValue", change.getOldValue());
    obj.put("newValue", change.getNewValue());
    return obj;
  }

  private static JSONObject toJson(final Command command) {
    final JSONObject obj = new JSONObject();
    obj.put("type", command.getType());
    obj.put("test", Boolean.toString(command.isTest()));
    obj.put("parameters", toJson(command.getParameters()));
    return obj;
  }

  private static JSONObject toJson(final Map<String, String> parameters) {
    final JSONObject obj = new JSONObject();
    for (final Entry<String, String> entry : parameters.entrySet()) {
      obj.put(entry.getKey(), entry.getValue());
    }
    return obj;
  }

  private static JSONObject toJson(final Response response) {
    final JSONObject obj = new JSONObject();
    obj.put("id", response.getIdentifier());
    obj.put("successful", Boolean.toString(response.isSuccessful()));
    obj.put("messages", toJsonArray(response.getMessages()));
    return obj;
  }

  private static JSONObject toJSon(final Message message) {
    final JSONObject obj = new JSONObject();
    obj.put("id", message.getIdentifier());
    obj.put("severity", message.getSeverity().name());
    obj.put("message", message.getMessage());
    obj.put("arguments", JsonEncoder.toJsonArray(message.getArguments()));
    return obj;
  }

  private static JSONArray toJsonArray(final List<Message> messages) {
    final JSONArray array = new JSONArray();
    for (final Message message : messages) {
      array.add(toJSon(message));
    }
    return array;
  }

  private static JSONArray toJsonArray(final String[] strings) {
    final JSONArray array = new JSONArray();
    for (final String string : strings) {
      array.add(string);
    }
    return array;
  }

}
