import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {
    private Map<UUID, Order> orders = new HashMap<>();
    private AtomicLong orderNumberCounter = new AtomicLong(1);

    public Order create(List<OrderItem> items, String comment, LocalDateTime scheduledTime) {
        String orderNumber = "ORD-" + orderNumberCounter.getAndIncrement();
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(orderNumber, items, comment, now, scheduledTime);
        orders.put(order.getId(), order);
        return order;
    }

    public Order get(UUID id) {
        return orders.get(id);
    }

    public List<Order> getAll() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> filterByDate(LocalDate date) {
        return orders.values().stream()
                .filter(o -> o.getOrderTime().toLocalDate().equals(date))
                .toList();
    }

    public List<Order> filterByScheduledDate(LocalDate date) {
        return orders.values().stream()
                .filter(o -> o.getScheduledTime() != null && o.getScheduledTime().toLocalDate().equals(date))
                .toList();
    }

    public void delete(UUID id) {
        orders.remove(id);
    }
}