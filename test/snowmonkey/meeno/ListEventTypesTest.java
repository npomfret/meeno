package snowmonkey.meeno;

import live.GenerateTestData;
import org.junit.Test;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.EventTypes;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ListEventTypesTest {
    @Test
    public void canParse() throws Exception {
        EventTypes eventTypes = EventTypes.parse(GenerateTestData.ListEventTypes.listEventTypesJson());

        EventType soccer = eventTypes.lookup("Soccer");

        assertThat(soccer.id, equalTo("1"));
        assertThat(soccer.name, equalTo("Soccer"));

        EventType horseRacing = eventTypes.lookup("Horse Racing");

        assertThat(horseRacing.id, equalTo("7"));
        assertThat(horseRacing.name, equalTo("Horse Racing"));
    }
}
