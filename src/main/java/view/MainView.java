package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Guest;
import model.MealOption;
import model.RSVPStatus;

public class MainView extends BorderPane {
    private final TableView<Guest> guestTable = new TableView<>();
    private final Button addButton = new Button("Add Guest");
    private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");
    private final Button refreshButton = new Button("Refresh");
    private final SummaryPane summaryPane = new SummaryPane();

    public MainView() {
        getStyleClass().add("app-shell");
        setPadding(new Insets(24));

        setTop(buildHeader());
        setCenter(buildContent());
    }

    private VBox buildHeader() {
        Label eyebrow = new Label("Wedding Guest Manager");
        eyebrow.getStyleClass().add("eyebrow");

        Label heading = new Label("Track RSVPs, meals, and party sizes in one place.");
        heading.getStyleClass().add("screen-title");

        VBox header = new VBox(4, eyebrow, heading);
        header.setPadding(new Insets(0, 0, 20, 0));
        return header;
    }

    private VBox buildContent() {
        configureTable();

        HBox actions = new HBox(12, addButton, editButton, deleteButton, refreshButton);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.getStyleClass().add("actions-row");

        VBox tableSection = new VBox(16, actions, guestTable);
        VBox.setVgrow(guestTable, Priority.ALWAYS);

        HBox content = new HBox(24, tableSection, summaryPane);
        HBox.setHgrow(tableSection, Priority.ALWAYS);
        summaryPane.setMinWidth(250);
        summaryPane.setPrefWidth(280);

        VBox container = new VBox(content);
        VBox.setVgrow(content, Priority.ALWAYS);
        return container;
    }

    private void configureTable() {
        guestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        guestTable.setPlaceholder(new Label("No guest responses yet. Add a guest to get started."));

        TableColumn<Guest, String> nameColumn = new TableColumn<>("Guest Name");
        nameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<Guest, String> contactColumn = new TableColumn<>("Contact Info");
        contactColumn.setCellValueFactory(cell -> cell.getValue().contactInfoProperty());

        TableColumn<Guest, Number> countColumn = new TableColumn<>("Party Size");
        countColumn.setCellValueFactory(cell -> cell.getValue().guestCountProperty());

        TableColumn<Guest, RSVPStatus> rsvpColumn = new TableColumn<>("RSVP");
        rsvpColumn.setCellValueFactory(cell -> cell.getValue().rsvpStatusProperty());

        TableColumn<Guest, MealOption> mealColumn = new TableColumn<>("Meal");
        mealColumn.setCellValueFactory(cell -> cell.getValue().mealOptionProperty());

        TableColumn<Guest, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(cell -> cell.getValue().notesProperty());

        guestTable.getColumns().addAll(nameColumn, contactColumn, countColumn, rsvpColumn, mealColumn, notesColumn);
    }

    public TableView<Guest> getGuestTable() {
        return guestTable;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public SummaryPane getSummaryPane() {
        return summaryPane;
    }
}
