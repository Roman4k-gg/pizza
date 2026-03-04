import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogHalf extends Half {
    private Pizza pizza;
    private Map<Ingredient, Integer> multipliers;
    private Crust crust;

    public CatalogHalf(Pizza pizza, Map<Ingredient, Integer> multipliers, Crust crust) {
        this.pizza = pizza;
        this.multipliers = multipliers != null ? new HashMap<>(multipliers) : new HashMap<>();
        this.crust = crust;
        for (Ingredient ing : this.multipliers.keySet()) {
            if (!pizza.getIngredients().contains(ing)) {
                throw new IllegalArgumentException("Ингредиент " + ing.getName() + " отсутствует в пицце");
            }
        }
    }

    @Override
    public List<Ingredient> getIngredients() {
        return pizza.getIngredients();
    }

    @Override
    public Map<Ingredient, Integer> getMultipliers() {
        return multipliers;
    }

    @Override
    public Crust getCrust() {
        return crust;
    }

    public Pizza getPizza() {
        return pizza;
    }
}