import java.util.Objects;
import java.util.UUID;

public class Ingredient implements Entity {
    private UUID id;
    private String name;
    private double cost;

    public Ingredient(String name, double cost) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.cost = cost;
    }

    @Override
    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Ингредиент: %s, стоимость: %.2f, ID: %s", name, cost, id);
    }
}