package snowmonkey.meeno.types;

public class TransferResponse extends ImmutbleType {
    public final String transactionId;

    public TransferResponse(String transactionId) {
        this.transactionId = transactionId;
    }
}
