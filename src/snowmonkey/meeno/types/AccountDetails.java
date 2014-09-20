package snowmonkey.meeno.types;

public final class AccountDetails extends ImmutbleType {

    public final String currencyCode;
    public final String firstName;
    public final String lastName;
    public final Locale localeCode;
    public final String region;
    public final String timezone;
    public final double discountRate;
    public final int pointsBalance;

    public AccountDetails(String currencyCode, String firstName, String lastName, Locale localeCode, String region, String timezone, double discountRate, int pointsBalance) {
        this.currencyCode = currencyCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.localeCode = localeCode;
        this.region = region;
        this.timezone = timezone;
        this.discountRate = discountRate;
        this.pointsBalance = pointsBalance;
    }
}