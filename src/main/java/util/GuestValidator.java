package util;

import java.util.ArrayList;
import java.util.List;

import model.Guest;
import model.MealOption;
import model.RSVPStatus;
import repository.GuestRepository;

public final class GuestValidator {
    private GuestValidator() {
    }

    public static List<String> validate(Guest guest, GuestRepository repository, Guest existingGuest) {
        List<String> errors = new ArrayList<>();

        if (guest.getName() == null || guest.getName().trim().isEmpty()) {
            errors.add("Guest name is required.");
        }

        if (guest.getGuestCount() <= 0) {
            errors.add("Guest count must be a positive number.");
        }

        if (guest.getRsvpStatus() == RSVPStatus.ACCEPTED && (guest.getMealOption() == null || guest.getMealOption() == MealOption.NONE)) {
            errors.add("Please choose a meal option when RSVP status is Accepted.");
        }

        if (guest.getRsvpStatus() != RSVPStatus.ACCEPTED) {
            guest.setMealOption(MealOption.NONE);
        }

        if (repository.isDuplicateName(guest.getName(), existingGuest)) {
            errors.add("A guest with this name already exists.");
        }

        String contactInfo = guest.getContactInfo() == null ? "" : guest.getContactInfo().trim();
        if (!contactInfo.isEmpty() && !isValidContact(contactInfo)) {
            errors.add("Contact info should look like an email address or phone number.");
        }

        return errors;
    }

    private static boolean isValidContact(String value) {
        return value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || value.matches("^[0-9+()\\-\\s]{7,}$");
    }
}
