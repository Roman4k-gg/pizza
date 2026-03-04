public enum Size {
    SMALL(0.8),
    MEDIUM(1.0),
    LARGE(1.2);

    private final double multiplier;

    Size(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public static Size fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Размер не может быть null");
        switch (s.toLowerCase()) {
            case "маленькая": return SMALL;
            case "средняя": return MEDIUM;
            case "большая": return LARGE;
            default: throw new IllegalArgumentException("Неверный размер: " + s);
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case SMALL: return "маленькая";
            case MEDIUM: return "средняя";
            case LARGE: return "большая";
            default: return super.toString();
        }
    }
}