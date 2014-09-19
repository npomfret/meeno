package snowmonkey.meeno;

import live.GenerateTestData;
import org.junit.Test;
import snowmonkey.meeno.types.Event;
import snowmonkey.meeno.types.Events;

import static org.apache.commons.io.FileUtils.*;

public class EventsTest {
    @Test
    public void test() throws Exception {
        Events events = Events.parse(readFileToString(GenerateTestData.LIST_EVENTS_FILE.toFile()));

        Event next = events.iterator().next();
        System.out.println("next = " + next);
    }
}
