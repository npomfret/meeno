package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class RunnerCatalog extends ImmutbleType {

    public final Long selectionId;
    public final String runnerName;
    public final Double handicap;


    public RunnerCatalog(Long selectionId, String runnerName, Double handicap) {
        this.selectionId = selectionId;
        this.runnerName = runnerName;
        this.handicap = handicap;
    }
}
