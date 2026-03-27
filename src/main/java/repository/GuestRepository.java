package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Guest;

public class GuestRepository {
    private final ObservableList<Guest> guests = FXCollections.observableArrayList();
    private long nextId = 1;

    public ObservableList<Guest> getAllGuests() {
        return guests;
    }

    public Guest addGuest(Guest guest) {
        guest.setId(nextId++);
        guests.add(guest);
        return guest;
    }

    public void updateGuest(Guest existingGuest, Guest updatedGuest) {
        existingGuest.updateFrom(updatedGuest);
    }

    public void deleteGuest(Guest guest) {
        guests.remove(guest);
    }

    public boolean isDuplicateName(String name, Guest excludedGuest) {
        String normalizedName = normalize(name);
        return guests.stream()
                .filter(guest -> excludedGuest == null || guest.getId() != excludedGuest.getId())
                .map(guest -> normalize(guest.getName()))
                .anyMatch(normalizedName::equals);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
