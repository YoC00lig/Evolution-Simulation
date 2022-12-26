package agh.ics.oop.gui;
import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Statistics;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
public class LineCharts {
    private final LineChart chart;
    private final Series series =  new Series();


    public LineCharts(String title, String ysName) {

        final NumberAxis x = new NumberAxis();
        final NumberAxis y = new NumberAxis();

        x.setLabel("Days");
        y.setLabel(ysName);

        chart = new LineChart(x, y);
        chart.getData().add(series);
        chart.setTitle(title);
        chart.setStyle("-fx-background-color: #eea29a;");
        chart.setCreateSymbols(false);
    }

    public void handler(int caseID, AbstractWorldMap map) {
        Statistics stats = new Statistics(map);
        int day = map.day;
        int value = switch (caseID){
            case 1 -> map.listOfAnimals.size();
            case 2 -> map.grasses.size();
            case 3 -> map.freeFields();
            case 4 -> stats.averageEnergy();
            default -> stats.averageLifeLength();
        };
        addToSeries(day, value);
    }

    public void addToSeries(int day, int value) {
        series.getData().add(new XYChart.Data(day, value));
    }
    public LineChart getChart(){
        return chart;
    }
}