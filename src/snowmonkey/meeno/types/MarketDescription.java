package snowmonkey.meeno.types;


import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

public final class MarketDescription extends ImmutbleType {

    public final Boolean persistenceEnabled;
    public final Boolean bspMarket;
    public final ZonedDateTime marketTime;
    public final ZonedDateTime suspendTime;
    @Nullable
    public final ZonedDateTime settleTime;
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

    public MarketDescription(Boolean persistenceEnabled, Boolean bspMarket, ZonedDateTime marketTime, ZonedDateTime suspendTime, ZonedDateTime settleTime, MarketBettingType bettingType, Boolean turnInPlayEnabled, String marketType, String regulator, Double marketBaseRate, Boolean discountAllowed, String wallet, String rules, Boolean rulesHasDate, String clarifications) {
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
