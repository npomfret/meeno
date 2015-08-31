package snowmonkey.meeno.types.experimental;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;

public class FootballMarket extends ImmutbleType {
    private final Navigation.Market market;

    public FootballMarket(Navigation.Market market) {
        this.market = market;
    }

    public MarketId marketId() {
        return market.id;
    }

    public String marketName() {
        return market.name;
    }

    public String matchName() {
        return market.parent.eventName();
    }

    public String competitionName() {
        return market.parent.parent().groupName();
    }

    public String countryName() {
        return market.parent.parent().parent().groupName();
    }

    public String print() {
        return market.parent.printHierarchy();
    }
}
