package snowmonkey.meeno.types.raw;

import org.jetbrains.annotations.Nullable;
import snowmonkey.meeno.types.BetId;

public class CancelInstruction {
    public final String betId;
    @Nullable
    public final Double sizeReduction;

    public CancelInstruction(BetId betId, Double sizeReduction) {
        this.betId = betId.asString();
        this.sizeReduction = sizeReduction;
    }

    public static CancelInstruction cancel(BetId betId) {
        return new CancelInstruction(betId, null);
    }

    public static CancelInstruction reduce(BetId betId, double sizeReduction) {
        return new CancelInstruction(betId, sizeReduction);
    }
}
