package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SummaryPane extends VBox {
    private final Label totalValue = new Label("0");
    private final Label acceptedValue = new Label("0");
    private final Label declinedValue = new Label("0");
    private final Label pendingValue = new Label("0");
    private final Label mealsValue = new Label("0");

    public SummaryPane() {
        getStyleClass().add("summary-pane");
        setSpacing(12);
        setPadding(new Insets(18));

        Label title = new Label("RSVP Summary");
        title.getStyleClass().add("panel-title");

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);

        addMetric(grid, 0, "Total Invited", totalValue);
        addMetric(grid, 1, "Accepted", acceptedValue);
        addMetric(grid, 2, "Declined", declinedValue);
        addMetric(grid, 3, "Pending", pendingValue);
        addMetric(grid, 4, "Estimated Meals", mealsValue);

        getChildren().addAll(title, grid);
    }

    public void updateMetrics(int totalGuests, int acceptedGuests, int declinedGuests, int pendingGuests, int estimatedMeals) {
        totalValue.setText(String.valueOf(totalGuests));
        acceptedValue.setText(String.valueOf(acceptedGuests));
        declinedValue.setText(String.valueOf(declinedGuests));
        pendingValue.setText(String.valueOf(pendingGuests));
        mealsValue.setText(String.valueOf(estimatedMeals));
    }

    private void addMetric(GridPane grid, int row, String labelText, Label valueLabel) {
        Label label = new Label(labelText);
        label.getStyleClass().add("metric-label");
        valueLabel.getStyleClass().add("metric-value");
        grid.add(label, 0, row);
        grid.add(valueLabel, 1, row);
    }
}
