import java.util.List;
import java.util.Map;

public class CustomPizzaItem extends OrderItem {
    private String name;
    private Dough dough;
    private List<Ingredient> ingredients;
    private Map<Ingredient, Integer> multipliers;
    private Size size;
    private Crust crust;

    public CustomPizzaItem(String name, Dough dough, List<Ingredient> ingredients,
                           Map<Ingredient, Integer> multipliers, Size size, Crust crust) {
        this.name = name;
        this.dough = dough;
        this.ingredients = ingredients;
        this.multipliers = multipliers != null ? multipliers : Map.of();
        this.size = size;
        this.crust = crust;
    }

    @Override
    public double calculateCost() {
        double ingredientsCost = ingredients.stream()
                .mapToDouble(ing -> ing.getCost() * multipliers.getOrDefault(ing, 1))
                .sum();
        double crustCost = crust != null ? crust.getCost() : 0;
        return (ingredientsCost + dough.getCost() + crustCost) * size.getMultiplier();
    }

    @Override
    public Size getSize() {
        return size;
    }

    public String getName() { return name; }
    public Dough getDough() { return dough; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public Map<Ingredient, Integer> getMultipliers() { return multipliers; }
    public Crust getCrust() { return crust; }
}