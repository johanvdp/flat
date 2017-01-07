package nl.jvdploeg.flat.socket;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class JsonRoundTripTest {

  @Test
  public void testChangeRoundTrip() throws ParseException {
    final Change original = new DefaultChange(ChangeAction.SET,
        new Path(new String[] { "A", "B", "C" }), "old", "new");
    final String json = JsonEncoder.encode(original).toString();
    final Object object = JsonDecoder.decode(json);
    Assert.assertTrue(object instanceof Change);
    final Change clone = (Change) object;
    Assert.assertEquals(original, clone);
  }

  @Test
  public void testCommandRoundTrip() throws ParseException {
    final String type = "Type";
    final boolean test = true;
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("A", "a");
    parameters.put("B", "b");
    final Command original = new DefaultCommand(type, test, parameters);
    final String json = JsonEncoder.encode(original).toString();
    final Object object = JsonDecoder.decode(json);
    Assert.assertTrue(object instanceof Command);
    final Command clone = (Command) object;
    Assert.assertEquals(original, clone);
  }

  @Test
  public void testResponseRoundTrip() throws ParseException {
    final Message message1 = new DefaultMessage("id1", Severity.INFO, "two () ()",
        new String[] { "A", "B" });
    final Message message2 = new DefaultMessage("id2", Severity.ERROR, "one ()",
        new String[] { "A" });
    final Response original = new DefaultResponse("id0", true, Arrays.asList(message1, message2));
    final String json = JsonEncoder.encode(original).toString();
    final Object object = JsonDecoder.decode(json);
    Assert.assertTrue(object instanceof Response);
    final Response clone = (Response) object;
    Assert.assertEquals(original, clone);
  }
}
