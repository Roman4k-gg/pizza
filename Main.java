import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static IngredientService ingredientService = new IngredientService();
    private static DoughService doughService = new DoughService();
    private static PizzaService pizzaService = new PizzaService();
    private static CrustService crustService = new CrustService();
    private static OrderService orderService = new OrderService();
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in, "CP866");
        

        initSampleData();

        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": manageIngredients(); break;
                case "2": manageDoughs(); break;
                case "3": managePizzas(); break;
                case "4": manageCrusts(); break;
                case "5": createOrder(); break;
                case "6": viewOrders(); break;
                case "7": filterPizzas(); break;
                case "8": filterOrders(); break;
                case "0": System.out.println("Выход из программы."); return;
                default: System.out.println("Неверный ввод. Попробуйте снова.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n========== ГЛАВНОЕ МЕНЮ ==========");
        System.out.println("1. Управление ингредиентами");
        System.out.println("2. Управление основами");
        System.out.println("3. Управление пиццами");
        System.out.println("4. Управление бортиками");
        System.out.println("5. Создать заказ");
        System.out.println("6. Просмотреть все заказы");
        System.out.println("7. Фильтрация пицц по ингредиенту");
        System.out.println("8. Фильтрация заказов по дате");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void manageIngredients() {
        while (true) {
            System.out.println("\n--- Управление ингредиентами ---");
            System.out.println("1. Добавить ингредиент");
            System.out.println("2. Редактировать ингредиент");
            System.out.println("3. Удалить ингредиент");
            System.out.println("4. Список ингредиентов");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addIngredient(); break;
                case "2": editIngredient(); break;
                case "3": deleteIngredient(); break;
                case "4": listIngredients(); break;
                case "0": return;
                default: System.out.println("Неверный ввод.");
            }
        }
    }

    private static void addIngredient() {
        System.out.print("Введите название ингредиента: ");
        String name = scanner.nextLine();
        System.out.print("Введите стоимость: ");
        try {
            double cost = Double.parseDouble(scanner.nextLine());
            Ingredient ing = ingredientService.create(name, cost);
            System.out.println("Ингредиент добавлен: " + ing);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: неверный формат стоимости.");
        }
    }

    private static void editIngredient() {
        listIngredients();
        System.out.print("Введите ID ингредиента для редактирования: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            Ingredient ing = ingredientService.get(id);
            if (ing == null) {
                System.out.println("Ингредиент не найден.");
                return;
            }
            System.out.print("Новое название (оставьте пустым для пропуска): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) ing.setName(name);
            System.out.print("Новая стоимость (оставьте пустым для пропуска): ");
            String costStr = scanner.nextLine();
            if (!costStr.isEmpty()) {
                double cost = Double.parseDouble(costStr);
                ing.setCost(cost);
            }
            System.out.println("Ингредиент обновлён: " + ing);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный ID или формат.");
        }
    }

    private static void deleteIngredient() {
        listIngredients();
        System.out.print("Введите ID ингредиента для удаления: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            ingredientService.delete(id);
            System.out.println("Ингредиент удалён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный ID.");
        }
    }

    private static void listIngredients() {
        List<Ingredient> list = ingredientService.getAll();
        if (list.isEmpty()) {
            System.out.println("Список ингредиентов пуст.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void manageDoughs() {
        while (true) {
            System.out.println("\n--- Управление основами ---");
            System.out.println("1. Добавить основу");
            System.out.println("2. Редактировать основу");
            System.out.println("3. Удалить основу");
            System.out.println("4. Список основ");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addDough(); break;
                case "2": editDough(); break;
                case "3": deleteDough(); break;
                case "4": listDoughs(); break;
                case "0": return;
                default: System.out.println("Неверный ввод.");
            }
        }
    }

    private static void addDough() {
        System.out.print("Введите название основы: ");
        String name = scanner.nextLine();
        System.out.print("Введите стоимость: ");
        try {
            double cost = Double.parseDouble(scanner.nextLine());
            System.out.print("Это классическая основа? (да/нет): ");
            String classicStr = scanner.nextLine();
            boolean classic = classicStr.equalsIgnoreCase("да");
            Dough dough = doughService.create(name, cost, classic);
            System.out.println("Основа добавлена: " + dough);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: неверный формат стоимости.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void editDough() {
        listDoughs();
        System.out.print("Введите ID основы для редактирования: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            Dough dough = doughService.get(id);
            if (dough == null) {
                System.out.println("Основа не найдена.");
                return;
            }
            System.out.print("Новое название (оставьте пустым для пропуска): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) dough.setName(name);
            System.out.print("Новая стоимость (оставьте пустым для пропуска): ");
            String costStr = scanner.nextLine();
            double cost = dough.getCost();
            if (!costStr.isEmpty()) {
                cost = Double.parseDouble(costStr);
            }
            System.out.print("Классическая? (да/нет/пропустить): ");
            String classicStr = scanner.nextLine();
            Boolean classic = null;
            if (!classicStr.isEmpty()) {
                classic = classicStr.equalsIgnoreCase("да");
            }
            doughService.update(id, dough.getName(), cost, classic);
            System.out.println("Основа обновлена: " + dough);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void deleteDough() {
        listDoughs();
        System.out.print("Введите ID основы для удаления: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            doughService.delete(id);
            System.out.println("Основа удалена.");
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный ID.");
        }
    }

    private static void listDoughs() {
        List<Dough> list = doughService.getAll();
        if (list.isEmpty()) {
            System.out.println("Список основ пуст.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void managePizzas() {
        while (true) {
            System.out.println("\n--- Управление пиццами ---");
            System.out.println("1. Добавить пиццу");
            System.out.println("2. Редактировать пиццу");
            System.out.println("3. Удалить пиццу");
            System.out.println("4. Список пицц");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addPizza(); break;
                case "2": editPizza(); break;
                case "3": deletePizza(); break;
                case "4": listPizzas(); break;
                case "0": return;
                default: System.out.println("Неверный ввод.");
            }
        }
    }

    private static void addPizza() {
        System.out.print("Введите название пиццы: ");
        String name = scanner.nextLine();
        System.out.println("Выберите основу (введите ID):");
        listDoughs();
        String doughIdStr = scanner.nextLine();
        try {
            UUID doughId = UUID.fromString(doughIdStr);
            Dough dough = doughService.get(doughId);
            if (dough == null) {
                System.out.println("Основа не найдена.");
                return;
            }
            List<Ingredient> ingredients = new ArrayList<>();
            while (true) {
                System.out.println("Текущие ингредиенты: " + ingredients.stream().map(Ingredient::getName).collect(Collectors.toList()));
                System.out.println("1. Добавить ингредиент");
                System.out.println("2. Завершить выбор");
                System.out.print("Выберите действие: ");
                String choice = scanner.nextLine();
                if (choice.equals("2")) break;
                if (choice.equals("1")) {
                    listIngredients();
                    System.out.print("Введите ID ингредиента: ");
                    String ingIdStr = scanner.nextLine();
                    try {
                        UUID ingId = UUID.fromString(ingIdStr);
                        Ingredient ing = ingredientService.get(ingId);
                        if (ing != null) {
                            ingredients.add(ing);
                        } else {
                            System.out.println("Ингредиент не найден.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный ID.");
                    }
                }
            }
            Pizza pizza = pizzaService.create(name, dough, ingredients);
            System.out.println("Пицца добавлена: " + pizza);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void editPizza() {
        listPizzas();
        System.out.print("Введите ID пиццы для редактирования: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            Pizza pizza = pizzaService.get(id);
            if (pizza == null) {
                System.out.println("Пицца не найдена.");
                return;
            }
            System.out.print("Новое название (оставьте пустым для пропуска): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) pizza.setName(name);
            System.out.println("Выберите новую основу (оставьте пустым для пропуска):");
            listDoughs();
            String doughIdStr = scanner.nextLine();
            if (!doughIdStr.isEmpty()) {
                UUID doughId = UUID.fromString(doughIdStr);
                Dough dough = doughService.get(doughId);
                if (dough != null) pizza.setDough(dough);
                else System.out.println("Основа не найдена, оставляем прежнюю.");
            }
            System.out.println("Хотите изменить список ингредиентов? (да/нет): ");
            if (scanner.nextLine().equalsIgnoreCase("да")) {
                List<Ingredient> ingredients = new ArrayList<>();
                while (true) {
                    System.out.println("Текущие ингредиенты: " + ingredients.stream().map(Ingredient::getName).collect(Collectors.toList()));
                    System.out.println("1. Добавить ингредиент");
                    System.out.println("2. Завершить выбор");
                    System.out.print("Выберите действие: ");
                    String choice = scanner.nextLine();
                    if (choice.equals("2")) break;
                    if (choice.equals("1")) {
                        listIngredients();
                        System.out.print("Введите ID ингредиента: ");
                        String ingIdStr = scanner.nextLine();
                        try {
                            UUID ingId = UUID.fromString(ingIdStr);
                            Ingredient ing = ingredientService.get(ingId);
                            if (ing != null) {
                                ingredients.add(ing);
                            } else {
                                System.out.println("Ингредиент не найден.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный ID.");
                        }
                    }
                }
                pizza.setIngredients(ingredients);
            }
            System.out.println("Пицца обновлена: " + pizza);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void deletePizza() {
        listPizzas();
        System.out.print("Введите ID пиццы для удаления: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            pizzaService.delete(id);
            System.out.println("Пицца удалена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный ID.");
        }
    }

    private static void listPizzas() {
        List<Pizza> list = pizzaService.getAll();
        if (list.isEmpty()) {
            System.out.println("Список пицц пуст.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void manageCrusts() {
        while (true) {
            System.out.println("\n--- Управление бортиками ---");
            System.out.println("1. Добавить бортик");
            System.out.println("2. Редактировать бортик");
            System.out.println("3. Удалить бортик");
            System.out.println("4. Список бортиков");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addCrust(); break;
                case "2": editCrust(); break;
                case "3": deleteCrust(); break;
                case "4": listCrusts(); break;
                case "0": return;
                default: System.out.println("Неверный ввод.");
            }
        }
    }

    private static void addCrust() {
        System.out.print("Введите название бортика: ");
        String name = scanner.nextLine();
        List<Ingredient> ingredients = new ArrayList<>();
        while (true) {
            System.out.println("Ингредиенты бортика: " + ingredients.stream().map(Ingredient::getName).collect(Collectors.toList()));
            System.out.println("1. Добавить ингредиент");
            System.out.println("2. Завершить выбор");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            if (choice.equals("2")) break;
            if (choice.equals("1")) {
                listIngredients();
                System.out.print("Введите ID ингредиента: ");
                String ingIdStr = scanner.nextLine();
                try {
                    UUID ingId = UUID.fromString(ingIdStr);
                    Ingredient ing = ingredientService.get(ingId);
                    if (ing != null) {
                        ingredients.add(ing);
                    } else {
                        System.out.println("Ингредиент не найден.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный ID.");
                }
            }
        }
        System.out.print("Тип списка: белый список (разрешённые пиццы) или чёрный (запрещённые)? (белый/черный): ");
        String listType = scanner.nextLine();
        boolean isWhitelist = listType.equalsIgnoreCase("белый");
        Set<Pizza> pizzas = new HashSet<>();
        System.out.println("Выберите пиццы для списка (можно оставить пустым, если список пуст):");
        while (true) {
            listPizzas();
            System.out.println("Текущие пиццы в списке: " + pizzas.stream().map(Pizza::getName).collect(Collectors.toList()));
            System.out.println("1. Добавить пиццу");
            System.out.println("2. Завершить выбор");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            if (choice.equals("2")) break;
            if (choice.equals("1")) {
                System.out.print("Введите ID пиццы: ");
                String pizzaIdStr = scanner.nextLine();
                try {
                    UUID pizzaId = UUID.fromString(pizzaIdStr);
                    Pizza pizza = pizzaService.get(pizzaId);
                    if (pizza != null) {
                        pizzas.add(pizza);
                    } else {
                        System.out.println("Пицца не найдена.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный ID.");
                }
            }
        }
        Crust crust = crustService.create(name, ingredients, pizzas, isWhitelist);
        System.out.println("Бортик добавлен: " + crust);
    }

    private static void editCrust() {
        listCrusts();
        System.out.print("Введите ID бортика для редактирования: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            Crust crust = crustService.get(id);
            if (crust == null) {
                System.out.println("Бортик не найден.");
                return;
            }
            System.out.print("Новое название (оставьте пустым для пропуска): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) crust.setName(name);
            System.out.println("Хотите изменить список ингредиентов? (да/нет): ");
            if (scanner.nextLine().equalsIgnoreCase("да")) {
                List<Ingredient> ingredients = new ArrayList<>();
                while (true) {
                    System.out.println("Ингредиенты: " + ingredients.stream().map(Ingredient::getName).collect(Collectors.toList()));
                    System.out.println("1. Добавить ингредиент");
                    System.out.println("2. Завершить выбор");
                    System.out.print("Выберите действие: ");
                    String choice = scanner.nextLine();
                    if (choice.equals("2")) break;
                    if (choice.equals("1")) {
                        listIngredients();
                        System.out.print("Введите ID ингредиента: ");
                        String ingIdStr = scanner.nextLine();
                        try {
                            UUID ingId = UUID.fromString(ingIdStr);
                            Ingredient ing = ingredientService.get(ingId);
                            if (ing != null) {
                                ingredients.add(ing);
                            } else {
                                System.out.println("Ингредиент не найден.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный ID.");
                        }
                    }
                }
                crust.setIngredients(ingredients);
            }
            System.out.println("Хотите изменить список пицц? (да/нет): ");
            if (scanner.nextLine().equalsIgnoreCase("да")) {
                Set<Pizza> pizzas = new HashSet<>();
                System.out.println("Выберите пиццы для списка:");
                while (true) {
                    listPizzas();
                    System.out.println("Текущие пиццы: " + pizzas.stream().map(Pizza::getName).collect(Collectors.toList()));
                    System.out.println("1. Добавить пиццу");
                    System.out.println("2. Завершить выбор");
                    System.out.print("Выберите действие: ");
                    String choice = scanner.nextLine();
                    if (choice.equals("2")) break;
                    if (choice.equals("1")) {
                        System.out.print("Введите ID пиццы: ");
                        String pizzaIdStr = scanner.nextLine();
                        try {
                            UUID pizzaId = UUID.fromString(pizzaIdStr);
                            Pizza pizza = pizzaService.get(pizzaId);
                            if (pizza != null) {
                                pizzas.add(pizza);
                            } else {
                                System.out.println("Пицца не найдена.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный ID.");
                        }
                    }
                }
                crust.setPizzas(pizzas);
            }
            System.out.print("Изменить тип списка? (белый/черный/пропустить): ");
            String type = scanner.nextLine();
            if (!type.isEmpty()) {
                crust.setWhitelist(type.equalsIgnoreCase("белый"));
            }
            System.out.println("Бортик обновлён: " + crust);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void deleteCrust() {
        listCrusts();
        System.out.print("Введите ID бортика для удаления: ");
        String idStr = scanner.nextLine();
        try {
            UUID id = UUID.fromString(idStr);
            crustService.delete(id);
            System.out.println("Бортик удалён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный ID.");
        }
    }

    private static void listCrusts() {
        List<Crust> list = crustService.getAll();
        if (list.isEmpty()) {
            System.out.println("Список бортиков пуст.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void createOrder() {
        List<OrderItem> items = new ArrayList<>();
        System.out.println("\n--- Создание заказа ---");
        while (true) {
            System.out.println("Тип элемента заказа:");
            System.out.println("1. Готовая пицца из каталога (с возможностью удвоения ингредиентов)");
            System.out.println("2. Создать пиццу вручную (не сохраняется в каталог)");
            System.out.println("3. Комбинированная пицца (половина А + половина Б)");
            System.out.println("0. Завершить добавление и оформить заказ");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": items.add(createCatalogPizzaItem()); break;
                case "2": items.add(createCustomPizzaItem()); break;
                case "3": items.add(createCompositePizzaItem()); break;
                case "0":
                    if (items.isEmpty()) {
                        System.out.println("Заказ не может быть пустым.");
                        continue;
                    }
                    System.out.print("Введите комментарий к заказу: ");
                    String comment = scanner.nextLine();
                    System.out.print("Укажите дату и время для отложенного заказа (например, 2025-05-03T15:30) или оставьте пустым: ");
                    String scheduledStr = scanner.nextLine();
                    LocalDateTime scheduled = null;
                    if (!scheduledStr.isEmpty()) {
                        try {
                            scheduled = LocalDateTime.parse(scheduledStr);
                        } catch (DateTimeParseException e) {
                            System.out.println("Неверный формат даты. Заказ будет создан без отложенного времени.");
                        }
                    }
                    Order order = orderService.create(items, comment, scheduled);
                    System.out.println("Заказ создан: " + order);
                    return;
                default: System.out.println("Неверный ввод.");
            }
        }
    }

    private static CatalogPizzaItem createCatalogPizzaItem() {
        System.out.println("Выберите пиццу из каталога:");
        listPizzas();
        System.out.print("Введите ID пиццы: ");
        String pizzaIdStr = scanner.nextLine();
        Pizza pizza = pizzaService.get(UUID.fromString(pizzaIdStr));
        if (pizza == null) {
            System.out.println("Пицца не найдена. Элемент не будет добавлен.");
            return null;
        }
        Map<Ingredient, Integer> extra = new HashMap<>();
        System.out.println("Хотите удвоить какие-либо ингредиенты? (да/нет): ");
        if (scanner.nextLine().equalsIgnoreCase("да")) {
            while (true) {
                System.out.println("Текущие удвоения: " + extra);
                listIngredients();
                System.out.print("Введите ID ингредиента для удвоения (или пустую строку для завершения): ");
                String ingIdStr = scanner.nextLine();
                if (ingIdStr.isEmpty()) break;
                try {
                    Ingredient ing = ingredientService.get(UUID.fromString(ingIdStr));
                    if (ing != null && pizza.getIngredients().contains(ing)) {
                        extra.put(ing, extra.getOrDefault(ing, 0) + 1);
                    } else {
                        System.out.println("Ингредиент не найден или не входит в состав пиццы.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный ID.");
                }
            }
        }
        System.out.print("Выберите размер (маленькая, средняя, большая): ");
        Size size = Size.fromString(scanner.nextLine());
        System.out.println("Выберите бортик (введите ID или оставьте пустым для отсутствия):");
        listCrusts();
        String crustIdStr = scanner.nextLine();
        Crust crust = null;
        if (!crustIdStr.isEmpty()) {
            crust = crustService.get(UUID.fromString(crustIdStr));
            if (crust != null && !crust.isCompatibleWith(pizza)) {
                System.out.println("Предупреждение: бортик несовместим с этой пиццей, но будет использован.");
            }
        }
        return new CatalogPizzaItem(pizza, extra, size, crust);
    }

    private static CustomPizzaItem createCustomPizzaItem() {
        System.out.print("Введите название для кастомной пиццы: ");
        String name = scanner.nextLine();
        System.out.println("Выберите основу:");
        listDoughs();
        Dough dough = doughService.get(UUID.fromString(scanner.nextLine()));
        if (dough == null) {
            System.out.println("Основа не найдена. Элемент не будет добавлен.");
            return null;
        }
        List<Ingredient> ingredients = new ArrayList<>();
        Map<Ingredient, Integer> multipliers = new HashMap<>();
        while (true) {
            System.out.println("Текущие ингредиенты: " + ingredients.stream().map(Ingredient::getName).collect(Collectors.toList()));
            System.out.println("1. Добавить ингредиент");
            System.out.println("2. Завершить выбор");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();
            if (choice.equals("2")) break;
            if (choice.equals("1")) {
                listIngredients();
                System.out.print("Введите ID ингредиента: ");
                String ingIdStr = scanner.nextLine();
                try {
                    Ingredient ing = ingredientService.get(UUID.fromString(ingIdStr));
                    if (ing != null) {
                        ingredients.add(ing);
                        System.out.print("Введите множитель (по умолчанию 1): ");
                        String multStr = scanner.nextLine();
                        int mult = multStr.isEmpty() ? 1 : Integer.parseInt(multStr);
                        multipliers.put(ing, mult);
                    } else {
                        System.out.println("Ингредиент не найден.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неверный множитель, используется 1.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный ID.");
                }
            }
        }
        System.out.print("Выберите размер (маленькая, средняя, большая): ");
        Size size = Size.fromString(scanner.nextLine());
        System.out.println("Выберите бортик (введите ID или оставьте пустым):");
        listCrusts();
        String crustIdStr = scanner.nextLine();
        Crust crust = crustIdStr.isEmpty() ? null : crustService.get(UUID.fromString(crustIdStr));
        return new CustomPizzaItem(name, dough, ingredients, multipliers, size, crust);
    }

    private static CompositePizzaItem createCompositePizzaItem() {
        System.out.println("Создание комбинированной пиццы (две половинки)");
        System.out.println("Выберите основу для всей пиццы:");
        listDoughs();
        Dough dough = doughService.get(UUID.fromString(scanner.nextLine()));
        if (dough == null) {
            System.out.println("Основа не найдена. Элемент не будет добавлен.");
            return null;
        }
        System.out.println("=== Первая половинка ===");
        Half half1 = createHalf();
        if (half1 == null) return null;
        System.out.println("=== Вторая половинка ===");
        Half half2 = createHalf();
        if (half2 == null) return null;
        System.out.print("Выберите размер (маленькая, средняя, большая): ");
        Size size = Size.fromString(scanner.nextLine());
        System.out.print("Использовать общий бортик для всей пиццы? (да/нет): ");
        boolean useCommon = scanner.nextLine().equalsIgnoreCase("да");
        Crust commonCrust = null;
        if (useCommon) {
            System.out.println("Выберите общий бортик:");
            listCrusts();
            String crustIdStr = scanner.nextLine();
            commonCrust = crustIdStr.isEmpty() ? null : crustService.get(UUID.fromString(crustIdStr));
        }
        try {
            return new CompositePizzaItem(dough, half1, half2, size, commonCrust, useCommon);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return null;
        }
    }

    private static Half createHalf() {
        System.out.println("Тип половинки:");
        System.out.println("1. Из каталога пицц");
        System.out.println("2. Создать свою");
        System.out.print("Выберите: ");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            System.out.println("Выберите пиццу из каталога:");
            listPizzas();
            String pizzaIdStr = scanner.nextLine();
            Pizza pizza = pizzaService.get(UUID.fromString(pizzaIdStr));
            if (pizza == null) {
                System.out.println("Пицца не найдена.");
                return null;
            }
            Map<Ingredient, Integer> multipliers = new HashMap<>();
            System.out.println("Хотите удвоить ингредиенты? (да/нет): ");
            if (scanner.nextLine().equalsIgnoreCase("да")) {
                while (true) {
                    System.out.println("Текущие удвоения: " + multipliers);
                    listIngredients();
                    System.out.print("Введите ID ингредиента для удвоения (пусто для завершения): ");
                    String ingIdStr = scanner.nextLine();
                    if (ingIdStr.isEmpty()) break;
                    try {
                        Ingredient ing = ingredientService.get(UUID.fromString(ingIdStr));
                        if (ing != null && pizza.getIngredients().contains(ing)) {
                            multipliers.put(ing, multipliers.getOrDefault(ing, 0) + 1);
                        } else {
                            System.out.println("Ингредиент не найден или не входит в состав пиццы.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный ID.");
                    }
                }
            }
            System.out.println("Выберите бортик для этой половинки:");
            listCrusts();
            String crustIdStr = scanner.nextLine();
            Crust crust = crustIdStr.isEmpty() ? null : crustService.get(UUID.fromString(crustIdStr));
            return new CatalogHalf(pizza, multipliers, crust);
        } else if (choice.equals("2")) {
            List<Ingredient> ingredients = new ArrayList<>();
            Map<Ingredient, Integer> multipliers = new HashMap<>();
            while (true) {
                System.out.println("Текущие ингредиенты: " + ingredients.stream().map(Ingredient::getName).collect(Collectors.toList()));
                System.out.println("1. Добавить ингредиент");
                System.out.println("2. Завершить выбор");
                System.out.print("Выберите действие: ");
                String subChoice = scanner.nextLine();
                if (subChoice.equals("2")) break;
                if (subChoice.equals("1")) {
                    listIngredients();
                    System.out.print("Введите ID ингредиента: ");
                    String ingIdStr = scanner.nextLine();
                    try {
                        Ingredient ing = ingredientService.get(UUID.fromString(ingIdStr));
                        if (ing != null) {
                            ingredients.add(ing);
                            System.out.print("Введите множитель (по умолчанию 1): ");
                            String multStr = scanner.nextLine();
                            int mult = multStr.isEmpty() ? 1 : Integer.parseInt(multStr);
                            multipliers.put(ing, mult);
                        } else {
                            System.out.println("Ингредиент не найден.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный множитель, используется 1.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный ID.");
                    }
                }
            }
            System.out.println("Выберите бортик для этой половинки:");
            listCrusts();
            String crustIdStr = scanner.nextLine();
            Crust crust = crustIdStr.isEmpty() ? null : crustService.get(UUID.fromString(crustIdStr));
            return new CustomHalf(ingredients, multipliers, crust);
        } else {
            System.out.println("Неверный выбор.");
            return null;
        }
    }

    private static void viewOrders() {
        List<Order> orders = orderService.getAll();
        if (orders.isEmpty()) {
            System.out.println("Список заказов пуст.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    private static void filterPizzas() {
        listIngredients();
        System.out.print("Введите ID ингредиента для фильтрации: ");
        String idStr = scanner.nextLine();
        try {
            Ingredient ing = ingredientService.get(UUID.fromString(idStr));
            if (ing == null) {
                System.out.println("Ингредиент не найден.");
                return;
            }
            List<Pizza> filtered = pizzaService.filterByIngredient(ing);
            if (filtered.isEmpty()) {
                System.out.println("Нет пицц с таким ингредиентом.");
            } else {
                filtered.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный ID.");
        }
    }

    private static void filterOrders() {
        System.out.print("Введите дату (ГГГГ-ММ-ДД): ");
        String dateStr = scanner.nextLine();
        try {
            LocalDate date = LocalDate.parse(dateStr);
            List<Order> filtered = orderService.filterByDate(date);
            if (filtered.isEmpty()) {
                System.out.println("Нет заказов на эту дату.");
            } else {
                filtered.forEach(System.out::println);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты.");
        }
    }
    private static void initSampleData() {
        ingredientService.create("Помидор", 1.5);
        ingredientService.create("Сыр", 2.0);
        ingredientService.create("Грибы", 1.8);
        ingredientService.create("Пепперони", 2.5);
        ingredientService.create("Лук", 1.0);
        ingredientService.create("Кунжут", 0.5);

        doughService.create("Классическое", 3.0, true);
        doughService.create("Чёрное", 3.5, false);
        doughService.create("Толстое", 3.5, false);

        Ingredient tomato = ingredientService.getAll().stream().filter(i -> i.getName().equals("Помидор")).findFirst().get();
        Ingredient cheese = ingredientService.getAll().stream().filter(i -> i.getName().equals("Сыр")).findFirst().get();
        Ingredient mushroom = ingredientService.getAll().stream().filter(i -> i.getName().equals("Грибы")).findFirst().get();
        Ingredient pepperoni = ingredientService.getAll().stream().filter(i -> i.getName().equals("Пепперони")).findFirst().get();
        Ingredient onion = ingredientService.getAll().stream().filter(i -> i.getName().equals("Лук")).findFirst().get();
        Ingredient sesame = ingredientService.getAll().stream().filter(i -> i.getName().equals("Кунжут")).findFirst().get();

        Dough classic = doughService.getAll().stream().filter(d -> d.getName().equals("Классическое")).findFirst().get();
        Dough black = doughService.getAll().stream().filter(d -> d.getName().equals("Чёрное")).findFirst().get();

        Pizza margherita = pizzaService.create("Маргарита", classic, Arrays.asList(tomato, cheese));
        Pizza pepperoniPizza = pizzaService.create("Пепперони", black, Arrays.asList(cheese, pepperoni));
        pizzaService.create("Грибная", classic, Arrays.asList(mushroom, cheese, onion));

        crustService.create("С кунжутом", Arrays.asList(sesame), new HashSet<>(Arrays.asList(margherita)), true);
        crustService.create("С сыром", Arrays.asList(cheese), new HashSet<>(Arrays.asList(pepperoniPizza)), false);
        crustService.create("Обычный", Arrays.asList(), new HashSet<>(), false);
    }
}