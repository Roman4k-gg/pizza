import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Pizza implements Entity {
    private UUID id;
    private String name;
    private Dough dough;
    private List<Ingredient> ingredients;

    public Pizza(String name, Dough dough, List<Ingredient> ingredients) {
        if (dough == null) {
            throw new IllegalArgumentException("Пицца должна иметь основу");
        }
        this.id = UUID.randomUUID();
        this.name = name;
        this.dough = dough;
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Dough getDough() { return dough; }
    public void setDough(Dough dough) {
        if (dough == null) throw new IllegalArgumentException("Основа не может быть null");
        this.dough = dough;
    }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = new ArrayList<>(ingredients); }

    public double getBaseCost() {
        double ingredientsCost = ingredients.stream().mapToDouble(Ingredient::getCost).sum();
        return ingredientsCost + dough.getCost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pizza pizza = (Pizza) o;
        return Objects.equals(id, pizza.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        List<String> ingNames = ingredients.stream().map(Ingredient::getName).collect(Collectors.toList());
        return String.format("Пицца: %s, основа: %s, ингредиенты: %s, базовая стоимость: %.2f, ID: %s",
                name, dough.getName(), ingNames, getBaseCost(), id);
    }
}