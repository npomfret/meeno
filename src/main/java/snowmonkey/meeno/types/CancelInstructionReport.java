package snowmonkey.meeno.types;

import snowmonkey.meeno.requests.CancelInstruction;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;

public class CancelInstructionReport extends ImmutbleType {

    public final InstructionReportStatus status;
    @Nullable
    public final InstructionReportErrorCode errorCode;
    @Nullable
    public final CancelInstruction instruction;
    @Nullable
    public final Double sizeCancelled;
    @Nullable
    public final ZonedDateTime cancelledDate;

    public CancelInstructionReport(InstructionReportStatus status,
                                   @Nullable InstructionReportErrorCode errorCode,
                                   @Nullable CancelInstruction instruction,
                                   @Nullable Double sizeCancelled,
                                   @Nullable ZonedDateTime cancelledDate) {
        this.status = status;
        this.errorCode = errorCode;
        this.instruction = instruction;
        this.sizeCancelled = sizeCancelled;
        this.cancelledDate = cancelledDate;
    }
}
