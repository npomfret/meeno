package snowmonkey.meeno.types;

import snowmonkey.meeno.ImmutableType;

public final class AccountFunds extends ImmutableType {

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