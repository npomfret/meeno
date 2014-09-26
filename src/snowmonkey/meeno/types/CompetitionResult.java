package snowmonkey.meeno.types;

public class CompetitionResult {
    public final Competition competition;
    public final int marketCount;
    public final String competitionRegion;

    public CompetitionResult(Competition competition, int marketCount, String competitionRegion) {
        this.competition = competition;
        this.marketCount = marketCount;
        this.competitionRegion = competitionRegion;
    }
}
