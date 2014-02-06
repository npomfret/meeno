package snowmonkey.meeno.types.raw;


import snowmonkey.meeno.types.ImmutbleType;

import java.util.Date;

public final class MarketDescription extends ImmutbleType {

    public final Boolean persistenceEnabled;
    public final Boolean bspMarket;
    public final Date marketTime;
    public final Date suspendTime;
    public final Date settleTime;
    public final String bettingType;
    public final Boolean turnInPlayEnabled;
    public final String marketType;
    public final String regulator;
    public final Double marketBaseRate;
    public final Boolean discountAllowed;
    public final String wallet;
    public final String rules;
    public final Boolean rulesHasDate;
    public final String clarifications;

    public MarketDescription(Boolean persistenceEnabled, Boolean bspMarket, Date marketTime, Date suspendTime, Date settleTime, String bettingType, Boolean turnInPlayEnabled, String marketType, String regulator, Double marketBaseRate, Boolean discountAllowed, String wallet, String rules, Boolean rulesHasDate, String clarifications) {
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
