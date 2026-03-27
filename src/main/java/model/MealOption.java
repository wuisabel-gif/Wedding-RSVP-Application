package model;

public enum MealOption {
    BEEF,
    CHICKEN,
    VEGETARIAN,
    NONE;

    @Override
    public String toString() {
        String lower = name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
