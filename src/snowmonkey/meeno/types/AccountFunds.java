package snowmonkey.meeno.types;

public class AccountFunds {

    public final double availableToBetBalance;
    public final double exposure;
    public final double retainedCommission;
    public final String exposureLimit;

    public AccountFunds(double availableToBetBalance, double exposure, double retainedCommission, String exposureLimit) {
        this.availableToBetBalance = availableToBetBalance;
        this.exposure = exposure;
        this.retainedCommission = retainedCommission;
        this.exposureLimit = exposureLimit;
    }

}