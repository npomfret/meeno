package snowmonkey.meeno.types;

public enum MarketBettingType {
    ODDS("ODDS"),
    LINE("LINE"),
    RANGE("RANGE"),
    ASIAN_HANDICAP_DOUBLE_LINE("ASIAN_HANDICAP_DOUBLE_LINE"),
    ASIAN_HANDICAP_SINGLE_LINE("ASIAN_HANDICAP_SINGLE_LINE"),
    FIXED_ODDS("FIXED_ODDS");

    public final String value;

    private MarketBettingType(String value) {
        this.value = value;
    }
}
