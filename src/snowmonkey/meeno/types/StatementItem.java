package snowmonkey.meeno.types;

import com.google.common.collect.ImmutableMap;

import java.time.ZonedDateTime;
import java.util.Map;

public class StatementItem extends ImmutbleType {
    public final String refId;
    public final ZonedDateTime itemDate;
    public final double amount;
    public final double balance;
    public final ItemClass itemClass;
    public final ImmutableMap<String, String> itemClassData;
    public final StatementLegacyData legacyData;

    public StatementItem(String refId, ZonedDateTime itemDate, double amount, double balance, ItemClass itemClass, Map<String, String> itemClassData, StatementLegacyData legacyData) {
        this.refId = refId;
        this.itemDate = itemDate;
        this.amount = amount;
        this.balance = balance;
        this.itemClass = itemClass;
        this.itemClassData = ImmutableMap.copyOf(itemClassData);
        this.legacyData = legacyData;
    }


}
