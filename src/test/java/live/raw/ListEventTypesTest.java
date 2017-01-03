package live.raw;

import live.AbstractLiveTestCase;
import org.junit.Test;
import snowmonkey.meeno.requests.ListEventTypes;
import snowmonkey.meeno.types.Locale;

import static live.raw.GenerateTestData.fileWriter;
import static snowmonkey.meeno.types.MarketFilter.Builder.noFilter;

public class ListEventTypesTest extends AbstractLiveTestCase {

    @Test
    public void test() throws Exception {
        httpAccess.listEventTypes(fileWriter(GenerateTestData.LIST_EVENT_TYPES_FILE), new ListEventTypes(noFilter(), Locale.EN_US));
    }

}
