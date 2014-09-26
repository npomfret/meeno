package live;

import org.junit.Test;
import snowmonkey.meeno.types.EventTypeResult;

import java.util.List;

public class ListEventTypesTest extends AbstractLiveTestCase {
    @Test
    public void listEventTypes() throws Exception {
        List<EventTypeResult> events = ukExchange().eventTypes();

        for (EventTypeResult event : events) {
            System.out.println("event = " + event);
        }
    }

}
