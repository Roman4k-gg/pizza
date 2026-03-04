import java.util.*;
import java.util.stream.Collectors;

public class Crust implements Entity {
    private UUID id;
    private String name;
    private List<Ingredient> ingredients;
    private Set<Pizza> pizzas;
    private boolean isWhitelist;

    public Crust(String name, List<Ingredient> ingredients, Set<Pizza> pizzas, boolean isWhitelist) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.ingredients = ingredients;
        this.pizzas = pizzas != null ? new HashSet<>(pizzas) : new HashSet<>();
        this.isWhitelist = isWhitelist;
    }

    @Override
    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public Set<Pizza> getPizzas() { return pizzas; }
    public void setPizzas(Set<Pizza> pizzas) { this.pizzas = new HashSet<>(pizzas); }

    public boolean isWhitelist() { return isWhitelist; }
    public void setWhitelist(boolean whitelist) { isWhitelist = whitelist; }

    public boolean isCompatibleWith(Pizza pizza) {
        if (pizzas.isEmpty()) {
            return !isWhitelist;
        }
        if (isWhitelist) {
            return pizzas.contains(pizza);
        } else {
            return !pizzas.contains(pizza);
        }
    }

    public double getCost() {
        return ingredients.stream().mapToDouble(Ingredient::getCost).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crust crust = (Crust) o;
        return Objects.equals(id, crust.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        List<String> ingNames = ingredients.stream().map(Ingredient::getName).collect(Collectors.toList());
        List<String> pizzaNames = pizzas.stream().map(Pizza::getName).collect(Collectors.toList());
        return String.format("Бортик: %s, ингредиенты: %s, тип списка: %s, пиццы: %s, ID: %s",
                name, ingNames, isWhitelist ? "белый" : "чёрный", pizzaNames, id);
    }
}