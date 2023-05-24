package graalbanstat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CalculateBans {
    private File bansFile;
    private String calcMonth;
    private String compareMonth;

    private Map<String, Integer> previousMonthWarns;
    private Map<String, Integer> previousMonthBans;
    private Map<String, Integer> calcMonthWarns;
    private Map<String, Integer> calcMonthBans;

    MessageGenerator mg;

    public CalculateBans(File bansFile, String calcMonth) {
        this.bansFile = bansFile;
        this.calcMonth = calcMonth;
        this.compareMonth = previousMonth(calcMonth);

        this.previousMonthWarns = new TreeMap<>();
        this.previousMonthBans = new TreeMap<>();
        this.calcMonthWarns = new TreeMap<>();
        this.calcMonthBans = new TreeMap<>();
    }

    public String main() {
        setBansWarns();
        mg = new MessageGenerator(previousMonthWarns, previousMonthBans, calcMonthWarns, calcMonthBans, compareMonth, calcMonth);
        return mg.generateMessage();
    }

    private void setBansWarns() {
        Pair<Map<String, Integer>, Map<String, Integer>> prevMth = readFile(filterFile(bansFile, compareMonth));
        previousMonthBans = prevMth.getFirst();
        previousMonthWarns = prevMth.getSecond();

        Pair<Map<String, Integer>, Map<String, Integer>> currMth = readFile(filterFile(bansFile, calcMonth));
        calcMonthBans = currMth.getFirst();
        calcMonthWarns = currMth.getSecond();
    }

    private String previousMonth(String mth) {
        List<String> monthAbbreviations = Arrays.asList(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        );
        int currentMonthIndex = monthAbbreviations.indexOf(mth);
        int previousMonthIndex = (currentMonthIndex - 1 + 12) % 12;
        return monthAbbreviations.get(previousMonthIndex);
    }

    private Pair<Map<String, Integer>, Map<String, Integer>> readFile(List<String> lines) {
        Map<String, Integer> banCategories = new TreeMap<>();
        Map<String, Integer> warnCategories = new TreeMap<>();

        for (String line : lines) {
            if (line.contains("comm banned")
                    || line.contains("(npcserver) has")
                    || line.contains("uploads")
                    || line.contains("system has")
                    || line.contains("GST")) {
                // Ignore comm bans and system bans
                continue;
            }
            String[] words = line.split("\\s+");
            for (String word : words) {
                try {
                    // Attempt to parse number
                    Double.parseDouble(word);
                } catch (NumberFormatException e) {
                    // Not a number
                    if (word.equals("banned")) {
                        // Banned with reason
                        int startIndex = line.indexOf("with reason: ");
                        if (startIndex != -1) {
                            startIndex += 13;
                            int endIndex = line.indexOf(" (", startIndex);
                            String banCategory = (endIndex == -1) ? line.substring(startIndex) : line.substring(startIndex, endIndex);
                            banCategories.put(banCategory, banCategories.getOrDefault(banCategory, 0) + 1);
                        }
                    } else if (word.equals("warning")) {
                        // Warning with reason
                        int startIndex = line.indexOf("with reason: ");
                        if (startIndex != -1) {
                            startIndex += 13;
                            int endIndex = line.indexOf(" (", startIndex);
                            String warningCategory = (endIndex == -1) ? line.substring(startIndex) : line.substring(startIndex, endIndex);
                            warnCategories.put(warningCategory, warnCategories.getOrDefault(warningCategory, 0) + 1);
                        }
                    }
                }
            }
        }

        return new Pair<>(banCategories, warnCategories);
    }

    private List<String> filterFile(File fileName, String month) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String previousLine = null;
            String line = reader.readLine();
            while (line != null) {
                if (previousLine != null && previousLine.contains(month)) {
                    lines.add(previousLine);
                    lines.add(line);
                }
                previousLine = line;
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lines;
    }

    private static class Pair<T, U> {
        private T first;
        private U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }

    public MessageGenerator getMg() {
        return mg;
    }
}
