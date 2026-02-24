import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.text.ParseException;

interface Repository<T, ID> {
    boolean add(T entity);
    boolean update(ID id, T entity);
    boolean delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
    int count();
}

interface Identifiable<ID> {
    ID getId();
}

interface Priced {
    double getPrice();
}

interface Named {
    String getName();
}

abstract class BaseEntity implements Identifiable<UUID>, Named, Priced {
    private final UUID id;
    private String name;
    
    public BaseEntity(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
    
    @Override
    public UUID getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name + " [ID: " + id.toString().substring(0, 8) + "]";
    }
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
        return getName() + " - " + price + " руб. [ID: " + getId().toString().substring(0, 8) + "]";
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
    
    public void setClassic(boolean classic) {
        isClassic = classic;
    }
    
    @Override
    public String toString() {
        return getName() + " - " + price + " руб." + 
               (isClassic ? " (классическая)" : "") + 
               " [ID: " + getId().toString().substring(0, 8) + "]";
    }
}

class Pizza implements Identifiable<UUID>, Named {
    private final UUID id;
    private String name;
    private Base base;
    private List<Ingredient> ingredients;
    private final Date createdAt;
    
    public Pizza(String name, Base base) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.base = base;
        this.ingredients = new ArrayList<>();
        this.createdAt = new Date();
    }
    
    @Override
    public UUID getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Base getBase() {
        return base;
    }
    
    public void setBase(Base base) {
        this.base = base;
    }
    
    public List<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }
    
    public void addIngredient(Ingredient ingredient) {
        if (ingredients.stream().noneMatch(ing -> ing.getId().equals(ingredient.getId()))) {
            ingredients.add(ingredient);
        }
    }
    
    public void removeIngredient(UUID ingredientId) {
        ingredients.removeIf(ing -> ing.getId().equals(ingredientId));
    }
    
    public void clearIngredients() {
        ingredients.clear();
    }
    
    public double getTotalPrice() {
        return base.getPrice() + ingredients.stream()
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===========================================\n");
        sb.append("Пицца: '").append(name).append("'\n");
        sb.append("   ID: ").append(id).append("\n");
        sb.append("   Создана: ").append(createdAt).append("\n");
        sb.append("   Основа: ").append(base).append("\n");
        sb.append("   Ингредиенты (").append(ingredients.size()).append("):\n");
        
        if (ingredients.isEmpty()) {
            sb.append("     нет\n");
        } else {
            for (Ingredient ing : ingredients) {
                sb.append("     - ").append(ing.getName())
                  .append(" (").append(ing.getPrice()).append(" руб.)\n");
            }
        }
        
        sb.append("   ИТОГО: ").append(getTotalPrice()).append(" руб.\n");
        sb.append("===========================================\n");
        return sb.toString();
    }
}

class Border implements Identifiable<UUID>, Named {
    private final UUID id;
    private String name;
    private double price;
    private List<Ingredient> ingredients;
    
    public Border(String name, double price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<>();
    }
    
    @Override
    public UUID getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public List<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }
    
    public void addIngredient(Ingredient ingredient) {
        if (ingredients.stream().noneMatch(ing -> ing.getId().equals(ingredient.getId()))) {
            ingredients.add(ingredient);
        }
    }
    
    public void removeIngredient(UUID ingredientId) {
        ingredients.removeIf(ing -> ing.getId().equals(ingredientId));
    }
    
    @Override
    public String toString() {
        return name + " - " + price + " руб. [ID: " + id.toString().substring(0, 8) + "]";
    }
}

class Order implements Identifiable<UUID> {
    private final UUID id;
    private String customerName;
    private String address;
    private String phoneNumber;
    private Date orderDate;
    private Date deliveryDate;
    private double totalPrice;
    private String status;
    private List<Pizza> pizzas;
    private Border border;
    private boolean isDelivered;
    
    public Order(String customerName, String address, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.customerName = customerName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.orderDate = new Date();
        this.deliveryDate = new Date();
        this.totalPrice = 0;
        this.status = "Новый";
        this.pizzas = new ArrayList<>();
        this.isDelivered = false;
    }
    
    @Override
    public UUID getId() {
        return id;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<Pizza> getPizzas() {
        return new ArrayList<>(pizzas);
    }
    
    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
        this.totalPrice += pizza.getTotalPrice();
    }
    
    public void removePizza(UUID pizzaId) {
        pizzas.removeIf(p -> p.getId().equals(pizzaId));
        this.totalPrice = pizzas.stream().mapToDouble(Pizza::getTotalPrice).sum();
    }
    
    public Border getBorder() {
        return border;
    }
    
    public void setBorder(Border border) {
        this.border = border;
        if (border != null) {
            this.totalPrice += border.getPrice();
        }
    }
    
    public boolean isDelivered() {
        return isDelivered;
    }
    
    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
        if (delivered) {
            setStatus("Доставлен");
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===========================================\n");
        sb.append("Заказ № ").append(id.toString().substring(0, 8)).append("\n");
        sb.append("Клиент: ").append(customerName).append("\n");
        sb.append("Адрес: ").append(address).append("\n");
        sb.append("Телефон: ").append(phoneNumber).append("\n");
        sb.append("Дата заказа: ").append(orderDate).append("\n");
        sb.append("Дата доставки: ").append(deliveryDate).append("\n");
        sb.append("Статус: ").append(status).append("\n");
        sb.append("Пиццы (").append(pizzas.size()).append("):\n");
        
        for (Pizza pizza : pizzas) {
            sb.append("   - ").append(pizza.getName()).append(" - ").append(pizza.getTotalPrice()).append(" руб.\n");
        }
        
        if (border != null) {
            sb.append("Борт: ").append(border.getName()).append(" - ").append(border.getPrice()).append(" руб.\n");
        }
        
        sb.append("Общая стоимость: ").append(totalPrice).append(" руб.\n");
        sb.append("Статус доставки: ").append(isDelivered ? "Доставлен" : "Не доставлен").append("\n");
        sb.append("===========================================\n");
        return sb.toString();
    }
}

class IngredientRepository implements Repository<Ingredient, UUID> {
    private final Map<UUID, Ingredient> storage = new HashMap<>();
    
    @Override
    public boolean add(Ingredient entity) {
        if (entity == null || entity.getId() == null) {
            return false;
        }
        storage.put(entity.getId(), entity);
        return true;
    }
    
    @Override
    public boolean update(UUID id, Ingredient entity) {
        if (!storage.containsKey(id)) {
            return false;
        }
        storage.put(id, entity);
        return true;
    }
    
    @Override
    public boolean delete(UUID id) {
        return storage.remove(id) != null;
    }
    
    @Override
    public Optional<Ingredient> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Ingredient> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public int count() {
        return storage.size();
    }
    
    public List<Ingredient> findByPriceRange(double min, double max) {
        return storage.values().stream()
                .filter(ing -> ing.getPrice() >= min && ing.getPrice() <= max)
                .collect(Collectors.toList());
    }
    
    public List<Ingredient> findByNameContaining(String text) {
        return storage.values().stream()
                .filter(ing -> ing.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}

class BaseRepository implements Repository<Base, UUID> {
    private final Map<UUID, Base> storage = new HashMap<>();
    private final double classicBaseMaxPercent = 1.2;
    
    @Override
    public boolean add(Base entity) {
        if (entity == null) {
            return false;
        }
        
        if (!entity.isClassic()) {
            Optional<Base> classicBase = storage.values().stream()
                    .filter(Base::isClassic)
                    .findFirst();
            
            if (classicBase.isPresent()) {
                double maxPrice = classicBase.get().getPrice() * classicBaseMaxPercent;
                if (entity.getPrice() > maxPrice) {
                    System.out.println("Ошибка: стоимость основы не может превышать 20% от классической!");
                    System.out.println("   Максимальная цена: " + maxPrice + " руб.");
                    return false;
                }
            }
        }
        
        storage.put(entity.getId(), entity);
        return true;
    }
    
    @Override
    public boolean update(UUID id, Base entity) {
        if (!storage.containsKey(id)) {
            return false;
        }
        storage.put(id, entity);
        return true;
    }
    
    @Override
    public boolean delete(UUID id) {
        return storage.remove(id) != null;
    }
    
    @Override
    public Optional<Base> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Base> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public int count() {
        return storage.size();
    }
    
    public Optional<Base> findClassicBase() {
        return storage.values().stream()
                .filter(Base::isClassic)
                .findFirst();
    }
}

class PizzaRepository implements Repository<Pizza, UUID> {
    private final Map<UUID, Pizza> storage = new HashMap<>();
    
    @Override
    public boolean add(Pizza entity) {
        if (entity == null || entity.getBase() == null) {
            System.out.println("Ошибка: нельзя создать пиццу без основы!");
            return false;
        }
        storage.put(entity.getId(), entity);
        return true;
    }
    
    @Override
    public boolean update(UUID id, Pizza entity) {
        if (!storage.containsKey(id)) {
            return false;
        }
        storage.put(id, entity);
        return true;
    }
    
    @Override
    public boolean delete(UUID id) {
        return storage.remove(id) != null;
    }
    
    @Override
    public Optional<Pizza> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Pizza> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public int count() {
        return storage.size();
    }
    
    public List<Pizza> findAllSortedByPriceAsc() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Pizza::getTotalPrice))
                .collect(Collectors.toList());
    }
    
    public List<Pizza> findAllSortedByPriceDesc() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Pizza::getTotalPrice).reversed())
                .collect(Collectors.toList());
    }
    
    public List<Pizza> findAllSortedByDate() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Pizza::getCreatedAt))
                .collect(Collectors.toList());
    }
    
    public List<Pizza> findAllSortedByName() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Pizza::getName))
                .collect(Collectors.toList());
    }
    
    public List<Pizza> findByPriceRange(double min, double max) {
        return storage.values().stream()
                .filter(pizza -> pizza.getTotalPrice() >= min && pizza.getTotalPrice() <= max)
                .collect(Collectors.toList());
    }
    
    public List<Pizza> findByNameContaining(String text) {
        return storage.values().stream()
                .filter(pizza -> pizza.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public Optional<Pizza> findMostExpensive() {
        return storage.values().stream()
                .max(Comparator.comparing(Pizza::getTotalPrice));
    }
    
    public Optional<Pizza> findCheapest() {
        return storage.values().stream()
                .min(Comparator.comparing(Pizza::getTotalPrice));
    }
    
    public double calculateTotalRevenue() {
        return storage.values().stream()
                .mapToDouble(Pizza::getTotalPrice)
                .sum();
    }
    
    public Map<String, List<Pizza>> groupByBase() {
        return storage.values().stream()
                .collect(Collectors.groupingBy(pizza -> pizza.getBase().getName()));
    }
}

class BorderRepository implements Repository<Border, UUID> {
    private final Map<UUID, Border> storage = new HashMap<>();
    
    @Override
    public boolean add(Border entity) {
        if (entity == null || entity.getId() == null) {
            return false;
        }
        storage.put(entity.getId(), entity);
        return true;
    }
    
    @Override
    public boolean update(UUID id, Border entity) {
        if (!storage.containsKey(id)) {
            return false;
        }
        storage.put(id, entity);
        return true;
    }
    
    @Override
    public boolean delete(UUID id) {
        return storage.remove(id) != null;
    }
    
    @Override
    public Optional<Border> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Border> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public int count() {
        return storage.size();
    }
    
    public List<Border> findByNameContaining(String text) {
        return storage.values().stream()
                .filter(border -> border.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Border> findByPriceRange(double min, double max) {
        return storage.values().stream()
                .filter(border -> border.getPrice() >= min && border.getPrice() <= max)
                .collect(Collectors.toList());
    }
}

class OrderRepository implements Repository<Order, UUID> {
    private final Map<UUID, Order> storage = new HashMap<>();
    
    @Override
    public boolean add(Order entity) {
        if (entity == null || entity.getId() == null) {
            return false;
        }
        storage.put(entity.getId(), entity);
        return true;
    }
    
    @Override
    public boolean update(UUID id, Order entity) {
        if (!storage.containsKey(id)) {
            return false;
        }
        storage.put(id, entity);
        return true;
    }
    
    @Override
    public boolean delete(UUID id) {
        return storage.remove(id) != null;
    }
    
    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Order> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public int count() {
        return storage.size();
    }
    
    public List<Order> findByCustomerName(String name) {
        return storage.values().stream()
                .filter(order -> order.getCustomerName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Order> findByStatus(String status) {
        return storage.values().stream()
                .filter(order -> order.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    
    public List<Order> findByDateRange(Date startDate, Date endDate) {
        return storage.values().stream()
                .filter(order -> order.getOrderDate().after(startDate) && order.getOrderDate().before(endDate))
                .collect(Collectors.toList());
    }
    
    public double calculateTotalRevenue() {
        return storage.values().stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }
}

class Comparators {
    
    public static Comparator<Ingredient> ingredientByPriceAsc() {
        return Comparator.comparing(Ingredient::getPrice);
    }
    
    public static Comparator<Ingredient> ingredientByPriceDesc() {
        return Comparator.comparing(Ingredient::getPrice).reversed();
    }
    
    public static Comparator<Ingredient> ingredientByName() {
        return Comparator.comparing(Ingredient::getName);
    }
    
    public static Comparator<Base> baseByPriceAsc() {
        return Comparator.comparing(Base::getPrice);
    }
    
    public static Comparator<Base> baseClassicFirst() {
        return (b1, b2) -> {
            if (b1.isClassic() && !b2.isClassic()) return -1;
            if (!b1.isClassic() && b2.isClassic()) return 1;
            return Double.compare(b1.getPrice(), b2.getPrice());
        };
    }
    
    public static Comparator<Pizza> pizzaByTotalPriceAsc() {
        return Comparator.comparing(Pizza::getTotalPrice);
    }
    
    public static Comparator<Pizza> pizzaByTotalPriceDesc() {
        return Comparator.comparing(Pizza::getTotalPrice).reversed();
    }
    
    public static Comparator<Pizza> pizzaByIngredientsCount() {
        return (p1, p2) -> Integer.compare(
                p1.getIngredients().size(),
                p2.getIngredients().size()
        );
    }
    
    public static Comparator<Pizza> pizzaByDate() {
        return Comparator.comparing(Pizza::getCreatedAt);
    }
    
    public static Comparator<Pizza> pizzaByBaseThenPrice() {
        return Comparator.comparing((Pizza p) -> p.getBase().getName())
                .thenComparing(Pizza::getTotalPrice);
    }
}

class PizzaManager {
    private final IngredientRepository ingredientRepo;
    private final BaseRepository baseRepo;
    private final PizzaRepository pizzaRepo;
    private final BorderRepository borderRepo;
    private final OrderRepository orderRepo;
    
    public PizzaManager() {
        this.ingredientRepo = new IngredientRepository();
        this.baseRepo = new BaseRepository();
        this.pizzaRepo = new PizzaRepository();
        this.borderRepo = new BorderRepository();
        this.orderRepo = new OrderRepository();
    }
    
    public IngredientRepository getIngredientRepo() {
        return ingredientRepo;
    }
    
    public BaseRepository getBaseRepo() {
        return baseRepo;
    }
    
    public PizzaRepository getPizzaRepo() {
        return pizzaRepo;
    }
    
    public BorderRepository getBorderRepo() {
        return borderRepo;
    }
    
    public OrderRepository getOrderRepo() {
        return orderRepo;
    }
    
    public void printStatistics() {
        System.out.println("\nСТАТИСТИКА:");
        System.out.println("   Ингредиентов: " + ingredientRepo.count());
        System.out.println("   Основ: " + baseRepo.count());
        System.out.println("   Пицц: " + pizzaRepo.count());
        System.out.println("   Бортов: " + borderRepo.count());
        System.out.println("   Заказов: " + orderRepo.count());
        System.out.println("   Общая выручка: " + orderRepo.calculateTotalRevenue() + " руб.");
    }
}

public class Main {
    private static PizzaManager manager;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        manager = new PizzaManager();
        scanner = new Scanner(System.in, "cp866");
        
        initializeData();
        
        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1: manageIngredients(); break;
                case 2: manageBases(); break;
                case 3: managePizzas(); break;
                case 4: manageBorders(); break;
                case 5: manageOrders(); break;
                case 6: showStatistics(); break;
                case 0:
                    System.out.println("\nДо свидания!");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
        scanner.close();
    }
    
    private static void initializeData() {
        manager.getBaseRepo().add(new Base("Классическая", 100, true));
        manager.getBaseRepo().add(new Base("Чёрная", 115, false));
        manager.getBaseRepo().add(new Base("Толстая", 110, false));
        manager.getBaseRepo().add(new Base("Безглютеновая", 90, false));
        
        manager.getIngredientRepo().add(new Ingredient("Пепперони", 50));
        manager.getIngredientRepo().add(new Ingredient("Моцарелла", 40));
        manager.getIngredientRepo().add(new Ingredient("Грибы", 30));
        manager.getIngredientRepo().add(new Ingredient("Ветчина", 45));
        manager.getIngredientRepo().add(new Ingredient("Томаты", 25));
        manager.getIngredientRepo().add(new Ingredient("Оливки", 35));
        
        manager.getBorderRepo().add(new Border("С кунжутом", 15));
        manager.getBorderRepo().add(new Border("С сыром", 25));
        manager.getBorderRepo().add(new Border("С чесноком", 10));
    }
    
    private static void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        КОНСТРУКТОР ПИЦЦЫ");
        System.out.println("=".repeat(50));
        System.out.println("1. Управление ингредиентами");
        System.out.println("2. Управление основами");
        System.out.println("3. Управление пиццами");
        System.out.println("4. Управление бортами");
        System.out.println("5. Управление заказами");
        System.out.println("6. Статистика");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт меню: ");
    }
    
    private static void manageIngredients() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("        УПРАВЛЕНИЕ ИНГРЕДИЕНТАМИ");
            System.out.println("-".repeat(50));
            System.out.println("1. Добавить ингредиент");
            System.out.println("2. Редактировать ингредиент");
            System.out.println("3. Удалить ингредиент");
            System.out.println("4. Показать все ингредиенты");
            System.out.println("5. Сортировать по цене (возрастание)");
            System.out.println("6. Сортировать по цене (убывание)");
            System.out.println("7. Найти по названию");
            System.out.println("8. Найти по диапазону цен");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    System.out.print("Название: ");
                    String name = scanner.nextLine();
                    System.out.print("Цена: ");
                    double price = getDoubleInput();
                    manager.getIngredientRepo().add(new Ingredient(name, price));
                    System.out.println("Ингредиент добавлен!");
                    break;
                    
                case 2:
                    showIngredients();
                    System.out.print("Введите номер ингредиента: ");
                    int editNum = getIntInput();
                    List<Ingredient> ingredientsEdit = manager.getIngredientRepo().findAll();
                    if (editNum > 0 && editNum <= ingredientsEdit.size()) {
                        Ingredient toEdit = ingredientsEdit.get(editNum - 1);
                        System.out.print("Новое название: ");
                        String newName = scanner.nextLine();
                        System.out.print("Новая цена: ");
                        double newPrice = getDoubleInput();
                        Ingredient updated = new Ingredient(newName, newPrice);
                        manager.getIngredientRepo().update(toEdit.getId(), updated);
                        System.out.println("Ингредиент обновлён!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 3:
                    showIngredients();
                    System.out.print("Введите номер ингредиента для удаления: ");
                    int delNum = getIntInput();
                    List<Ingredient> ingredientsDel = manager.getIngredientRepo().findAll();
                    if (delNum > 0 && delNum <= ingredientsDel.size()) {
                        Ingredient toDelete = ingredientsDel.get(delNum - 1);
                        if (manager.getIngredientRepo().delete(toDelete.getId())) {
                            System.out.println("Ингредиент удалён!");
                        } else {
                            System.out.println("Не найдено");
                        }
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 4:
                    showIngredients();
                    break;
                    
                case 5:
                    List<Ingredient> sortedAsc = manager.getIngredientRepo().findAll()
                            .stream()
                            .sorted(Comparators.ingredientByPriceAsc())
                            .collect(Collectors.toList());
                    printIngredientList(sortedAsc);
                    break;
                    
                case 6:
                    List<Ingredient> sortedDesc = manager.getIngredientRepo().findAll()
                            .stream()
                            .sorted(Comparators.ingredientByPriceDesc())
                            .collect(Collectors.toList());
                    printIngredientList(sortedDesc);
                    break;
                    
                case 7:
                    System.out.print("Введите текст для поиска: ");
                    String searchText = scanner.nextLine();
                    List<Ingredient> found = manager.getIngredientRepo().findByNameContaining(searchText);
                    if (found.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        printIngredientList(found);
                    }
                    break;
                    
                case 8:
                    System.out.print("Минимальная цена: ");
                    double min = getDoubleInput();
                    System.out.print("Максимальная цена: ");
                    double max = getDoubleInput();
                    List<Ingredient> byPrice = manager.getIngredientRepo().findByPriceRange(min, max);
                    if (byPrice.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        printIngredientList(byPrice);
                    }
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
            System.out.println("\n" + "-".repeat(50));
            System.out.println("        УПРАВЛЕНИЕ ОСНОВАМИ");
            System.out.println("-".repeat(50));
            System.out.println("1. Добавить основу");
            System.out.println("2. Редактировать основу");
            System.out.println("3. Удалить основу");
            System.out.println("4. Показать все основы");
            System.out.println("5. Сортировать: классические сначала");
            System.out.println("6. Сортировать по цене");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    System.out.print("Название: ");
                    String name = scanner.nextLine();
                    System.out.print("Цена: ");
                    double price = getDoubleInput();
                    System.out.print("Классическая? (да/нет): ");
                    String isClassicStr = scanner.nextLine();
                    boolean isClassic = isClassicStr.equalsIgnoreCase("да") || 
                                      isClassicStr.equalsIgnoreCase("yes");
                    
                    if (manager.getBaseRepo().add(new Base(name, price, isClassic))) {
                        System.out.println("Основа добавлена!");
                    }
                    break;
                    
                case 2:
                    showBases();
                    System.out.print("Введите номер основы: ");
                    int editNum = getIntInput();
                    List<Base> basesEdit = manager.getBaseRepo().findAll();
                    if (editNum > 0 && editNum <= basesEdit.size()) {
                        Base toEdit = basesEdit.get(editNum - 1);
                        System.out.print("Новое название: ");
                        String newName = scanner.nextLine();
                        System.out.print("Новая цена: ");
                        double newPrice = getDoubleInput();
                        System.out.print("Классическая? (да/нет): ");
                        String newIsClassic = scanner.nextLine();
                        boolean isClassicNew = newIsClassic.equalsIgnoreCase("да") || 
                                             newIsClassic.equalsIgnoreCase("yes");
                        Base updated = new Base(newName, newPrice, isClassicNew);
                        manager.getBaseRepo().update(toEdit.getId(), updated);
                        System.out.println("Основа обновлена!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 3:
                    showBases();
                    System.out.print("Введите номер основы для удаления: ");
                    int delNum = getIntInput();
                    List<Base> basesDel = manager.getBaseRepo().findAll();
                    if (delNum > 0 && delNum <= basesDel.size()) {
                        Base toDelete = basesDel.get(delNum - 1);
                        if (manager.getBaseRepo().delete(toDelete.getId())) {
                            System.out.println("Основа удалена!");
                        } else {
                            System.out.println("Не найдено");
                        }
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 4:
                    showBases();
                    break;
                    
                case 5:
                    List<Base> classicFirst = manager.getBaseRepo().findAll()
                            .stream()
                            .sorted(Comparators.baseClassicFirst())
                            .collect(Collectors.toList());
                    printBaseList(classicFirst);
                    break;
                    
                case 6:
                    List<Base> byPrice = manager.getBaseRepo().findAll()
                            .stream()
                            .sorted(Comparators.baseByPriceAsc())
                            .collect(Collectors.toList());
                    printBaseList(byPrice);
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
            System.out.println("\n" + "-".repeat(50));
            System.out.println("        УПРАВЛЕНИЕ ПИЦЦАМИ");
            System.out.println("-".repeat(50));
            System.out.println("1. Создать пиццу");
            System.out.println("2. Редактировать пиццу");
            System.out.println("3. Удалить пиццу");
            System.out.println("4. Показать все пиццы");
            System.out.println("5. Сортировать по цене (возрастание)");
            System.out.println("6. Сортировать по цене (убывание)");
            System.out.println("7. Сортировать по количеству ингредиентов");
            System.out.println("8. Найти самую дорогую");
            System.out.println("9. Найти самую дешёвую");
            System.out.println("10. Поиск по названию");
            System.out.println("11. Группировка по основе");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    createPizza();
                    break;
                    
                case 2:
                    showPizzas();
                    System.out.print("Введите номер пиццы для редактирования: ");
                    int editNum = getIntInput();
                    List<Pizza> pizzasEdit = manager.getPizzaRepo().findAll();
                    if (editNum > 0 && editNum <= pizzasEdit.size()) {
                        createPizza();
                        Pizza toEdit = pizzasEdit.get(editNum - 1);
                        manager.getPizzaRepo().delete(toEdit.getId());
                        System.out.println("Пицца обновлена!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 3:
                    showPizzas();
                    System.out.print("Введите номер пиццы для удаления: ");
                    int delNum = getIntInput();
                    List<Pizza> pizzasDel = manager.getPizzaRepo().findAll();
                    if (delNum > 0 && delNum <= pizzasDel.size()) {
                        Pizza toDelete = pizzasDel.get(delNum - 1);
                        if (manager.getPizzaRepo().delete(toDelete.getId())) {
                            System.out.println("Пицца удалена!");
                        } else {
                            System.out.println("Не найдено");
                        }
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 4:
                    showPizzas();
                    break;
                    
                case 5:
                    List<Pizza> sorted = manager.getPizzaRepo().findAllSortedByPriceAsc();
                    printPizzaList(sorted);
                    break;
                    
                case 6:
                    List<Pizza> sortedDesc = manager.getPizzaRepo().findAllSortedByPriceDesc();
                    printPizzaList(sortedDesc);
                    break;
                    
                case 7:
                    List<Pizza> byIngredients = manager.getPizzaRepo().findAll()
                            .stream()
                            .sorted(Comparators.pizzaByIngredientsCount())
                            .collect(Collectors.toList());
                    printPizzaList(byIngredients);
                    break;
                    
                case 8:
                    manager.getPizzaRepo().findMostExpensive()
                            .ifPresentOrElse(
                                    p -> System.out.println("\nСамая дорогая:\n" + p),
                                    () -> System.out.println("Нет пицц")
                            );
                    break;
                    
                case 9:
                    manager.getPizzaRepo().findCheapest()
                            .ifPresentOrElse(
                                    p -> System.out.println("\nСамая дешёвая:\n" + p),
                                    () -> System.out.println("Нет пицц")
                            );
                    break;
                    
                case 10:
                    System.out.print("Введите название: ");
                    String name = scanner.nextLine();
                    List<Pizza> found = manager.getPizzaRepo().findByNameContaining(name);
                    if (found.isEmpty()) {
                        System.out.println("Не найдено");
                    } else {
                        printPizzaList(found);
                    }
                    break;
                    
                case 11:
                    Map<String, List<Pizza>> grouped = manager.getPizzaRepo().groupByBase();
                    grouped.forEach((baseName, pizzas) -> {
                        System.out.println("\n " + baseName + ":");
                        pizzas.forEach(p -> 
                            System.out.println("   - " + p.getName() + " - " + p.getTotalPrice() + " руб.")
                        );
                    });
                    break;
                    
                case 0:
                    back = true;
                    break;
                    
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }
    
    private static void manageBorders() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("        УПРАВЛЕНИЕ БОРТАМИ");
            System.out.println("-".repeat(50));
            System.out.println("1. Добавить борт");
            System.out.println("2. Редактировать борт");
            System.out.println("3. Удалить борт");
            System.out.println("4. Показать все борты");
            System.out.println("5. Найти по названию");
            System.out.println("6. Найти по диапазону цен");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    System.out.print("Название: ");
                    String name = scanner.nextLine();
                    System.out.print("Цена: ");
                    double price = getDoubleInput();
                    Border border = new Border(name, price);
                    manager.getBorderRepo().add(border);
                    System.out.println("Борт добавлен!");
                    break;
                    
                case 2:
                    showBorders();
                    System.out.print("Введите номер борта: ");
                    int editNum = getIntInput();
                    List<Border> bordersEdit = manager.getBorderRepo().findAll();
                    if (editNum > 0 && editNum <= bordersEdit.size()) {
                        Border toEdit = bordersEdit.get(editNum - 1);
                        System.out.print("Новое название: ");
                        String newName = scanner.nextLine();
                        System.out.print("Новая цена: ");
                        double newPrice = getDoubleInput();
                        Border updated = new Border(newName, newPrice);
                        manager.getBorderRepo().update(toEdit.getId(), updated);
                        System.out.println("Борт обновлён!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 3:
                    showBorders();
                    System.out.print("Введите номер борта для удаления: ");
                    int delNum = getIntInput();
                    List<Border> bordersDel = manager.getBorderRepo().findAll();
                    if (delNum > 0 && delNum <= bordersDel.size()) {
                        Border toDelete = bordersDel.get(delNum - 1);
                        if (manager.getBorderRepo().delete(toDelete.getId())) {
                            System.out.println("Борт удалён!");
                        } else {
                            System.out.println("Не найдено");
                        }
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 4:
                    showBorders();
                    break;
                    
                case 5:
                    System.out.print("Введите текст для поиска: ");
                    String searchText = scanner.nextLine();
                    List<Border> found = manager.getBorderRepo().findByNameContaining(searchText);
                    if (found.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        printBorderList(found);
                    }
                    break;
                    
                case 6:
                    System.out.print("Минимальная цена: ");
                    double min = getDoubleInput();
                    System.out.print("Максимальная цена: ");
                    double max = getDoubleInput();
                    List<Border> byPrice = manager.getBorderRepo().findByPriceRange(min, max);
                    if (byPrice.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        printBorderList(byPrice);
                    }
                    break;
                    
                case 0:
                    back = true;
                    break;
                    
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }
    
    private static void manageOrders() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("        УПРАВЛЕНИЕ ЗАКАЗАМИ");
            System.out.println("-".repeat(50));
            System.out.println("1. Создать заказ");
            System.out.println("2. Редактировать заказ");
            System.out.println("3. Удалить заказ");
            System.out.println("4. Показать все заказы");
            System.out.println("5. Найти по клиенту");
            System.out.println("6. Найти по статусу");
            System.out.println("7. Найти по дате");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    createOrder();
                    break;
                    
                case 2:
                    showOrders();
                    System.out.print("Введите номер заказа для редактирования: ");
                    int editNum = getIntInput();
                    List<Order> ordersEdit = manager.getOrderRepo().findAll();
                    if (editNum > 0 && editNum <= ordersEdit.size()) {
                        Order toEdit = ordersEdit.get(editNum - 1);
                        createOrder();
                        manager.getOrderRepo().delete(toEdit.getId());
                        System.out.println("Заказ обновлён!");
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 3:
                    showOrders();
                    System.out.print("Введите номер заказа для удаления: ");
                    int delNum = getIntInput();
                    List<Order> ordersDel = manager.getOrderRepo().findAll();
                    if (delNum > 0 && delNum <= ordersDel.size()) {
                        Order toDelete = ordersDel.get(delNum - 1);
                        if (manager.getOrderRepo().delete(toDelete.getId())) {
                            System.out.println("Заказ удалён!");
                        } else {
                            System.out.println("Не найдено");
                        }
                    } else {
                        System.out.println("Неверный номер");
                    }
                    break;
                    
                case 4:
                    showOrders();
                    break;
                    
                case 5:
                    System.out.print("Введите имя клиента: ");
                    String customerName = scanner.nextLine();
                    List<Order> found = manager.getOrderRepo().findByCustomerName(customerName);
                    if (found.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        printOrderList(found);
                    }
                    break;
                    
                case 6:
                    System.out.print("Введите статус: ");
                    String status = scanner.nextLine();
                    List<Order> byStatus = manager.getOrderRepo().findByStatus(status);
                    if (byStatus.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        printOrderList(byStatus);
                    }
                    break;
                    
                case 7:
                    System.out.println("Введите начальную дату (в формате ГГГГ-ММ-ДД): ");
                    String startDateStr = scanner.nextLine();
                    System.out.println("Введите конечную дату (в формате ГГГГ-ММ-ДД): ");
                    String endDateStr = scanner.nextLine();
                    
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = sdf.parse(startDateStr);
                        Date endDate = sdf.parse(endDateStr);
                        
                        List<Order> byDate = manager.getOrderRepo().findByDateRange(startDate, endDate);
                        if (byDate.isEmpty()) {
                            System.out.println("Ничего не найдено");
                        } else {
                            printOrderList(byDate);
                        }
                    } catch (ParseException e) {
                        System.out.println("Неверный формат даты");
                    }
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
        System.out.println("\nСОЗДАНИЕ ПИЦЦЫ");
        
        System.out.print("Название: ");
        String pizzaName = scanner.nextLine();
        
        System.out.println("\nВыберите основу:");
        showBases();
        System.out.print("Введите номер основы: ");
        int baseNum = getIntInput();
        
        List<Base> bases = manager.getBaseRepo().findAll();
        if (baseNum <= 0 || baseNum > bases.size()) {
            System.out.println("Неверный номер основы");
            return;
        }
        
        Base selectedBase = bases.get(baseNum - 1);
        
        Pizza pizza = new Pizza(pizzaName, selectedBase);
        
        boolean adding = true;
        while (adding) {
            System.out.println("\nВыберите ингредиент:");
            showIngredients();
            System.out.println("0. Завершить выбор");
            
            System.out.print("Введите номер ингредиента: ");
            int ingNum = getIntInput();
            
            if (ingNum == 0) {
                adding = false;
            } else {
                List<Ingredient> ingredients = manager.getIngredientRepo().findAll();
                if (ingNum > 0 && ingNum <= ingredients.size()) {
                    Ingredient ing = ingredients.get(ingNum - 1);
                    pizza.addIngredient(ing);
                    System.out.println("Добавлено: " + ing.getName());
                } else {
                    System.out.println("Неверный номер");
                }
            }
        }
        
        if (manager.getPizzaRepo().add(pizza)) {
            System.out.println("\nПицца создана!");
            System.out.println(pizza);
        }
    }
    
    private static void createOrder() {
        System.out.println("\nСОЗДАНИЕ ЗАКАЗА");
        
        System.out.print("Имя клиента: ");
        String customerName = scanner.nextLine();
        System.out.print("Адрес: ");
        String address = scanner.nextLine();
        System.out.print("Телефон: ");
        String phoneNumber = scanner.nextLine();
        
        Order order = new Order(customerName, address, phoneNumber);
        
        boolean addingPizzas = true;
        while (addingPizzas) {
            System.out.println("\nДобавьте пиццу в заказ:");
            showPizzas();
            System.out.println("0. Завершить добавление пицц");
            
            System.out.print("Введите номер пиццы: ");
            int pizzaNum = getIntInput();
            
            if (pizzaNum == 0) {
                addingPizzas = false;
            } else {
                List<Pizza> pizzas = manager.getPizzaRepo().findAll();
                if (pizzaNum > 0 && pizzaNum <= pizzas.size()) {
                    Pizza pizza = pizzas.get(pizzaNum - 1);
                    order.addPizza(pizza);
                    System.out.println("Добавлено: " + pizza.getName());
                } else {
                    System.out.println("Неверный номер");
                }
            }
        }
        
        System.out.println("\nВыберите борт (если не нужен, введите 0):");
        showBorders();
        System.out.print("Введите номер борта: ");
        int borderNum = getIntInput();
        
        if (borderNum > 0) {
            List<Border> borders = manager.getBorderRepo().findAll();
            if (borderNum <= borders.size()) {
                Border border = borders.get(borderNum - 1);
                order.setBorder(border);
                System.out.println("Борт добавлен: " + border.getName());
            } else {
                System.out.println("Неверный номер");
            }
        }
        
        if (manager.getOrderRepo().add(order)) {
            System.out.println("\nЗаказ создан!");
            System.out.println(order);
        }
    }
    
    private static void showStatistics() {
        manager.printStatistics();
        
        System.out.println("\nАНАЛИТИКА:");
        
        long count = manager.getPizzaRepo().count();
        if (count > 0) {
            double avg = manager.getPizzaRepo().calculateTotalRevenue() / count;
            System.out.println("   Средняя стоимость пиццы: " + String.format("%.2f", avg) + " руб.");
        }
        
        Map<String, Long> baseCount = manager.getPizzaRepo().findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getBase().getName(),
                        Collectors.counting()
                ));
        
        if (!baseCount.isEmpty()) {
            String popularBase = baseCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Нет данных");
            System.out.println("   Самая популярная основа: " + popularBase);
        }
    }
    
    private static void showIngredients() {
        List<Ingredient> ingredients = manager.getIngredientRepo().findAll();
        printIngredientList(ingredients);
    }
    
    private static void printIngredientList(List<Ingredient> list) {
        if (list.isEmpty()) {
            System.out.println("   Пусто");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Ingredient ing = list.get(i);
            System.out.println((i+1) + ". " + ing);
        }
    }
    
    private static void showBases() {
        List<Base> bases = manager.getBaseRepo().findAll();
        if (bases.isEmpty()) {
            System.out.println("   Пусто");
            return;
        }
        for (int i = 0; i < bases.size(); i++) {
            Base base = bases.get(i);
            System.out.println((i+1) + ". " + base);
        }
    }
    
    private static void printBaseList(List<Base> list) {
        if (list.isEmpty()) {
            System.out.println("   Пусто");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Base base = list.get(i);
            System.out.println((i+1) + ". " + base);
        }
    }
    
    private static void showPizzas() {
        List<Pizza> pizzas = manager.getPizzaRepo().findAll();
        printPizzaList(pizzas);
    }
    
    private static void printPizzaList(List<Pizza> list) {
        if (list.isEmpty()) {
            System.out.println("   Нет пицц");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Pizza pizza = list.get(i);
            System.out.println((i+1) + ". " + pizza.getName() + 
                             " - " + pizza.getTotalPrice() + " руб." +
                             " [ID: " + pizza.getId().toString().substring(0, 8) + "]");
        }
    }
    
    private static void showBorders() {
        List<Border> borders = manager.getBorderRepo().findAll();
        printBorderList(borders);
    }
    
    private static void printBorderList(List<Border> list) {
        if (list.isEmpty()) {
            System.out.println("   Пусто");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Border border = list.get(i);
            System.out.println((i+1) + ". " + border);
        }
    }
    
    private static void showOrders() {
        List<Order> orders = manager.getOrderRepo().findAll();
        printOrderList(orders);
    }
    
    private static void printOrderList(List<Order> list) {
        if (list.isEmpty()) {
            System.out.println("   Нет заказов");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            System.out.println((i+1) + ". Заказ № " + order.getId().toString().substring(0, 8) + 
                             " - " + order.getTotalPrice() + " руб." +
                             " [Статус: " + order.getStatus() + "]");
        }
    }
    
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("   Введите число: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    
    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("   Введите число: ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}