package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class Competition extends ImmutbleType {

    public final String id;
    public final String name;

    public Competition(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
