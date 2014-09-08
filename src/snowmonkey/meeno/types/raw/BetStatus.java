package snowmonkey.meeno.types.raw;


import java.util.Arrays;

public enum BetStatus {
    SETTLED,
    VOIDED,
    LAPSED,
    CANCELLED;

    public static Iterable<BetStatus> all() {
        return Arrays.asList(BetStatus.values());
    }

    public static Iterable<BetStatus> notSettled() {
        return Arrays.asList(VOIDED, LAPSED, CANCELLED);
    }
}
