import java.util.Objects;
import java.util.UUID;

public class Dough implements Entity {
    private UUID id;
    private String name;
    private double cost;
    private boolean isClassic;

    public Dough(String name, double cost, boolean isClassic) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.cost = cost;
        this.isClassic = isClassic;
    }

    @Override
    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public boolean isClassic() { return isClassic; }
    public void setClassic(boolean classic) { isClassic = classic; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dough dough = (Dough) o;
        return Objects.equals(id, dough.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Основа: %s, стоимость: %.2f, классическая: %s, ID: %s",
                name, cost, isClassic ? "да" : "нет", id);
    }
}