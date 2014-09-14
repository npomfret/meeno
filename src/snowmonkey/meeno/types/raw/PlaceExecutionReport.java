package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;

import java.util.List;

public final class PlaceExecutionReport extends ImmutbleType {

    public final String customerRef;
    public final ExecutionReportStatus status;
    public final ExecutionReportErrorCode errorCode;
    public final MarketId marketId;
    public final List<PlaceInstructionReport> instructionReports;

    public PlaceExecutionReport(String customerRef, ExecutionReportStatus status, ExecutionReportErrorCode errorCode, MarketId marketId, List<PlaceInstructionReport> instructionReports) {
        this.customerRef = customerRef;
        this.status = status;
        this.errorCode = errorCode;
        this.marketId = marketId;
        this.instructionReports = instructionReports;
    }
}