package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.Date;

public final class Match extends ImmutbleType {

    public final String betId;
    public final String matchId;
    public final String side;
    public final Double price;
    public final Double Size;
    public final Date matchDate;


    public Match(String betId, String matchId, String side, Double price, Double size, Date matchDate) {
        this.betId = betId;
        this.matchId = matchId;
        this.side = side;
        this.price = price;
        Size = size;
        this.matchDate = matchDate;
    }
}
