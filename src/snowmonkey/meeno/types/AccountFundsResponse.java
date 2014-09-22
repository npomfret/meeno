package snowmonkey.meeno.types;

public class AccountFundsResponse extends ImmutbleType {
    public final double availableToBetBalance;
    public final double exposure;
    public final double retainedCommission;
    public final double exposureLimit;
    public final double discountRate;
    public final int pointsBalance;

    public AccountFundsResponse(double availableToBetBalance, double exposure, double retainedCommission, double exposureLimit, double discountRate, int pointsBalance) {
        this.availableToBetBalance = availableToBetBalance;
        this.exposure = exposure;
        this.retainedCommission = retainedCommission;
        this.exposureLimit = exposureLimit;
        this.discountRate = discountRate;
        this.pointsBalance = pointsBalance;
    }
}
