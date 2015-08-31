package snowmonkey.meeno;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;

public class ExchangeTest {
    @Test
    public void methodNameFrom() throws Exception {
        Exchange.MethodName methodName = Exchange.methodNameFrom(Exchange.UK.bettingUris.jsonRestUri(Exchange.MethodName.TRANSFER_FUNDS));
        assertThat(methodName, equalTo(Exchange.MethodName.TRANSFER_FUNDS));
    }
}