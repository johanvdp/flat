package nl.jvdploeg.flat.socket;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeAction;
import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.Command;
import nl.jvdploeg.flat.application.DefaultCommand;
import nl.jvdploeg.flat.application.DefaultResponse;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.flat.message.DefaultMessage;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.message.Severity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JsonDecoder {

  /**
   * Decode json.
   * 
   * @param json
   *          The json.
   * @return As object.
   * @throws ParseException
   *           On error.
   */
  public static Object decode(final String json) throws ParseException {
    final JSONParser parser = new JSONParser();
    final JSONObject obj = (JSONObject) parser.parse(json);
    final String type = (String) obj.get("type");
    final JSONObject frame = (JSONObject) obj.get("frame");
    switch (type) {
      case "Change":
        return toChange(frame);
      case "Command":
        return toCommand(frame);
      case "Response":
        return toResponse(frame);
      default:
        throw new IllegalArgumentException("unknown frame type:" + json);
    }
  }

  private static Change toChange(final JSONObject obj) throws ParseException {
    final ChangeAction action = ChangeAction.valueOf((String) obj.get("action"));
    final Path path = new Path(((List<String>) obj.get("path")).toArray(new String[0]));
    final String oldValue = (String) obj.get("oldValue");
    final String newValue = (String) obj.get("newValue");
    final Change change = new DefaultChange(action, path, oldValue, newValue);
    return change;
  }

  private static Command toCommand(final JSONObject obj) throws ParseException {
    final String type = (String) obj.get("type");
    final boolean test = Boolean.valueOf((String) obj.get("test"));
    final Map<String, String> parameters = toParameters((JSONObject) obj.get("parameters"));
    final Command command = new DefaultCommand(type, test, parameters);
    return command;
  }

  private static Message toMessage(final JSONObject obj) {
    final String id = (String) obj.get("id");
    final Severity severity = Severity.valueOf((String) obj.get("severity"));
    final String messageText = (String) obj.get("message");
    final JSONArray argumentArray = (JSONArray) obj.get("arguments");
    final String[] arguments = JsonDecoder.toStrings(argumentArray).toArray(new String[0]);
    final Message message = new DefaultMessage(id, severity, messageText, arguments);
    return message;
  }

  private static List<Message> toMessages(final JSONArray array) {
    final List<Message> messages = new ArrayList<>();
    final Iterator iterator = array.iterator();
    while (iterator.hasNext()) {
      final JSONObject obj = (JSONObject) iterator.next();
      messages.add(toMessage(obj));
    }
    return messages;
  }

  private static Map<String, String> toParameters(final JSONObject obj) {
    final Map<String, String> parameters = new HashMap<>();
    final Iterator iterator = obj.entrySet().iterator();
    while (iterator.hasNext()) {
      final Entry<String, String> entry = (Entry<String, String>) iterator.next();
      parameters.put(entry.getKey(), entry.getValue());

    }
    return parameters;
  }

  private static Response toResponse(final JSONObject obj) throws ParseException {
    final String identifier = (String) obj.get("id");
    final boolean successful = Boolean.parseBoolean((String) obj.get("successful"));
    final List<Message> messages = toMessages((JSONArray) obj.get("messages"));
    final Response response = new DefaultResponse(identifier, successful, messages);
    return response;
  }

  private static List<String> toStrings(final JSONArray array) {
    final List<String> strings = new ArrayList<>();
    final Iterator iterator = array.iterator();
    while (iterator.hasNext()) {
      final String string = (String) iterator.next();
      strings.add(string);
    }
    return strings;
  }
}
