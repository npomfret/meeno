package snowmonkey.meeno.types;

import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static snowmonkey.meeno.types.CustomerRef.safeCustomerRef;

public class CustomerRefTest {
    @Test
    public void testHappyCase() throws Exception {
        assertThat(safeCustomerRef("foo").asString(), equalTo("foo"));
        assertThat(safeCustomerRef("foo:bar").asString(), equalTo("foo:bar"));
        assertThat(safeCustomerRef("foo+Bar").asString(), equalTo("foo+Bar"));
        assertThat(safeCustomerRef("foo_Bar").asString(), equalTo("foo_Bar"));
    }

    @Test
    public void testWillRemoveIllegalCharacters() throws Exception {
        assertThat(safeCustomerRef("foo£bar").asString(), equalTo("foobar"));
        assertThat(safeCustomerRef("foo£#bar").asString(), equalTo("foobar"));
        assertThat(safeCustomerRef("foo bar").asString(), equalTo("foobar"));
    }

    @Test
    public void testWillTrimLength() throws Exception {
        assertThat(safeCustomerRef(randomAlphabetic(10)).asString().length(), equalTo(10));
        assertThat(safeCustomerRef(randomAlphabetic(40)).asString().length(), equalTo(32));
    }
}
