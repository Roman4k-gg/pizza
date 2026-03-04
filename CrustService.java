import java.util.*;

public class CrustService {
    private Map<UUID, Crust> crusts = new HashMap<>();

    public Crust create(String name, List<Ingredient> ingredients, Set<Pizza> pizzas, boolean isWhitelist) {
        Crust crust = new Crust(name, ingredients, pizzas, isWhitelist);
        crusts.put(crust.getId(), crust);
        return crust;
    }

    public Crust get(UUID id) {
        return crusts.get(id);
    }

    public List<Crust> getAll() {
        return new ArrayList<>(crusts.values());
    }

    public Crust update(UUID id, String name, List<Ingredient> ingredients, Set<Pizza> pizzas, Boolean isWhitelist) {
        Crust crust = crusts.get(id);
        if (crust != null) {
            crust.setName(name);
            crust.setIngredients(ingredients);
            if (pizzas != null) crust.setPizzas(pizzas);
            if (isWhitelist != null) crust.setWhitelist(isWhitelist);
        }
        return crust;
    }

    public void delete(UUID id) {
        crusts.remove(id);
    }
}