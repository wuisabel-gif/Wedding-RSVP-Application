package model;

public enum RSVPStatus {
    ACCEPTED,
    DECLINED,
    PENDING;

    @Override
    public String toString() {
        String lower = name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
