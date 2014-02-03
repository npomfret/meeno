package snowmonkey.meeno;

import org.junit.Test;
import snowmonkey.meeno.types.Event;

public class EventsTest {
    @Test
    public void test() throws Exception {
        Events events = Events.parse(GenerateTestData.listEventsJson());

        Event next = events.iterator().next();
        System.out.println("next = " + next);
    }
}
