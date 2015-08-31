package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.ImmutbleType;

import javax.annotation.Nullable;

public class CancelInstruction extends ImmutbleType {
    public final BetId betId;
    @Nullable
    public final Double sizeReduction;

    public CancelInstruction(BetId betId, @Nullable Double sizeReduction) {
        this.betId = betId;
        this.sizeReduction = sizeReduction;
    }

    public static CancelInstruction cancel(BetId betId) {
        return new CancelInstruction(betId, null);
    }

    public static CancelInstruction reduce(BetId betId, double sizeReduction) {
        return new CancelInstruction(betId, sizeReduction);
    }
}
