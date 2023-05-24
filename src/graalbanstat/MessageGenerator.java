package graalbanstat;

import java.time.YearMonth;
import java.util.Map;

public class MessageGenerator {
    static Map<String, Integer> previousMonthWarns;
    static Map<String, Integer> previousMonthBans;
    static Map<String, Integer> calcMonthWarns;
    static Map<String, Integer> calcMonthBans;
    static String prevMonth;
    static String calcMonth;

    private static int prevBanTotal;
    private static int prevWarnTotal;
    private static int calcBanTotal;
    private static int calcWarnTotal;

    private static String mY;
    private static String mY2;
    private static String previousMonth;
    private static String currentMonth;

    public MessageGenerator(Map<String, Integer> prevMonthWarns,
                            Map<String, Integer> prevMonthBans,
                            Map<String, Integer> curMonthWarns,
                            Map<String, Integer> curMonthBans,
                            String previousMonth,
                            String currentMonth) {
        MessageGenerator.previousMonthWarns = prevMonthWarns;
        MessageGenerator.previousMonthBans = prevMonthBans;
        MessageGenerator.calcMonthWarns = curMonthWarns;
        MessageGenerator.calcMonthBans = curMonthBans;
        MessageGenerator.prevMonth = previousMonth;
        MessageGenerator.calcMonth = currentMonth;

    }

    public static String generateMessage() {

        calculateTotals();
        dates();

        StringBuilder msg = new StringBuilder();

        msg.append("Hello everyone, the ").append(mY).append(" ban statistics have been calculated! <@&185535444262322176> <@&605934575163670539>\n\n");
        msg.append("In ").append(currentMonth).append(", we had ").append(calcBanTotal).append(" bans and ").append(calcWarnTotal).append(" warnings issued.\n");
        msg.append("The lists below compare ").append(mY2).append(" to ").append(mY).append(". An up arrow (:uparrow:) indicates an increase, and a down arrow (:downarrow:) indicates a decrease. Any category with a total of 0 this month is excluded.\n\n");
        msg.append("**Bans**\n");

        for (Map.Entry<String, Integer> entry : calcMonthBans.entrySet()) {
            String category = entry.getKey();
            int banCount = entry.getValue();
            double increase;
            int oldBanCount = previousMonthBans.getOrDefault(category, 0);
            String trend;
            String trendValue;
            if (oldBanCount != 0) {
                increase = 100.0 * (banCount - oldBanCount) / oldBanCount;
                trend = increase > 0 ? ":uparrow:" : increase < 0 ? ":downarrow:" : ":heavy_minus_sign:";
                trendValue = " (" + trend + String.format("%.1f", Math.abs(increase)) + "%)";
            } else {
                trendValue = " (:uparrow: ∞%)";
            }
            if (calcMonthBans.containsKey(category)) {
                msg.append("> *").append(category).append(":*    ").append(banCount).append(" ").append(trendValue).append("\n");
            }
        }

        msg.append("\n**Warnings**\n");
        for (Map.Entry<String, Integer> entry : calcMonthWarns.entrySet()) {
            String category = entry.getKey();
            int warnCount = entry.getValue();
            double increase;
            String trend;
            String trendValue;
            int oldWarnCount = previousMonthWarns.getOrDefault(category, 0);
            if (oldWarnCount != 0) {
                increase = 100.0 * (warnCount - oldWarnCount) / oldWarnCount;
                trend = increase > 0 ? ":uparrow:" : increase < 0 ? ":downarrow:" : ":heavy_minus_sign:";
                trendValue = " (" + trend + String.format("%.1f", Math.abs(increase)) + "%)";
            } else {
                trendValue = " (:uparrow: ∞%)";
            }
            if (calcMonthWarns.containsKey(category)) {
                msg.append("> *").append(category).append(":*    ").append(warnCount).append(" ").append(trendValue).append("\n");
            }
        }

        return msg.toString().replace("\n", System.getProperty("line.separator"));
    }

    private static void calculateTotals() {
        prevBanTotal = 0;
        prevWarnTotal = 0;
        calcBanTotal = 0;
        calcWarnTotal = 0;

        for (Map.Entry<String, Integer> entry : previousMonthBans.entrySet()) {
            prevBanTotal += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : previousMonthWarns.entrySet()) {
            prevWarnTotal += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : calcMonthBans.entrySet()) {
            calcBanTotal += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : calcMonthWarns.entrySet()) {
            calcWarnTotal += entry.getValue();
        }
    }

    private static void dates() {
        previousMonth = getFullMonthName(prevMonth);
        currentMonth = getFullMonthName(calcMonth);

        YearMonth ym = YearMonth.now().minusMonths(1);
        int year = ym.getYear();
        mY = currentMonth + " " + year;

        YearMonth ym2 = YearMonth.now().minusMonths(2);
        int year2 = ym2.getYear();
        mY2 = previousMonth + " " + year2;
    }

    private static String getFullMonthName(String abbreviatedMonth) {
        switch (abbreviatedMonth) {
            case "Jan":
                return "January";
            case "Feb":
                return "February";
            case "Mar":
                return "March";
            case "Apr":
                return "April";
            case "May":
                return "May";
            case "Jun":
                return "June";
            case "Jul":
                return "July";
            case "Aug":
                return "August";
            case "Sep":
                return "September";
            case "Oct":
                return "October";
            case "Nov":
                return "November";
            case "Dec":
                return "December";
            default:
                throw new IllegalArgumentException("Invalid month abbreviation: " + abbreviatedMonth);
        }
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
