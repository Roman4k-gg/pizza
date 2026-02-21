import java.util.*;

abstract class BaseEntity {
    protected String name;
    
    public BaseEntity(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract double getPrice();
    
    public abstract String toString();
}

class Ingredient extends BaseEntity {
    private double price;
    
    public Ingredient(String name, double price) {
        super(name);
        this.price = price;
    }
    
    @Override
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return name + " - " + price + " руб.";
    }
}

class Base extends BaseEntity {
    private double price;
    private boolean isClassic;
    
    public Base(String name, double price, boolean isClassic) {
        super(name);
        this.price = price;
        this.isClassic = isClassic;
    }
    
    @Override
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public boolean isClassic() {
        return isClassic;
    }
    
    @Override
    public String toString() {
        return name + " - " + price + " руб." + (isClassic ? " (классическая)" : "");
    }
}

class Pizza {
    private String name;
    private Base base;
    private List<Ingredient> ingredients;
    
    public Pizza(String name, Base base) {
        this.name = name;
        this.base = base;
        this.ingredients = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public Base getBase() {
        return base;
    }
    
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }
    
    public void removeIngredient(int index) {
        if (index >= 0 && index < ingredients.size()) {
            ingredients.remove(index);
        }
    }
    
    public double getTotalPrice() {
        double total = base.getPrice();
        for (Ingredient ingredient : ingredients) {
            total += ingredient.getPrice();
        }
        return total;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Пицца '").append(name).append("'\n");
        sb.append("  Основа: ").append(base.getName()).append(" (").append(base.getPrice()).append(" руб.)\n");
        sb.append("  Ингредиенты:\n");
        if (ingredients.isEmpty()) {
            sb.append("    нет\n");
        } else {
            for (Ingredient ing : ingredients) {
                sb.append("    - ").append(ing.getName()).append(" (").append(ing.getPrice()).append(" руб.)\n");
            }
        }
        sb.append("  Итого: ").append(getTotalPrice()).append(" руб.");
        return sb.toString();
    }
}

class PizzaManager {
    private List<Pizza> pizzas;
    private List<Ingredient> ingredients;
    private List<Base> bases;
    
    public PizzaManager() {
        this.pizzas = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.bases = new ArrayList<>();
    }
    
    public boolean addIngredient(Ingredient ingredient) {
        return ingredients.add(ingredient);
    }
    
    public boolean updateIngredient(int index, Ingredient newIngredient) {
        if (index >= 0 && index < ingredients.size()) {
            ingredients.set(index, newIngredient);
            return true;
        }
        return false;
    }
    
    public boolean deleteIngredient(int index) {
        if (index >= 0 && index < ingredients.size()) {
            ingredients.remove(index);
            return true;
        }
        return false;
    }
    
    public boolean addBase(Base base) {
        if (!base.isClassic()) {
            Double classicPrice = getClassicBasePrice();
            if (classicPrice != null) {
                double maxPrice = classicPrice * 1.2;
                if (base.getPrice() > maxPrice) {
                    System.out.println("Ошибка: стоимость основы не может превышать 20% от классической!");
                    return false;
                }
            }
        }
        return bases.add(base);
    }
    
    public boolean updateBase(int index, Base newBase) {
        if (index >= 0 && index < bases.size()) {
            return addBase(newBase);
        }
        return false;
    }
    
    public boolean deleteBase(int index) {
        if (index >= 0 && index < bases.size()) {
            bases.remove(index);
            return true;
        }
        return false;
    }
    
    public boolean addPizza(Pizza pizza) {
        return pizzas.add(pizza);
    }
    
    public boolean updatePizza(int index, Pizza newPizza) {
        if (index >= 0 && index < pizzas.size()) {
            pizzas.set(index, newPizza);
            return true;
        }
        return false;
    }
    
    public boolean deletePizza(int index) {
        if (index >= 0 && index < pizzas.size()) {
            pizzas.remove(index);
            return true;
        }
        return false;
    }
    
    private Double getClassicBasePrice() {
        for (Base base : bases) {
            if (base.isClassic()) {
                return base.getPrice();
            }
        }
        return null;
    }
    
    // Отображение списков
    public void displayIngredients() {
        System.out.println("\n=== ИНГРЕДИЕНТЫ ===");
        if (ingredients.isEmpty()) {
            System.out.println("Нет ингредиентов");
        } else {
            for (int i = 0; i < ingredients.size(); i++) {
                System.out.println((i + 1) + ". " + ingredients.get(i));
            }
        }
    }
    
    public void displayBases() {
        System.out.println("\n=== ОСНОВЫ ДЛЯ ПИЦЦЫ ===");
        if (bases.isEmpty()) {
            System.out.println("Нет основ");
        } else {
            for (int i = 0; i < bases.size(); i++) {
                System.out.println((i + 1) + ". " + bases.get(i));
            }
        }
    }
    
    public void displayPizzas() {
        System.out.println("\n=== ПИЦЦЫ ===");
        if (pizzas.isEmpty()) {
            System.out.println("Нет созданных пицц");
        } else {
            for (int i = 0; i < pizzas.size(); i++) {
                System.out.println((i + 1) + ". " + pizzas.get(i).getName() + 
                                 " - " + pizzas.get(i).getTotalPrice() + " руб.");
            }
        }
    }
    
    public void displayPizzaDetails(int index) {
        if (index >= 0 && index < pizzas.size()) {
            System.out.println("\n" + pizzas.get(index));
        }
    }
    
    public List<Ingredient> getIngredients() { return ingredients; }
    public List<Base> getBases() { return bases; }
    public List<Pizza> getPizzas() { return pizzas; }
}

public class Main {
    private static PizzaManager manager = new PizzaManager();
    private static Scanner scanner = new Scanner(System.in, "cp866");
    
    public static void main(String[] args) {
        initializeData();
        
        boolean running = true;
        
        while (running) {
            showMainMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    manageIngredients();
                    break;
                case 2:
                    manageBases();
                    break;
                case 3:
                    managePizzas();
                    break;
                case 4:
                    manager.displayIngredients();
                    break;
                case 5:
                    manager.displayBases();
                    break;
                case 6:
                    manager.displayPizzas();
                    break;
                case 0:
                    running = false;
                    System.out.println("До свидания!");
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
        scanner.close();
    }
    
    private static void initializeData() {
        manager.addBase(new Base("Классическая", 100, true));
        manager.addBase(new Base("Чёрная", 115, false));
        manager.addBase(new Base("Толстая", 110, false));
        
        manager.addIngredient(new Ingredient("Пепперони", 50));
        manager.addIngredient(new Ingredient("Моцарелла", 40));
        manager.addIngredient(new Ingredient("Грибы", 30));
        manager.addIngredient(new Ingredient("Ветчина", 45));
    }
    
    private static void showMainMenu() {
        System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
        System.out.println("1. Управление ингредиентами");
        System.out.println("2. Управление основами");
        System.out.println("3. Управление пиццами");
        System.out.println("4. Показать все ингредиенты");
        System.out.println("5. Показать все основы");
        System.out.println("6. Показать все пиццы");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт меню: ");
    }
    
    private static void manageIngredients() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== УПРАВЛЕНИЕ ИНГРЕДИЕНТАМИ ===");
            System.out.println("1. Добавить ингредиент");
            System.out.println("2. Редактировать ингредиент");
            System.out.println("3. Удалить ингредиент");
            System.out.println("4. Показать все ингредиенты");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    System.out.print("Название ингредиента: ");
                    String name = scanner.nextLine();
                    System.out.print("Стоимость: ");
                    double price = getDoubleInput();
                    manager.addIngredient(new Ingredient(name, price));
                    System.out.println("Ингредиент добавлен!");
                    break;
                case 2:
                    manager.displayIngredients();
                    System.out.print("Номер ингредиента для редактирования: ");
                    int editIndex = getIntInput() - 1;
                    if (editIndex >= 0 && editIndex < manager.getIngredients().size()) {
                        System.out.print("Новое название: ");
                        String newName = scanner.nextLine();
                        System.out.print("Новая стоимость: ");
                        double newPrice = getDoubleInput();
                        manager.updateIngredient(editIndex, new Ingredient(newName, newPrice));
                        System.out.println("Ингредиент обновлен!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                case 3:
                    manager.displayIngredients();
                    System.out.print("Номер ингредиента для удаления: ");
                    int delIndex = getIntInput() - 1;
                    if (manager.deleteIngredient(delIndex)) {
                        System.out.println("Ингредиент удален!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                case 4:
                    manager.displayIngredients();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }
    
    private static void manageBases() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== УПРАВЛЕНИЕ ОСНОВАМИ ===");
            System.out.println("1. Добавить основу");
            System.out.println("2. Редактировать основу");
            System.out.println("3. Удалить основу");
            System.out.println("4. Показать все основы");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    System.out.print("Название основы: ");
                    String name = scanner.nextLine();
                    System.out.print("Стоимость: ");
                    double price = getDoubleInput();
                    System.out.print("Это классическая основа? (да/нет): ");
                    String isClassicStr = scanner.nextLine();
                    boolean isClassic = isClassicStr.equalsIgnoreCase("да") || 
                                      isClassicStr.equalsIgnoreCase("yes");
                    
                    Base newBase = new Base(name, price, isClassic);
                    if (manager.addBase(newBase)) {
                        System.out.println("Основа добавлена!");
                    }
                    break;
                case 2:
                    manager.displayBases();
                    System.out.print("Номер основы для редактирования: ");
                    int editIndex = getIntInput() - 1;
                    if (editIndex >= 0 && editIndex < manager.getBases().size()) {
                        System.out.print("Новое название: ");
                        String newName = scanner.nextLine();
                        System.out.print("Новая стоимость: ");
                        double newPrice = getDoubleInput();
                        System.out.print("Классическая основа? (да/нет): ");
                        String newIsClassicStr = scanner.nextLine();
                        boolean newIsClassic = newIsClassicStr.equalsIgnoreCase("да") || 
                                            newIsClassicStr.equalsIgnoreCase("yes");
                        manager.updateBase(editIndex, new Base(newName, newPrice, newIsClassic));
                        System.out.println("Основа обновлена!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                case 3:
                    manager.displayBases();
                    System.out.print("Номер основы для удаления: ");
                    int delIndex = getIntInput() - 1;
                    if (manager.deleteBase(delIndex)) {
                        System.out.println("Основа удалена!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                case 4:
                    manager.displayBases();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }
    
    private static void managePizzas() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== УПРАВЛЕНИЕ ПИЦЦАМИ ===");
            System.out.println("1. Создать пиццу");
            System.out.println("2. Редактировать пиццу");
            System.out.println("3. Удалить пиццу");
            System.out.println("4. Показать все пиццы");
            System.out.println("5. Показать пиццы");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    createPizza();
                    break;
                case 2:
                    manager.displayPizzas();
                    System.out.print("Номер пиццы для редактирования: ");
                    int editIndex = getIntInput() - 1;
                    if (editIndex >= 0 && editIndex < manager.getPizzas().size()) {
                        createPizza();
                        manager.deletePizza(editIndex);
                        System.out.println("Пицца обновлена!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                case 3:
                    manager.displayPizzas();
                    System.out.print("Номер пиццы для удаления: ");
                    int delIndex = getIntInput() - 1;
                    if (manager.deletePizza(delIndex)) {
                        System.out.println("Пицца удалена!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                case 4:
                    manager.displayPizzas();
                    break;
                case 5:
                    manager.displayPizzas();
                    System.out.print("Номер пиццы для просмотра: ");
                    int viewIndex = getIntInput() - 1;
                    manager.displayPizzaDetails(viewIndex);
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }
    
    private static void createPizza() {
        System.out.print("Название пиццы: ");
        String pizzaName = scanner.nextLine();
        
        manager.displayBases();
        System.out.print("Выберите основу (номер): ");
        int baseChoice = getIntInput() - 1;
        
        if (baseChoice < 0 || baseChoice >= manager.getBases().size()) {
            System.out.println("Неверный выбор основы!");
            return;
        }
        
        Base selectedBase = manager.getBases().get(baseChoice);
        Pizza pizza = new Pizza(pizzaName, selectedBase);
        
        boolean addingIngredients = true;
        while (addingIngredients) {
            manager.displayIngredients();
            System.out.println("0. Завершить выбор ингредиентов");
            System.out.print("Выберите ингредиент (номер): ");
            int ingredientChoice = getIntInput() - 1;
            
            if (ingredientChoice == -1) {
                addingIngredients = false;
            } else if (ingredientChoice >= 0 && ingredientChoice < manager.getIngredients().size()) {
                pizza.addIngredient(manager.getIngredients().get(ingredientChoice));
                System.out.println("Ингредиент добавлен!");
            } else {
                System.out.println("Неверный выбор");
            }
        }
        
        manager.addPizza(pizza);
        System.out.println("\nПицца создана!");
        System.out.println(pizza);
    }
    
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число!");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    
    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.println("Пожалуйста, введите число!");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}