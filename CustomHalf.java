import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomHalf extends Half {
    private List<Ingredient> ingredients;
    private Map<Ingredient, Integer> multipliers;
    private Crust crust;

    public CustomHalf(List<Ingredient> ingredients, Map<Ingredient, Integer> multipliers, Crust crust) {
        this.ingredients = ingredients;
        this.multipliers = multipliers != null ? new HashMap<>(multipliers) : new HashMap<>();
        this.crust = crust;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public Map<Ingredient, Integer> getMultipliers() {
        return multipliers;
    }

    @Override
    public Crust getCrust() {
        return crust;
    }
}