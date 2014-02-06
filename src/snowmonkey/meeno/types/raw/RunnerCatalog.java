package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.SelectionId;

public final class RunnerCatalog extends ImmutbleType {

    public final SelectionId selectionId;
    public final String runnerName;
    public final Double handicap;

    public RunnerCatalog(SelectionId selectionId, String runnerName, Double handicap) {
        this.selectionId = selectionId;
        this.runnerName = runnerName;
        this.handicap = handicap;
    }
}
