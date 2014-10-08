package snowmonkey.meeno.types;


import java.util.UUID;

/**
 * Optional parameter allowing the client to pass a unique string (up to 32 chars) that is used to
 * de-dupe mistaken re-submissions.   CustomerRef can contain: upper/lower chars, digits, chars : - . _ + * : ; ~ only.
 */
public class CustomerRef extends MicroType<String> {

    public CustomerRef(String value) {
        super(value);
    }

    public static CustomerRef uniqueCustomerRef() {
        return safeCustomerRef(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    public static CustomerRef customerRef(String value) {
        CustomerRef customerRef = safeCustomerRef(value);
        String safe = customerRef.asString();
        if (!safe.equals(value)) {
            throw new IllegalStateException("'" + value + "' is no a valid customer ref - see https://api.developer.betfair.com/services/webapps/docs/display/1smk3cen4v3lu3yomq5qye0ni/placeOrders");
        }
        return customerRef;
    }

    public static CustomerRef safeCustomerRef(String value) {
        String cleaned = value.replaceAll("[^a-zA-Z0-9\\._\\+\\*;~:-]", "");
        String chopped = cleaned.substring(0, Math.min(cleaned.length(), 32));
        return new CustomerRef(chopped);
    }

    public String asString() {
        return value;
    }
}
