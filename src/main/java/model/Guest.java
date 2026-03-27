package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Guest {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty contactInfo = new SimpleStringProperty("");
    private final IntegerProperty guestCount = new SimpleIntegerProperty(1);
    private final ObjectProperty<RSVPStatus> rsvpStatus = new SimpleObjectProperty<>(RSVPStatus.PENDING);
    private final ObjectProperty<MealOption> mealOption = new SimpleObjectProperty<>(MealOption.NONE);
    private final StringProperty notes = new SimpleStringProperty("");

    public Guest(long id, String name, String contactInfo, int guestCount, RSVPStatus rsvpStatus,
                 MealOption mealOption, String notes) {
        setId(id);
        setName(name);
        setContactInfo(contactInfo);
        setGuestCount(guestCount);
        setRsvpStatus(rsvpStatus);
        setMealOption(mealOption);
        setNotes(notes);
    }

    public Guest copy() {
        return new Guest(getId(), getName(), getContactInfo(), getGuestCount(), getRsvpStatus(), getMealOption(), getNotes());
    }

    public void updateFrom(Guest other) {
        setName(other.getName());
        setContactInfo(other.getContactInfo());
        setGuestCount(other.getGuestCount());
        setRsvpStatus(other.getRsvpStatus());
        setMealOption(other.getMealOption());
        setNotes(other.getNotes());
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public LongProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo.get();
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo.set(contactInfo);
    }

    public StringProperty contactInfoProperty() {
        return contactInfo;
    }

    public int getGuestCount() {
        return guestCount.get();
    }

    public void setGuestCount(int guestCount) {
        this.guestCount.set(guestCount);
    }

    public IntegerProperty guestCountProperty() {
        return guestCount;
    }

    public RSVPStatus getRsvpStatus() {
        return rsvpStatus.get();
    }

    public void setRsvpStatus(RSVPStatus rsvpStatus) {
        this.rsvpStatus.set(rsvpStatus);
    }

    public ObjectProperty<RSVPStatus> rsvpStatusProperty() {
        return rsvpStatus;
    }

    public MealOption getMealOption() {
        return mealOption.get();
    }

    public void setMealOption(MealOption mealOption) {
        this.mealOption.set(mealOption);
    }

    public ObjectProperty<MealOption> mealOptionProperty() {
        return mealOption;
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public StringProperty notesProperty() {
        return notes;
    }

    @Override
    public String toString() {
        return "%s (%s, %d guest%s)".formatted(getName(), getRsvpStatus(), getGuestCount(), getGuestCount() == 1 ? "" : "s");
    }
}
