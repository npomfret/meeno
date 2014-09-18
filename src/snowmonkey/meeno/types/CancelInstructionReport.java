package snowmonkey.meeno.types;

import org.jetbrains.annotations.Nullable;
import snowmonkey.meeno.requests.CancelInstruction;

import java.time.ZonedDateTime;

public class CancelInstructionReport extends ImmutbleType {

    public final InstructionReportStatus status;
    @Nullable
    public final InstructionReportErrorCode errorCode;
    @Nullable
    public final CancelInstruction instruction;
    public final double sizeCancelled;
    @Nullable
    public final ZonedDateTime cancelledDate;

    public CancelInstructionReport(InstructionReportStatus status, InstructionReportErrorCode errorCode, CancelInstruction instruction, double sizeCancelled, ZonedDateTime cancelledDate) {
        this.status = status;
        this.errorCode = errorCode;
        this.instruction = instruction;
        this.sizeCancelled = sizeCancelled;
        this.cancelledDate = cancelledDate;
    }
}
