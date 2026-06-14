package view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.MealOption;
import model.RSVPStatus;

public class GuestFormView extends VBox {
    private final TextField nameField = new TextField();
    private final TextField contactField = new TextField();
    private final Spinner<Integer> guestCountSpinner = new Spinner<>();
    private final ComboBox<RSVPStatus> rsvpStatusBox = new ComboBox<>();
    private final ComboBox<MealOption> mealOptionBox = new ComboBox<>();
    private final TextArea notesArea = new TextArea();
    private final Label messageLabel = new Label();
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    public GuestFormView() {
        setSpacing(18);
        setPadding(new Insets(20));
        getStyleClass().add("form-pane");

        Label title = new Label("Guest Details");
        title.getStyleClass().add("panel-title");

        Label subtitle = new Label("Every reply brings the big day closer.");
        subtitle.getStyleClass().add("panel-sub");

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(14);

        // Fixed label column so the field names never collapse into "…",
        // and a growing field column that fills the rest of the dialog.
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setMinWidth(110);
        labelColumn.setHalignment(HPos.LEFT);
        ColumnConstraints fieldColumn = new ColumnConstraints();
        fieldColumn.setHgrow(Priority.ALWAYS);
        fieldColumn.setFillWidth(true);
        grid.getColumnConstraints().addAll(labelColumn, fieldColumn);

        guestCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
        guestCountSpinner.setEditable(true);

        rsvpStatusBox.getItems().setAll(RSVPStatus.values());
        mealOptionBox.getItems().setAll(MealOption.values());

        notesArea.setPrefRowCount(4);
        notesArea.setWrapText(true);

        int row = 0;
        addRow(grid, row++, "Guest Name", nameField);
        addRow(grid, row++, "Contact Info", contactField);
        addRow(grid, row++, "Party Size", guestCountSpinner);
        addRow(grid, row++, "RSVP Status", rsvpStatusBox);
        addRow(grid, row++, "Meal Option", mealOptionBox);
        addRow(grid, row, "Notes", notesArea);

        messageLabel.getStyleClass().add("form-message");
        messageLabel.setWrapText(true);

        saveButton.getStyleClass().add("primary");
        cancelButton.getStyleClass().add("ghost");
        HBox actions = new HBox(12, saveButton, cancelButton);

        getChildren().addAll(title, subtitle, grid, messageLabel, actions);
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node node) {
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");
        label.setMinWidth(Region.USE_PREF_SIZE);
        grid.add(label, 0, row);
        grid.add(node, 1, row);
        GridPane.setHgrow(node, Priority.ALWAYS);
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getContactField() {
        return contactField;
    }

    public Spinner<Integer> getGuestCountSpinner() {
        return guestCountSpinner;
    }

    public ComboBox<RSVPStatus> getRsvpStatusBox() {
        return rsvpStatusBox;
    }

    public ComboBox<MealOption> getMealOptionBox() {
        return mealOptionBox;
    }

    public TextArea getNotesArea() {
        return notesArea;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}
