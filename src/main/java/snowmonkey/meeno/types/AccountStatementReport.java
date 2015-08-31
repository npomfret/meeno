package snowmonkey.meeno.types;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class AccountStatementReport extends ImmutbleType {
    public final ImmutableList<StatementItem> accountStatement;
    public final boolean moreAvailable;

    public AccountStatementReport(List<StatementItem> accountStatement, boolean moreAvailable) {
        this.accountStatement = ImmutableList.copyOf(accountStatement);
        this.moreAvailable = moreAvailable;
    }
}
