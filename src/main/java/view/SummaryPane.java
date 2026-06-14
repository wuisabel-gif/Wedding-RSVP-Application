package view;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SummaryPane extends VBox {

    // The big day — also used for the live countdown.
    private static final LocalDate WEDDING_DAY = LocalDate.of(2026, 9, 12);

    private final Label attendingValue = new Label("0");
    private final Label totalValue = new Label("0");
    private final Label declinedValue = new Label("0");
    private final Label pendingValue = new Label("0");
    private final Label platesValue = new Label("0");
    private final Label responseValue = new Label("0%");
    private final Label countdownNumber = new Label("—");
    private final Label countdownCaption = new Label("");

    public SummaryPane() {
        getStyleClass().add("summary-pane");
        setSpacing(16);
        setPadding(new Insets(24, 22, 22, 22));

        Label title = new Label("The Celebration So Far");
        title.getStyleClass().add("panel-title");

        Label subtitle = new Label("A living tally of every reply");
        subtitle.getStyleClass().add("panel-sub");

        getChildren().addAll(title, subtitle, buildHero(), divider(), buildMetricGrid(),
                divider(), buildResponseRow(), buildCountdownCard());

        refreshCountdown();
    }

    private VBox buildHero() {
        attendingValue.getStyleClass().add("stat-hero-number");
        Label caption = new Label("guests joining the celebration");
        caption.getStyleClass().add("stat-hero-caption");

        VBox hero = new VBox(-2, attendingValue, caption);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(4, 0, 4, 0));
        return hero;
    }

    private GridPane buildMetricGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(12);

        addMetric(grid, 0, "Total invited", totalValue);
        addMetric(grid, 1, "Politely declined", declinedValue);
        addMetric(grid, 2, "Awaiting reply", pendingValue);
        addMetric(grid, 3, "Plates to prepare", platesValue);
        return grid;
    }

    private VBox buildResponseRow() {
        Label label = new Label("Response rate");
        label.getStyleClass().add("metric-label");
        responseValue.getStyleClass().add("metric-value");

        GridPane row = new GridPane();
        row.setHgap(18);
        row.add(label, 0, 0);
        row.add(responseValue, 1, 0);
        GridPane.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);

        return new VBox(row);
    }

    private VBox buildCountdownCard() {
        countdownNumber.getStyleClass().add("countdown-number");
        countdownCaption.getStyleClass().add("countdown-caption");

        VBox card = new VBox(2, countdownNumber, countdownCaption);
        card.getStyleClass().add("countdown-card");
        card.setAlignment(Pos.CENTER);
        VBox.setMargin(card, new Insets(4, 0, 0, 0));
        return card;
    }

    private Region divider() {
        Region line = new Region();
        line.getStyleClass().add("summary-divider");
        line.setMaxWidth(Double.MAX_VALUE);
        return line;
    }

    public void updateMetrics(int totalGuests, int acceptedGuests, int declinedGuests,
                              int pendingGuests, int estimatedMeals, int responseRatePercent) {
        attendingValue.setText(String.valueOf(acceptedGuests));
        totalValue.setText(String.valueOf(totalGuests));
        declinedValue.setText(String.valueOf(declinedGuests));
        pendingValue.setText(String.valueOf(pendingGuests));
        platesValue.setText(String.valueOf(estimatedMeals));
        responseValue.setText(responseRatePercent + "%");
    }

    private void refreshCountdown() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), WEDDING_DAY);
        if (days > 1) {
            countdownNumber.setText(days + " days");
            countdownCaption.setText("until they say “I do”");
        } else if (days == 1) {
            countdownNumber.setText("Tomorrow");
            countdownCaption.setText("the big day is almost here");
        } else if (days == 0) {
            countdownNumber.setText("Today!");
            countdownCaption.setText("wishing them a lifetime of joy");
        } else {
            countdownNumber.setText("Just married");
            countdownCaption.setText("with love, always");
        }
    }

    private void addMetric(GridPane grid, int row, String labelText, Label valueLabel) {
        Label label = new Label(labelText);
        label.getStyleClass().add("metric-label");
        label.setMaxWidth(Double.MAX_VALUE);
        valueLabel.getStyleClass().add("metric-value");
        grid.add(label, 0, row);
        grid.add(valueLabel, 1, row);
        GridPane.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);
    }
}
