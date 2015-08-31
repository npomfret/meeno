package snowmonkey.meeno.types;

public final class Competition extends ImmutbleType {

    public final CompetitionId id;
    public final String name;

    public Competition(CompetitionId id, String name) {
        this.id = id;
        this.name = name;
    }
}
