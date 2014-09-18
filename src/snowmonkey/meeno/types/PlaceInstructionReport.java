package snowmonkey.meeno.types;

import java.time.ZonedDateTime;

public final class PlaceInstructionReport extends ImmutbleType {
    public final InstructionReportStatus status;
    public final InstructionReportErrorCode errorCode;
    public final PlaceInstruction instruction;
    public final BetId betId;
    public final ZonedDateTime placedDate;
    public final double averagePriceMatched;
    public final double sizeMatched;

    public PlaceInstructionReport(InstructionReportStatus status, InstructionReportErrorCode errorCode, PlaceInstruction instruction, BetId betId, ZonedDateTime placedDate, double averagePriceMatched, double sizeMatched) {
        this.status = status;
        this.errorCode = errorCode;
        this.instruction = instruction;
        this.betId = betId;
        this.placedDate = placedDate;
        this.averagePriceMatched = averagePriceMatched;
        this.sizeMatched = sizeMatched;
    }
}
