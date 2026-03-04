import java.util.HashMap;
import java.util.Map;

public class CatalogPizzaItem extends OrderItem {
    private Pizza pizza;
    private Map<Ingredient, Integer> extraMultipliers;
    private Size size;
    private Crust crust;

    public CatalogPizzaItem(Pizza pizza, Map<Ingredient, Integer> extraMultipliers, Size size, Crust crust) {
        this.pizza = pizza;
        this.extraMultipliers = extraMultipliers != null ? new HashMap<>(extraMultipliers) : new HashMap<>();
        this.size = size;
        this.crust = crust;
        if (crust != null && !crust.isCompatibleWith(pizza)) {
            throw new IllegalArgumentException("Бортик несовместим с этой пиццей");
        }
        for (Ingredient ing : this.extraMultipliers.keySet()) {
            if (!pizza.getIngredients().contains(ing)) {
                throw new IllegalArgumentException("Ингредиент " + ing.getName() + " отсутствует в пицце");
            }
        }
    }

    @Override
    public double calculateCost() {
        double base = pizza.getBaseCost();
        double extra = extraMultipliers.keySet().stream()
                .mapToDouble(ing -> ing.getCost() * extraMultipliers.get(ing))
                .sum();
        double crustCost = crust != null ? crust.getCost() : 0;
        return (base + extra + crustCost) * size.getMultiplier();
    }

    @Override
    public Size getSize() {
        return size;
    }

    public Pizza getPizza() { return pizza; }
    public Map<Ingredient, Integer> getExtraMultipliers() { return extraMultipliers; }
    public Crust getCrust() { return crust; }
}