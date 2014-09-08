package snowmonkey.meeno.types;

import live.GenerateTestData;
import org.joda.time.DateTime;
import org.junit.Test;
import snowmonkey.meeno.types.raw.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ListMarketCatalogueTest {
    @Test
    public void test() throws Exception {
        MarketCatalogues markets = MarketCatalogues.parse(GenerateTestData.ListMarketCatalogue.listMarketCatalogueJson());
        MarketId marketId = new MarketId("1.112645443");
        MarketCatalogue marketCatalogue = markets.get(marketId);

        assertThat(marketCatalogue.competition, equalTo(new Competition(new CompetitionId("62815"), "Copa Libertadores")));
        assertThat(marketCatalogue.description, equalTo(new MarketDescription(
                true,
                false,
                new DateTime("2014-02-07T01:00:00.000Z"),
                new DateTime("2014-02-07T01:00:00.000Z"),
                null,
                MarketBettingType.ODDS,
                true,
                "TEAM_B_1",
                null,
                5.0D,
                true,
                "UK wallet",
                "<br><br><b>Market Information</b><br><br>For further information please see <a href=http://content.betfair.com/aboutus/content.asp?sWhichKey=Rules%20and%20Regulations#undefined.do style=color:0163ad; text-decoration: underline; target=_blank>Rules & Regs</a>.<br><br> Who will win this match with the stated handicap applied? <b><font color=red> All bets apply to Full Time according to the match officials, plus any stoppage time.  Extra-time/penalty shoot-outs are not included.</b></font><br><br> <b><font color=blue>If this market is re-opened for In-Play betting, </b></font> unmatched bets will be cancelled at kick off and the market turned in play. The market will be suspended if it appears that a goal has been scored, a penalty will be given, or a red card will be shown. With the exception of bets for which the \"keep\" option has been selected, unmatched bets will be cancelled in the event of a confirmed goal or sending off. Please note that should our data feeds fail we may be unable to manage this game in-play. <br><br>Customers should be aware that:<b><br><br><li>Transmissions described as “live” by some broadcasters may actually be delayed</li><br><li>The extent of any such delay may vary, depending on the set-up through which they are receiving pictures or data.</b><br><br> If this market is scheduled to go in-play, but due to unforeseen circumstances we are unable to offer the market in-play, then this market will be re-opened for the half-time interval and suspended again an hour after the scheduled kick-off time.  Whilst it is our intention to re-open this market for the half-time interval, in certain circumstances this may not always be possible.<br>",
                true,
                null)));
        assertThat(marketCatalogue.event, equalTo(new Event("27140610", "Lanus v Caracas", "AR", "GMT", null, new DateTime("2014-02-07T01:00:00.000Z"))));
        assertThat(marketCatalogue.eventType, equalTo(new EventType(new EventTypeId("1"), "Soccer")));
        assertThat(marketCatalogue.marketId, equalTo(marketId));
        assertThat(marketCatalogue.marketName, equalTo("Caracas +1"));
        assertThat(marketCatalogue.runners.size(), equalTo(3));
        assertThat(marketCatalogue.runners.get(0), equalTo(new RunnerCatalog(new SelectionId(7039291L), "Caracas +1", 0.0d)));
        assertThat(marketCatalogue.runners.get(1), equalTo(new RunnerCatalog(new SelectionId(6747604L), "Lanus -1", 0.0d)));
        assertThat(marketCatalogue.runners.get(2), equalTo(new RunnerCatalog(new SelectionId(151478L), "Draw", 0.0d)));
    }
}
