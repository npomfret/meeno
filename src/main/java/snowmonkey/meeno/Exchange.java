package snowmonkey.meeno;

import snowmonkey.meeno.types.ExchangeId;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public enum Exchange {
    UK(
            new Uris("https://api.betfair.com/exchange/account/rest/v1.0"),
            new Uris("https://api.betfair.com/exchange/betting/rest/v1.0")
    );

    public static final URI NAVIGATION = URI.create("https://api.betfair.com/exchange/betting/rest/v1/en/navigation/menu.json");

    public static final URI LOGIN_URI = URI.create("https://identitysso.betfair.com/api/certlogin");
    public static final URI LOGOUT_URI = URI.create("https://identitysso.betfair.com/api/logout");

    public static final ExchangeId DEFAULT_EXCHANGE_ID = new ExchangeId("1");

    public static Exchange lookupByExchangeId(ExchangeId exchangeId) {
        if (exchangeId.equals(DEFAULT_EXCHANGE_ID))
            return UK;
        else
            throw new IllegalStateException("Don't know about exchange id " + exchangeId);
    }

    public final Exchange.Uris accountUris;
    public final Exchange.Uris bettingUris;

    Exchange(Uris accountUris, Uris bettingUris) {
        this.accountUris = accountUris;
        this.bettingUris = bettingUris;
    }

    static class Uris {
        private final String jsonRestUrl;

        Uris(String jsonRestUrl) {
            this.jsonRestUrl = jsonRestUrl;
        }

        public URI jsonRestUri(MethodName method) {
            return URI.create(jsonRestUrl + "/" + method.pathPart + "/");
        }
    }

    public static MethodName methodNameFrom(URI uri) {
        if (uri.equals(NAVIGATION))
            return MethodName.NAVIGATION;
        String[] split = uri.toASCIIString().split("/");
        return MethodName.lookup.get(split[split.length - 1]);
    }

    public static enum MethodName {
        TRANSFER_FUNDS("transferFunds"),
        CANCEL_ORDERS("cancelOrders"),
        GET_ACCOUNT_DETAILS("getAccountDetails"),
        GET_ACCOUNT_FUNDS("getAccountFunds"),
        LIST_CLEARED_ORDERS("listClearedOrders"),
        LIST_COMPETITIONS("listCompetitions"),
        LIST_COUNTRIES("listCountries"),
        LIST_CURRENT_ORDERS("listCurrentOrders"),
        LIST_EVENTS("listEvents"),
        LIST_EVENT_TYPES("listEventTypes"),
        LIST_MARKET_BOOK("listMarketBook"),
        LIST_MARKET_CATALOGUE("listMarketCatalogue"),
        LIST_MARKET_TYPES("listMarketTypes"),
        LIST_TIME_RANGES("listTimeRanges"),
        PLACE_ORDERS("placeOrders"),
        NAVIGATION("navigation/menu.json");

        private static final Map<String, MethodName> lookup = new HashMap<String, MethodName>() {{
            for (MethodName methodName : MethodName.values()) {
                put(methodName.pathPart, methodName);
            }
        }};

        public final String pathPart;

        MethodName(String pathPart) {
            this.pathPart = pathPart;
        }

    }
}
