package snowmonkey.meeno;

import java.net.URI;

public enum Exchange {
    UK(
            new Uris("https://api.betfair.com/exchange/account/json-rpc/v1", "https://api.betfair.com/exchange/account/rest/v1.0"),
            new Uris("https://api.betfair.com/exchange/betting/json-rpc/v1", "https://api.betfair.com/exchange/betting/rest/v1.0")
    ),

    AUSTRALIA(
            new Uris("https://api-au.betfair.com/exchange/account/json-rpc/v1", "https://api-au.betfair.com/exchange/account/rest/v1.0"),
            new Uris("https://api-au.betfair.com/exchange/betting/json-rpc/v1", "https://api-au.betfair.com/exchange/betting/rest/v1.0")
    );

    public static final String LOGIN_URI = "https://identitysso.betfair.com/api/certlogin";
    public final Exchange.Uris accountUris;
    public final Exchange.Uris bettingUris;

    Exchange(Uris accountUris, Uris bettingUris) {
        this.accountUris = accountUris;
        this.bettingUris = bettingUris;
    }

    static class Uris {
        private final String jsonRcpUrl;
        private final String jsonRestUrl;

        Uris(String jsonRcpUrl, String jsonRestUrl) {
            this.jsonRcpUrl = jsonRcpUrl;
            this.jsonRestUrl = jsonRestUrl;
        }

        public URI jsonRcpUri(String method) {
            return URI.create(jsonRcpUrl + "/" + method + "/");
        }

        public URI jsonRestUri(String method) {
            return URI.create(jsonRestUrl + "/" + method + "/");
        }
    }
}
