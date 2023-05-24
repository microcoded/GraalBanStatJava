package graalbanstat;

import java.util.Map;

public class GraphGenerator {
    private Map<String, Integer> calcMonthWarns;
    private Map<String, Integer> calcMonthBans;
    private String mY;

    public GraphGenerator(MessageGenerator mg) {
        this.calcMonthWarns = mg.getCalcMonthWarns();
        this.calcMonthBans = mg.getCalcMonthBans();
        this.mY = mg.getMY();
    }

    public Map<String, Integer> getCalcMonthWarns() {
        return calcMonthWarns;
    }

    public Map<String, Integer> getCalcMonthBans() {
        return calcMonthBans;
    }

    public String getMY() {
        return mY;
    }
}
