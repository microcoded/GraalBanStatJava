package graalbanstat;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CSVGenerator {
    private Map<String, Integer> bansMap;
    private Map<String, Integer> warnsMap;
    private String currentMonthYear;

    public CSVGenerator(Map<String, Integer> bansMap, Map<String, Integer> warnsMap, String currentMonthYear) {
        this.bansMap = bansMap;
        this.warnsMap = warnsMap;
        this.currentMonthYear = currentMonthYear;
    }

    public void main() {
        List<Map.Entry<String, Integer>> reversedBansMap = new ArrayList<>(bansMap.entrySet());
        reversedBansMap.sort(Map.Entry.comparingByKey(Comparator.reverseOrder()));

        List<Map.Entry<String, Integer>> reversedWarnsMap = new ArrayList<>(warnsMap.entrySet());
        reversedWarnsMap.sort(Map.Entry.comparingByKey(Comparator.reverseOrder()));

        try {
            String home = System.getProperty("user.home");
            File csvFile = new File(home + "/Downloads/output.csv");

            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write("Classic iPhone Ban Statistics - " + currentMonthYear + ",\n\n");
            writer.write("Category,Bans,Warns\n");
            for (Map.Entry<String, Integer> entry : reversedBansMap) {
                String category = entry.getKey();
                int bans = entry.getValue();
                Integer warns = warnsMap.get(category);
                if (warns == null) {
                    writer.write(category + "," + bans + ",\n");
                } else {
                    writer.write(category + "," + bans + "," + warns + "\n");
                }
            }
            writer.close();

            // Open the CSV file with Microsoft Excel
            Desktop.getDesktop().open(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
