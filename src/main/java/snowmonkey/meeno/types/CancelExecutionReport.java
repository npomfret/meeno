package snowmonkey.meeno.types;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.List;

public class CancelExecutionReport extends ImmutbleType {
    @Nullable
    public final CustomerRef customerRef;
    public final ExecutionReportStatus status;
    @Nullable
    public final ExecutionReportErrorCode errorCode;
    @Nullable
    public final MarketId marketId;
    @Nullable
    public final ImmutableList<CancelInstructionReport> instructionReports;

    public CancelExecutionReport(@Nullable CustomerRef customerRef,
                                 ExecutionReportStatus status,
                                 @Nullable ExecutionReportErrorCode errorCode,
                                 @Nullable MarketId marketId,
                                 List<CancelInstructionReport> instructionReports) {
        this.customerRef = customerRef;
        this.status = status;
        this.errorCode = errorCode;
        this.marketId = marketId;
        this.instructionReports = ImmutableList.copyOf(instructionReports);
    }
}
