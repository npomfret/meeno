package snowmonkey.meeno.types.raw;


import snowmonkey.meeno.types.ImmutbleType;

import java.util.List;


public final class PlaceExecutionReport extends ImmutbleType {

    public final String customerRef;
    public final ExecutionReportStatus status;
    public final ExecutionReportErrorCode errorCode;
    public final String marketId;
    public final List<PlaceInstructionReport> instructionReports;

    public PlaceExecutionReport(String customerRef, ExecutionReportStatus status, ExecutionReportErrorCode errorCode, String marketId, List<PlaceInstructionReport> instructionReports) {
        this.customerRef = customerRef;
        this.status = status;
        this.errorCode = errorCode;
        this.marketId = marketId;
        this.instructionReports = instructionReports;
    }
}