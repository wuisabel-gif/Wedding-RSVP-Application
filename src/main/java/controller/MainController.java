package controller;

import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Guest;
import model.RSVPStatus;
import repository.GuestRepository;
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
        guestTable.setItems(repository.getAllGuests());

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

        view.getSummaryPane().updateMetrics(totalGuests, acceptedGuests, declinedGuests, pendingGuests, acceptedGuests);
    }

    private void seedSampleData() {
        repository.addGuest(new Guest(0, "Alex Rivera", "alex@example.com", 2, RSVPStatus.ACCEPTED, model.MealOption.CHICKEN, "Prefers aisle seating"));
        repository.addGuest(new Guest(0, "Jordan Lee", "(555) 101-2828", 1, RSVPStatus.PENDING, model.MealOption.NONE, "Waiting on travel plans"));
        repository.addGuest(new Guest(0, "Priya Shah", "priya@example.com", 3, RSVPStatus.DECLINED, model.MealOption.NONE, "Sending a gift"));
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
