public class CompositePizzaItem extends OrderItem {
    private Dough dough;
    private Half half1;
    private Half half2;
    private Size size;
    private Crust commonCrust;
    private boolean useCommonCrust;

    public CompositePizzaItem(Dough dough, Half half1, Half half2, Size size,
                              Crust commonCrust, boolean useCommonCrust) {
        this.dough = dough;
        this.half1 = half1;
        this.half2 = half2;
        this.size = size;
        this.commonCrust = commonCrust;
        this.useCommonCrust = useCommonCrust;
        if (!useCommonCrust) {
            if (half1 instanceof CatalogHalf && !((CatalogHalf) half1).getCrust().isCompatibleWith(((CatalogHalf) half1).getPizza()))
                throw new IllegalArgumentException("Бортик несовместим с первой половинкой пиццы");
            if (half2 instanceof CatalogHalf && !((CatalogHalf) half2).getCrust().isCompatibleWith(((CatalogHalf) half2).getPizza()))
                throw new IllegalArgumentException("Бортик несовместим со второй половинкой пиццы");
        } else {
            if (commonCrust != null) {
                if (half1 instanceof CatalogHalf && !commonCrust.isCompatibleWith(((CatalogHalf) half1).getPizza()))
                    throw new IllegalArgumentException("Общий бортик несовместим с первой половинкой пиццы");
                if (half2 instanceof CatalogHalf && !commonCrust.isCompatibleWith(((CatalogHalf) half2).getPizza()))
                    throw new IllegalArgumentException("Общий бортик несовместим со второй половинкой пиццы");
            }
        }
    }

    @Override
    public double calculateCost() {
        double half1Cost = half1.getIngredientsCost();
        double half2Cost = half2.getIngredientsCost();
        double crustCost = 0;
        if (useCommonCrust) {
            crustCost = commonCrust != null ? commonCrust.getCost() : 0;
        } else {
            crustCost = (half1.getCrust() != null ? half1.getCrust().getCost() : 0) +
                        (half2.getCrust() != null ? half2.getCrust().getCost() : 0);
        }
        return (half1Cost + half2Cost + dough.getCost() + crustCost) * size.getMultiplier();
    }

    @Override
    public Size getSize() {
        return size;
    }

    public Dough getDough() { return dough; }
    public Half getHalf1() { return half1; }
    public Half getHalf2() { return half2; }
    public Crust getCommonCrust() { return commonCrust; }
    public boolean isUseCommonCrust() { return useCommonCrust; }
}