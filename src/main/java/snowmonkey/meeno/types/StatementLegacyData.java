package snowmonkey.meeno.types;

import java.time.ZonedDateTime;

public class StatementLegacyData extends ImmutbleType {
    public final double avgPrice;
    public final double betSize;
    public final String betType;
    public final String betCategoryType;
    public final String commissionRate;
    public final EventId eventId;
    public final EventTypeId eventTypeId;
    public final String fullMarketName;
    public final double grossBetAmount;
    public final String marketName;
    public final String marketType;
    public final ZonedDateTime placedDate;
    public final SelectionId selectionId;
    public final String selectionName;
    public final ZonedDateTime startDate;
    public final String transactionType;
    public final long transactionId;
    public final String winLose;

    public StatementLegacyData(double avgPrice, double betSize, String betType, String betCategoryType, String commissionRate, EventId eventId, EventTypeId eventTypeId, String fullMarketName, double grossBetAmount, String marketName, String marketType, ZonedDateTime placedDate, SelectionId selectionId, String selectionName, ZonedDateTime startDate, String transactionType, long transactionId, String winLose) {
        this.avgPrice = avgPrice;
        this.betSize = betSize;
        this.betType = betType;
        this.betCategoryType = betCategoryType;
        this.commissionRate = commissionRate;
        this.eventId = eventId;
        this.eventTypeId = eventTypeId;
        this.fullMarketName = fullMarketName;
        this.grossBetAmount = grossBetAmount;
        this.marketName = marketName;
        this.marketType = marketType;
        this.placedDate = placedDate;
        this.selectionId = selectionId;
        this.selectionName = selectionName;
        this.startDate = startDate;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.winLose = winLose;
    }
}
