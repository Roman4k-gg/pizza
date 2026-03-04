import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order implements Entity {
    private UUID id;
    private String orderNumber;
    private List<OrderItem> items;
    private String comment;
    private LocalDateTime orderTime;
    private LocalDateTime scheduledTime;

    public Order(String orderNumber, List<OrderItem> items, String comment,
                 LocalDateTime orderTime, LocalDateTime scheduledTime) {
        this.id = UUID.randomUUID();
        this.orderNumber = orderNumber;
        this.items = new ArrayList<>(items);
        this.comment = comment;
        this.orderTime = orderTime;
        this.scheduledTime = scheduledTime;
    }

    @Override
    public UUID getId() { return id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = new ArrayList<>(items); }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public double getTotalCost() {
        return items.stream().mapToDouble(OrderItem::calculateCost).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String scheduled = scheduledTime != null ? ", отложен на: " + scheduledTime : "";
        return String.format("Заказ №%s, время: %s, стоимость: %.2f, комментарий: '%s'%s, ID: %s",
                orderNumber, orderTime, getTotalCost(), comment, scheduled, id);
    }
}