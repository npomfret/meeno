package snowmonkey.meeno;

import java.util.Map;

public interface MarketFilter {
    void addToResponse(Map<String, Object> map);
}
