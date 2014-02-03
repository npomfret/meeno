package snowmonkey.meeno;

import org.junit.Test;
import snowmonkey.meeno.types.EventType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class EventTypesTest {
    @Test
    public void canParse() throws Exception {
        EventTypes eventTypes = EventTypes.parse(GenerateTestData.listEventTypesJson());

        EventType soccer = eventTypes.lookup("Soccer");

        assertThat(soccer.id, equalTo("1"));
        assertThat(soccer.name, equalTo("Soccer"));
        assertThat(soccer.marketCount, greaterThan(10));

        EventType horseRacing = eventTypes.lookup("Horse Racing");

        assertThat(horseRacing.id, equalTo("7"));
        assertThat(horseRacing.name, equalTo("Horse Racing"));
        assertThat(horseRacing.marketCount, greaterThan(10));
    }
}
