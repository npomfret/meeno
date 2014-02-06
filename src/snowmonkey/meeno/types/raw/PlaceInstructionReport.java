package snowmonkey.meeno.types.raw;


import snowmonkey.meeno.types.ImmutbleType;

import java.util.Date;

public final class PlaceInstructionReport extends ImmutbleType {
    public final InstructionReportStatus status;
    public final InstructionReportErrorCode errorCode;
    public final PlaceInstruction instructionl;
    public final String betId;
    public final Date placedDate;
    public final double averagePriceMatched;
    public final double sizeMatched;


    public PlaceInstructionReport(InstructionReportStatus status, InstructionReportErrorCode errorCode, PlaceInstruction instructionl, String betId, Date placedDate, double averagePriceMatched, double sizeMatched) {
        this.status = status;
        this.errorCode = errorCode;
        this.instructionl = instructionl;
        this.betId = betId;
        this.placedDate = placedDate;
        this.averagePriceMatched = averagePriceMatched;
        this.sizeMatched = sizeMatched;
    }
}
