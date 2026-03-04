import java.util.*;

public class PizzaService {
    private Map<UUID, Pizza> pizzas = new HashMap<>();

    public Pizza create(String name, Dough dough, List<Ingredient> ingredients) {
        Pizza pizza = new Pizza(name, dough, ingredients);
        pizzas.put(pizza.getId(), pizza);
        return pizza;
    }

    public Pizza get(UUID id) {
        return pizzas.get(id);
    }

    public List<Pizza> getAll() {
        return new ArrayList<>(pizzas.values());
    }

    public Pizza update(UUID id, String name, Dough dough, List<Ingredient> ingredients) {
        Pizza pizza = pizzas.get(id);
        if (pizza != null) {
            pizza.setName(name);
            pizza.setDough(dough);
            pizza.setIngredients(ingredients);
        }
        return pizza;
    }

    public void delete(UUID id) {
        pizzas.remove(id);
    }

    public List<Pizza> filterByIngredient(Ingredient ingredient) {
        return pizzas.values().stream()
                .filter(p -> p.getIngredients().contains(ingredient))
                .toList();
    }
}