package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.Wallet;

public class TransferFunds extends ImmutbleType {
    public final Wallet from;
    public final Wallet to;
    public final double amount;

    public TransferFunds(Wallet from, Wallet to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
