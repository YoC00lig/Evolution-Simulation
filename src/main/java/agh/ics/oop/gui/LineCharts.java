package agh.ics.oop.gui;
import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Statistics;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineCharts {
    private LineChart chart;
    private XYChart.Series series;

    public LineCharts(String title) {
        NumberAxis xs = new NumberAxis();
        xs.setLabel("Days");
        this.chart = new LineChart(xs, new NumberAxis());
        series = new XYChart.Series();
        chart.setTitle(title);
        chart.setStyle("-fx-background-color: #eea29a;");
        chart.setCreateSymbols(true);
        chart.getData().add(series);
    }

    public LineChart getChart(){
        return chart;
    }

    public void updateAnimalsNumber(AbstractWorldMap map){
        series.getData().add(new XYChart.Data(map.day, map.listOfAnimals.size()));
    }

    public void updatePlantsNumber(AbstractWorldMap map){
        series.getData().add(new XYChart.Data(map.day, map.grasses.size()));
    }

    public void updateEnergy(AbstractWorldMap map){
        Statistics stats = new Statistics(map);
        series.getData().add(new XYChart.Data(map.day, stats.averageEnergy()));
    }

    public void updateLifeLength(AbstractWorldMap map){
        Statistics stats = new Statistics(map);
        series.getData().add(new XYChart.Data(map.day, stats.averageLifeLength()));
    }

    public void updateFreeFields(AbstractWorldMap map){
        series.getData().add(new XYChart.Data(map.day, map.freeFields()));
    }
}
