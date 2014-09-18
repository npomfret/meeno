package snowmonkey.meeno.types;


import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

public final class MarketDescription extends ImmutbleType {

    public final Boolean persistenceEnabled;
    public final Boolean bspMarket;
    public final DateTime marketTime;
    public final DateTime suspendTime;
    @Nullable
    public final DateTime settleTime;
    public final MarketBettingType bettingType;
    public final Boolean turnInPlayEnabled;
    public final String marketType;
    @Nullable
    public final String regulator;
    public final Double marketBaseRate;
    public final Boolean discountAllowed;
    @Nullable
    public final String wallet;
    @Nullable
    public final String rules;
    @Nullable
    public final Boolean rulesHasDate;
    @Nullable
    public final String clarifications;

    public MarketDescription(Boolean persistenceEnabled, Boolean bspMarket, DateTime marketTime, DateTime suspendTime, DateTime settleTime, MarketBettingType bettingType, Boolean turnInPlayEnabled, String marketType, String regulator, Double marketBaseRate, Boolean discountAllowed, String wallet, String rules, Boolean rulesHasDate, String clarifications) {
        this.persistenceEnabled = persistenceEnabled;
        this.bspMarket = bspMarket;
        this.marketTime = marketTime;
        this.suspendTime = suspendTime;
        this.settleTime = settleTime;
        this.bettingType = bettingType;
        this.turnInPlayEnabled = turnInPlayEnabled;
        this.marketType = marketType;
        this.regulator = regulator;
        this.marketBaseRate = marketBaseRate;
        this.discountAllowed = discountAllowed;
        this.wallet = wallet;
        this.rules = rules;
        this.rulesHasDate = rulesHasDate;
        this.clarifications = clarifications;
    }
}
