package snowmonkey.meeno.types;

import java.time.ZonedDateTime;

public final class Match extends ImmutbleType {

    public final BetId betId;
    public final MatchId matchId;
    public final Side side;
    public final Double price;
    public final Double size;
    public final ZonedDateTime matchDate;

    public Match(BetId betId, MatchId matchId, Side side, Double price, Double size, ZonedDateTime matchDate) {
        this.betId = betId;
        this.matchId = matchId;
        this.side = side;
        this.price = price;
        this.size = size;
        this.matchDate = matchDate;
    }
}
