package snowmonkey.meeno.types;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;

public final class PlaceInstructionReport extends ImmutbleType {
    public final InstructionReportStatus status;
    public final InstructionReportErrorCode errorCode;
    public final PlaceInstruction instruction;
    @Nullable
    public final BetId betId;
    @Nullable
    public final ZonedDateTime placedDate;
    @Nullable
    public final Double averagePriceMatched;
    @Nullable
    public final Double sizeMatched;

    public PlaceInstructionReport(InstructionReportStatus status, InstructionReportErrorCode errorCode, PlaceInstruction instruction,
                                  BetId betId, ZonedDateTime placedDate, Double averagePriceMatched, Double sizeMatched) {
        this.status = status;
        this.errorCode = errorCode;
        this.instruction = instruction;
        this.betId = betId;
        this.placedDate = placedDate;
        this.averagePriceMatched = averagePriceMatched;
        this.sizeMatched = sizeMatched;
    }
}
