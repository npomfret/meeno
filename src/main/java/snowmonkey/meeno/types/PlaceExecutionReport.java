package snowmonkey.meeno.types;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class PlaceExecutionReport extends ImmutbleType {

    public final CustomerRef customerRef;
    public final ExecutionReportStatus status;
    public final ExecutionReportErrorCode errorCode;
    public final MarketId marketId;
    public final ImmutableList<PlaceInstructionReport> instructionReports;

    public PlaceExecutionReport(CustomerRef customerRef, ExecutionReportStatus status, ExecutionReportErrorCode errorCode, MarketId marketId, List<PlaceInstructionReport> instructionReports) {
        this.customerRef = customerRef;
        this.status = status;
        this.errorCode = errorCode;
        this.marketId = marketId;
        this.instructionReports = instructionReports == null ? ImmutableList.of() : ImmutableList.copyOf(instructionReports);
    }
}