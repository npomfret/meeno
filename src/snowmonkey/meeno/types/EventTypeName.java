package snowmonkey.meeno.types;

public class EventTypeName extends MicroType<String> {
    public static final EventTypeName AMERICAN_FOOTBALL = new EventTypeName("American Football");
    public static final EventTypeName AUSTRALIAN_RULES = new EventTypeName("Australian Rules");
    public static final EventTypeName BASEBALL = new EventTypeName("Baseball");
    public static final EventTypeName BASKETBALL = new EventTypeName("Basketball");
    public static final EventTypeName BOWLS = new EventTypeName("Bowls");
    public static final EventTypeName BOXING = new EventTypeName("Boxing");
    public static final EventTypeName CHESS = new EventTypeName("Chess");
    public static final EventTypeName CRICKET = new EventTypeName("Cricket");
    public static final EventTypeName CYCLING = new EventTypeName("Cycling");
    public static final EventTypeName DARTS = new EventTypeName("Darts");
    public static final EventTypeName FINANCIAL_BETS = new EventTypeName("Financial Bets");
    public static final EventTypeName GAELIC_GAMES = new EventTypeName("Gaelic Games");
    public static final EventTypeName GOLF = new EventTypeName("Golf");
    public static final EventTypeName GREYHOUND_RACING = new EventTypeName("Greyhound Racing");
    public static final EventTypeName HANDBALL = new EventTypeName("Handball");
    public static final EventTypeName HORSE_RACING = new EventTypeName("Horse Racing");
    public static final EventTypeName ICE_HOCKEY = new EventTypeName("Ice Hockey");
    public static final EventTypeName MIXED_MARTIAL_ARTS = new EventTypeName("Mixed Martial Arts");
    public static final EventTypeName MOTOR_SPORT = new EventTypeName("Motor Sport");
    public static final EventTypeName POKER = new EventTypeName("Poker");
    public static final EventTypeName POLITICS = new EventTypeName("Politics");
    public static final EventTypeName POOL = new EventTypeName("Pool");
    public static final EventTypeName RUGBY_LEAGUE = new EventTypeName("Rugby League");
    public static final EventTypeName RUGBY_UNION = new EventTypeName("Rugby Union");
    public static final EventTypeName SNOOKER = new EventTypeName("Snooker");
    public static final EventTypeName SOCCER = new EventTypeName("Soccer");
    public static final EventTypeName SPECIAL_BETS = new EventTypeName("Special Bets");
    public static final EventTypeName TENNIS = new EventTypeName("Tennis");
    public static final EventTypeName VOLLEYBALL = new EventTypeName("Volleyball");

    public EventTypeName(String value) {
        super(value);
    }

    public String betfairName() {
        return value;
    }
}
