package controller;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Guest;
import model.MealOption;
import model.RSVPStatus;
import repository.GuestRepository;
import util.GuestValidator;
import view.GuestFormView;

public class GuestFormController {
    private final GuestRepository repository;

    public GuestFormController(GuestRepository repository) {
        this.repository = repository;
    }

    public Optional<Guest> showAddDialog(Window owner) {
        return showDialog(owner, null);
    }

    public Optional<Guest> showEditDialog(Window owner, Guest guest) {
        return showDialog(owner, guest);
    }

    private Optional<Guest> showDialog(Window owner, Guest existingGuest) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(existingGuest == null ? "Add Guest" : "Edit Guest");

        GuestFormView view = new GuestFormView();
        Scene scene = new Scene(new StackPane(view), 520, 540);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        ((StackPane) scene.getRoot()).setPadding(new Insets(8));
        dialog.setScene(scene);

        Guest workingCopy = existingGuest == null
                ? new Guest(0, "", "", 1, RSVPStatus.PENDING, MealOption.NONE, "")
                : existingGuest.copy();

        populateFields(view, workingCopy);
        bindDynamicBehavior(view);

        final Guest[] result = new Guest[1];

        view.getSaveButton().setOnAction(event -> {
            Guest candidate = buildGuestFromForm(view, workingCopy.getId());
            List<String> errors = GuestValidator.validate(candidate, repository, existingGuest);

            if (!errors.isEmpty()) {
                view.getMessageLabel().setText(String.join("\n", errors));
                return;
            }

            result[0] = candidate;
            dialog.close();
        });

        view.getCancelButton().setOnAction(event -> dialog.close());

        // On macOS a modal dialog can open hidden behind its owner window, which
        // makes the whole app look frozen. Center it over the owner and force it
        // to the front once it is shown.
        dialog.setResizable(false);
        centerOnOwner(dialog, owner, 520, 540);
        dialog.setOnShown(event -> {
            dialog.toFront();
            dialog.requestFocus();
            view.getNameField().requestFocus();
        });

        dialog.showAndWait();
        return Optional.ofNullable(result[0]);
    }

    private void centerOnOwner(Stage dialog, Window owner, double width, double height) {
        if (owner == null || Double.isNaN(owner.getX()) || owner.getWidth() <= 0) {
            return;
        }
        dialog.setX(owner.getX() + (owner.getWidth() - width) / 2);
        dialog.setY(owner.getY() + (owner.getHeight() - height) / 2);
    }

    private void populateFields(GuestFormView view, Guest guest) {
        view.getNameField().setText(guest.getName());
        view.getContactField().setText(guest.getContactInfo());
        view.getGuestCountSpinner().getValueFactory().setValue(guest.getGuestCount());
        view.getRsvpStatusBox().setValue(guest.getRsvpStatus());
        view.getMealOptionBox().setValue(guest.getMealOption());
        view.getNotesArea().setText(guest.getNotes());
    }

    private void bindDynamicBehavior(GuestFormView view) {
        Runnable mealStateUpdater = () -> {
            RSVPStatus status = view.getRsvpStatusBox().getValue();
            boolean accepted = status == RSVPStatus.ACCEPTED;
            view.getMealOptionBox().setDisable(!accepted);
            if (!accepted) {
                view.getMealOptionBox().setValue(MealOption.NONE);
            } else if (view.getMealOptionBox().getValue() == null || view.getMealOptionBox().getValue() == MealOption.NONE) {
                view.getMealOptionBox().setValue(MealOption.CHICKEN);
            }
        };

        view.getRsvpStatusBox().valueProperty().addListener((obs, oldValue, newValue) -> mealStateUpdater.run());
        mealStateUpdater.run();
    }

    private Guest buildGuestFromForm(GuestFormView view, long id) {
        Integer guestCount = view.getGuestCountSpinner().getValue();
        return new Guest(
                id,
                view.getNameField().getText().trim(),
                view.getContactField().getText().trim(),
                guestCount == null ? 0 : guestCount,
                view.getRsvpStatusBox().getValue(),
                view.getMealOptionBox().getValue(),
                view.getNotesArea().getText().trim()
        );
    }

    public boolean confirmDelete(Window owner, Guest guest) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Remove %s from the guest list?".formatted(guest.getName()),
                ButtonType.CANCEL,
                ButtonType.OK);
        alert.initOwner(owner);
        alert.setHeaderText("Delete Guest");
        return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }
}
