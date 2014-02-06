package snowmonkey.meeno;

import org.junit.Test;
import snowmonkey.meeno.types.EventTypes;
import snowmonkey.meeno.types.raw.EventType;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class EventTypesTest {
    @Test
    public void canParse() throws Exception {
        EventTypes eventTypes = EventTypes.parse(GenerateTestData.listEventTypesJson());

        EventType soccer = eventTypes.lookup("Soccer");

        assertThat(soccer.id, equalTo("1"));
        assertThat(soccer.name, equalTo("Soccer"));

        EventType horseRacing = eventTypes.lookup("Horse Racing");

        assertThat(horseRacing.id, equalTo("7"));
        assertThat(horseRacing.name, equalTo("Horse Racing"));
    }
}
