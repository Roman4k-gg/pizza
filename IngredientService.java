import java.util.*;

public class IngredientService {
    private Map<UUID, Ingredient> ingredients = new HashMap<>();

    public Ingredient create(String name, double cost) {
        Ingredient ingredient = new Ingredient(name, cost);
        ingredients.put(ingredient.getId(), ingredient);
        return ingredient;
    }

    public Ingredient get(UUID id) {
        return ingredients.get(id);
    }

    public List<Ingredient> getAll() {
        return new ArrayList<>(ingredients.values());
    }

    public Ingredient update(UUID id, String name, double cost) {
        Ingredient ingredient = ingredients.get(id);
        if (ingredient != null) {
            ingredient.setName(name);
            ingredient.setCost(cost);
        }
        return ingredient;
    }

    public void delete(UUID id) {
        ingredients.remove(id);
    }
}