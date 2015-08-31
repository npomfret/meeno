package snowmonkey.meeno.types;

public class CompetitionId extends MicroType<String> {
    public CompetitionId(String value) {
        super(value);
    }

    public static CompetitionId competitionId(String value) {
        return new CompetitionId(value);
    }
}
