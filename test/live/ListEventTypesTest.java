package live;

import org.junit.Test;
import snowmonkey.meeno.types.EventTypeResult;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class ListEventTypesTest extends AbstractLiveTestCase {
    @Test
    public void listEventTypes() throws Exception {
        EventTypeResult[] events = ukExchange().eventTypes();

        for (EventTypeResult event : events) {
            System.out.println("event = " + event);
        }
    }

}
