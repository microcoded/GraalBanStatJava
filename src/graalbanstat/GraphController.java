package graalbanstat;

import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

public class GraphController {
    @FXML
    private StackedBarChart<Integer, String> banWarnChart;

    private GraphGenerator gg;
    private XYChart.Series<Integer, String> banSeries = new XYChart.Series<>();
    private XYChart.Series<Integer, String> warnSeries = new XYChart.Series<>();

    @FXML
    public void initialize() {
    }

    public void initData(GraphGenerator gg) {
        this.gg = gg;
        banWarnChart.setTitle("Classic iPhone Ban Statistics — " + gg.getMY());

        banSeries.setName("Bans");
        warnSeries.setName("Warns");

        for (String s : gg.getCalcMonthBans().keySet()) {
            Integer i = gg.getCalcMonthBans().get(s);
            XYChart.Data<Integer, String> d = new XYChart.Data<>(i, s);
            banSeries.getData().add(d);
        }

        for (String s : gg.getCalcMonthWarns().keySet()) {
            Integer i = gg.getCalcMonthWarns().get(s);
            XYChart.Data<Integer, String> d = new XYChart.Data<>(i, s);
            warnSeries.getData().add(d);
        }

        banWarnChart.getData().addAll(banSeries, warnSeries);
    }
}
