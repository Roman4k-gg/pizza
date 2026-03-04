import java.util.List;
import java.util.Map;

public abstract class Half {
    public abstract List<Ingredient> getIngredients();
    public abstract Map<Ingredient, Integer> getMultipliers();
    public abstract Crust getCrust();

    public double getIngredientsCost() {
        double cost = 0;
        for (Ingredient ing : getIngredients()) {
            int multiplier = getMultipliers().getOrDefault(ing, 1);
            cost += ing.getCost() * multiplier;
        }
        return cost;
    }
}