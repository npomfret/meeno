package snowmonkey.meeno.types;

public class AccountFunds {

    public final double availableToBetBalance;
    public final double exposure;
    public final double retainedCommission;
    public final double exposureLimit;

    public AccountFunds(double availableToBetBalance, double exposure, double retainedCommission, double exposureLimit) {
        this.availableToBetBalance = availableToBetBalance;
        this.exposure = exposure;
        this.retainedCommission = retainedCommission;
        this.exposureLimit = exposureLimit;
    }

}