package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import model.Guest;
import model.MealOption;
import model.RSVPStatus;

public class MainView extends BorderPane {

    // The couple this little app was built for — see the story in README.md.
    public static final String COUPLE_NAMES = "Eleanor & James";
    public static final String WEDDING_DATE = "Saturday, the twelfth of September, 2026";
    public static final String WEDDING_VENUE = "The Old Maple Orchard  ·  Sonoma, California";

    private final TableView<Guest> guestTable = new TableView<>();
    private final TextField searchField = new TextField();
    private final Button albumButton = new Button("❦  See Album");
    private final Button addButton = new Button("＋  Add Guest");
    private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");
    private final Button refreshButton = new Button("Refresh");
    private final SummaryPane summaryPane = new SummaryPane();

    public MainView() {
        getStyleClass().add("app-shell");
        setPadding(new Insets(28, 32, 22, 32));

        setTop(buildHeroBanner(buildHeader()));
        setCenter(buildContent());
        setBottom(buildFooter());
    }

    /** Wraps the hero content over the golden-hour ceremony photo with a soft ivory veil. */
    private StackPane buildHeroBanner(VBox content) {
        StackPane banner = new StackPane();
        banner.getStyleClass().add("hero-banner");
        banner.setMinHeight(Region.USE_PREF_SIZE);

        ImageView bg = new ImageView();
        var url = getClass().getResource("/images/hero.png");
        if (url != null) {
            bg.setImage(new Image(url.toExternalForm()));
        }
        bg.setManaged(false);            // don't let the photo inflate the banner height
        bg.setPreserveRatio(true);
        bg.setSmooth(true);
        double aspect = 1672.0 / 941.0;
        bg.fitWidthProperty().bind(banner.widthProperty());
        bg.layoutYProperty().bind(banner.heightProperty()
                .subtract(banner.widthProperty().divide(aspect)).divide(2));

        Region veil = new Region();
        veil.getStyleClass().add("hero-veil");

        banner.getChildren().addAll(bg, veil, content);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(36);
        clip.setArcHeight(36);
        clip.widthProperty().bind(banner.widthProperty());
        clip.heightProperty().bind(banner.heightProperty());
        banner.setClip(clip);

        setMargin(banner, new Insets(0, 0, 18, 0));
        return banner;
    }

    private VBox buildHeader() {
        MonogramCrest crest = new MonogramCrest(112);

        Label eyebrow = new Label("T O G E T H E R   W I T H   T H E I R   F A M I L I E S");
        eyebrow.getStyleClass().add("hero-eyebrow");

        Label names = new Label(COUPLE_NAMES);
        names.getStyleClass().add("couple-names");

        Label meta = new Label(WEDDING_DATE);
        meta.getStyleClass().add("hero-meta");

        Label venue = new Label(WEDDING_VENUE);
        venue.getStyleClass().add("hero-sub");

        HBox ornament = buildOrnament();

        albumButton.getStyleClass().setAll("button", "primary", "hero-album-button");
        VBox.setMargin(albumButton, new Insets(8, 0, 0, 0));

        VBox header = new VBox(2, crest, eyebrow, names, meta, venue, ornament, albumButton);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(26, 24, 26, 24));
        return header;
    }

    private HBox buildOrnament() {
        Line left = new Line(0, 0, 70, 0);
        left.getStyleClass().add("rule");
        Line right = new Line(0, 0, 70, 0);
        right.getStyleClass().add("rule");

        Label leaf = new Label("❦");
        leaf.getStyleClass().add("ornament");

        HBox ornament = new HBox(14, left, leaf, right);
        ornament.setAlignment(Pos.CENTER);
        ornament.setPadding(new Insets(8, 0, 2, 0));
        return ornament;
    }

    private VBox buildContent() {
        configureTable();

        searchField.setPromptText("Search by name, contact, meal, or note…");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(280);

        addButton.getStyleClass().add("primary");
        editButton.getStyleClass().add("ghost");
        deleteButton.getStyleClass().add("ghost");
        refreshButton.getStyleClass().add("ghost");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actions = new HBox(12, searchField, spacer, addButton, editButton, deleteButton, refreshButton);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.getStyleClass().add("actions-row");

        VBox tableSection = new VBox(16, actions, guestTable);
        VBox.setVgrow(guestTable, Priority.ALWAYS);

        HBox content = new HBox(24, tableSection, summaryPane);
        HBox.setHgrow(tableSection, Priority.ALWAYS);
        summaryPane.setMinWidth(280);
        summaryPane.setPrefWidth(300);

        VBox container = new VBox(content);
        VBox.setVgrow(content, Priority.ALWAYS);
        return container;
    }

    private Label buildFooter() {
        Label footer = new Label("Made with love by the neighbors at No. 11  ·  for Eleanor & James");
        footer.getStyleClass().add("footer-note");
        footer.setMaxWidth(Double.MAX_VALUE);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(16, 0, 2, 0));
        return footer;
    }

    private void configureTable() {
        guestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        guestTable.setPlaceholder(new Label("No replies yet — add your first guest to begin the celebration."));

        TableColumn<Guest, String> nameColumn = new TableColumn<>("Guest Name");
        nameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<Guest, String> contactColumn = new TableColumn<>("Contact Info");
        contactColumn.setCellValueFactory(cell -> cell.getValue().contactInfoProperty());

        TableColumn<Guest, Number> countColumn = new TableColumn<>("Party");
        countColumn.setCellValueFactory(cell -> cell.getValue().guestCountProperty());
        countColumn.setCellFactory(col -> centeredCell());
        countColumn.setMaxWidth(90);

        TableColumn<Guest, RSVPStatus> rsvpColumn = new TableColumn<>("RSVP");
        rsvpColumn.setCellValueFactory(cell -> cell.getValue().rsvpStatusProperty());
        rsvpColumn.setCellFactory(col -> statusBadgeCell());

        TableColumn<Guest, MealOption> mealColumn = new TableColumn<>("Meal");
        mealColumn.setCellValueFactory(cell -> cell.getValue().mealOptionProperty());
        mealColumn.setCellFactory(col -> mealChipCell());

        TableColumn<Guest, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(cell -> cell.getValue().notesProperty());

        guestTable.getColumns().addAll(nameColumn, contactColumn, countColumn, rsvpColumn, mealColumn, notesColumn);
    }

    /** Centers a plain numeric cell (the party-size column). */
    private TableCell<Guest, Number> centeredCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                getStyleClass().remove("cell-center");
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(value.intValue()));
                    getStyleClass().add("cell-center");
                }
            }
        };
    }

    /** Renders the RSVP status as a soft colored pill. */
    private TableCell<Guest, RSVPStatus> statusBadgeCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(RSVPStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }
                Label badge = new Label(status.toString());
                badge.getStyleClass().setAll("badge", "badge-" + status.name().toLowerCase());
                setGraphic(badge);
                setAlignment(Pos.CENTER_LEFT);
            }
        };
    }

    /** Shows the chosen meal as a chip, or a quiet dash when there isn't one. */
    private TableCell<Guest, MealOption> mealChipCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(MealOption meal, boolean empty) {
                super.updateItem(meal, empty);
                getStyleClass().remove("cell-muted");
                if (empty || meal == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                if (meal == MealOption.NONE) {
                    setGraphic(null);
                    setText("—");
                    getStyleClass().add("cell-muted");
                } else {
                    setText(null);
                    Label chip = new Label(meal.toString());
                    chip.getStyleClass().add("chip");
                    setGraphic(chip);
                }
            }
        };
    }

    public TableView<Guest> getGuestTable() {
        return guestTable;
    }

    public TextField getSearchField() {
        return searchField;
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

    public Button getAlbumButton() {
        return albumButton;
    }

    public SummaryPane getSummaryPane() {
        return summaryPane;
    }
}
