package interfaces;

import javafx.util.Pair;

import java.util.List;

public interface IPresentable {
    boolean showGraph();
    Integer getMaxYAxisValue();
    List<Pair<String, List<Double>>> getGraphDataSeries();
    List<Pair<String, String>> getLabelsAndValues();
    void deleteFromDataContext();
}
