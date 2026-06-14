package controller;

import java.util.Locale;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Guest;
import model.MealOption;
import model.RSVPStatus;
import repository.GuestRepository;
import view.AlbumSlideshow;
import view.MainView;

public class MainController {
    private final Stage stage;
    private final GuestRepository repository = new GuestRepository();
    private final GuestFormController formController = new GuestFormController(repository);
    private final MainView view = new MainView();

    public MainController(Stage stage) {
        this.stage = stage;
        seedSampleData();
        bindView();
        updateSummary();
    }

    public Parent getView() {
        return view;
    }

    private void bindView() {
        TableView<Guest> guestTable = view.getGuestTable();

        FilteredList<Guest> filteredGuests = new FilteredList<>(repository.getAllGuests(), guest -> true);
        guestTable.setItems(filteredGuests);
        view.getSearchField().textProperty().addListener((obs, oldText, newText) ->
                filteredGuests.setPredicate(buildSearchPredicate(newText)));

        view.getAlbumButton().setOnAction(event -> AlbumSlideshow.open(stage));
        view.getAddButton().setOnAction(event -> onAddGuest());
        view.getEditButton().setOnAction(event -> onEditGuest());
        view.getDeleteButton().setOnAction(event -> onDeleteGuest());
        view.getRefreshButton().setOnAction(event -> guestTable.refresh());

        guestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateActionState());
        repository.getAllGuests().addListener((ListChangeListener<Guest>) change -> {
            updateActionState();
            updateSummary();
        });

        updateActionState();
    }

    private void onAddGuest() {
        formController.showAddDialog(stage).ifPresent(guest -> {
            repository.addGuest(guest);
            view.getGuestTable().getSelectionModel().select(guest);
            showInfo("Guest Saved", "%s has been added to the guest list.".formatted(guest.getName()));
        });
    }

    private void onEditGuest() {
        Guest selectedGuest = view.getGuestTable().getSelectionModel().getSelectedItem();
        if (selectedGuest == null) {
            showWarning("No Selection", "Choose a guest to edit.");
            return;
        }

        formController.showEditDialog(stage, selectedGuest).ifPresent(updatedGuest -> {
            repository.updateGuest(selectedGuest, updatedGuest);
            view.getGuestTable().refresh();
            updateSummary();
            showInfo("Guest Updated", "%s has been updated.".formatted(selectedGuest.getName()));
        });
    }

    private void onDeleteGuest() {
        Guest selectedGuest = view.getGuestTable().getSelectionModel().getSelectedItem();
        if (selectedGuest == null) {
            showWarning("No Selection", "Choose a guest to delete.");
            return;
        }

        if (formController.confirmDelete(stage, selectedGuest)) {
            repository.deleteGuest(selectedGuest);
            showInfo("Guest Removed", "%s has been removed from the guest list.".formatted(selectedGuest.getName()));
        }
    }

    private void updateActionState() {
        boolean hasSelection = view.getGuestTable().getSelectionModel().getSelectedItem() != null;
        view.getEditButton().setDisable(!hasSelection);
        view.getDeleteButton().setDisable(!hasSelection);
    }

    private void updateSummary() {
        int totalGuests = repository.getAllGuests().stream().mapToInt(Guest::getGuestCount).sum();
        int acceptedGuests = repository.getAllGuests().stream()
                .filter(guest -> guest.getRsvpStatus() == RSVPStatus.ACCEPTED)
                .mapToInt(Guest::getGuestCount)
                .sum();
        int declinedGuests = repository.getAllGuests().stream()
                .filter(guest -> guest.getRsvpStatus() == RSVPStatus.DECLINED)
                .mapToInt(Guest::getGuestCount)
                .sum();
        int pendingGuests = repository.getAllGuests().stream()
                .filter(guest -> guest.getRsvpStatus() == RSVPStatus.PENDING)
                .mapToInt(Guest::getGuestCount)
                .sum();

        long totalParties = repository.getAllGuests().size();
        long repliedParties = repository.getAllGuests().stream()
                .filter(guest -> guest.getRsvpStatus() != RSVPStatus.PENDING)
                .count();
        int responseRate = totalParties == 0 ? 0 : (int) Math.round(repliedParties * 100.0 / totalParties);

        view.getSummaryPane().updateMetrics(totalGuests, acceptedGuests, declinedGuests, pendingGuests, acceptedGuests, responseRate);
    }

    private java.util.function.Predicate<Guest> buildSearchPredicate(String rawQuery) {
        String query = rawQuery == null ? "" : rawQuery.trim().toLowerCase(Locale.ROOT);
        if (query.isEmpty()) {
            return guest -> true;
        }
        return guest -> {
            String haystack = String.join(" ",
                    safe(guest.getName()),
                    safe(guest.getContactInfo()),
                    safe(guest.getNotes()),
                    guest.getRsvpStatus() == null ? "" : guest.getRsvpStatus().toString(),
                    guest.getMealOption() == null ? "" : guest.getMealOption().toString()
            ).toLowerCase(Locale.ROOT);
            return haystack.contains(query);
        };
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    // A handful of the neighbors, friends, and family on Eleanor & James's list.
    private void seedSampleData() {
        repository.addGuest(new Guest(0, "Margaret & Tom Whitfield", "margaret.whitfield@example.com", 2, RSVPStatus.ACCEPTED, MealOption.CHICKEN, "Next-door neighbors — bringing the toast"));
        repository.addGuest(new Guest(0, "Daniel Calloway", "(555) 318-4420", 1, RSVPStatus.ACCEPTED, MealOption.BEEF, "James's brother & best man"));
        repository.addGuest(new Guest(0, "Sophie Hart", "sophie.hart@example.com", 2, RSVPStatus.ACCEPTED, MealOption.VEGETARIAN, "Maid of honor (Eleanor's sister)"));
        repository.addGuest(new Guest(0, "The Nguyen Family", "lan.nguyen@example.com", 4, RSVPStatus.PENDING, MealOption.NONE, "Checking on the kids' babysitter"));
        repository.addGuest(new Guest(0, "Professor Alan Reyes", "(555) 902-7711", 1, RSVPStatus.PENDING, MealOption.NONE, "Travelling back from a conference"));
        repository.addGuest(new Guest(0, "Grace & Omar Bishara", "grace.bishara@example.com", 2, RSVPStatus.DECLINED, MealOption.NONE, "Out of town — sending their love"));
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.initOwner(stage);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.initOwner(stage);
        alert.setHeaderText(title);
        alert.showAndWait();
    }
}
