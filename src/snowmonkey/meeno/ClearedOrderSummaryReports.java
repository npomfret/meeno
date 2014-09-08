package snowmonkey.meeno;

import snowmonkey.meeno.types.raw.ClearedOrderSummaryReport;

import java.util.Arrays;
import java.util.List;

public class ClearedOrderSummaryReports {
    private final List<ClearedOrderSummaryReport> clearedOrderSummaryReports;

    public ClearedOrderSummaryReports(List<ClearedOrderSummaryReport> clearedOrderSummaryReports) {
        this.clearedOrderSummaryReports = clearedOrderSummaryReports;
    }

    public static ClearedOrderSummaryReports create(ClearedOrderSummaryReport[] clearedOrderSummaryReports) {
        return new ClearedOrderSummaryReports(Arrays.asList(clearedOrderSummaryReports));
    }

}
