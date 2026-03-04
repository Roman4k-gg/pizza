import java.util.*;

public class DoughService {
    private Map<UUID, Dough> doughs = new HashMap<>();

    public Dough create(String name, double cost, boolean isClassic) {
        if (isClassic) {
            doughs.values().stream().filter(Dough::isClassic).findFirst().ifPresent(d -> d.setClassic(false));
        } else {
            Optional<Dough> classic = doughs.values().stream().filter(Dough::isClassic).findFirst();
            if (classic.isPresent()) {
                double classicCost = classic.get().getCost();
                if (cost > classicCost * 1.2) {
                    throw new IllegalArgumentException("Стоимость не может превышать 120% от стоимости классической основы");
                }
            } else {
                throw new IllegalStateException("Сначала необходимо создать классическую основу");
            }
        }
        Dough dough = new Dough(name, cost, isClassic);
        doughs.put(dough.getId(), dough);
        return dough;
    }

    public Dough get(UUID id) {
        return doughs.get(id);
    }

    public List<Dough> getAll() {
        return new ArrayList<>(doughs.values());
    }

    public Dough update(UUID id, String name, double cost, Boolean isClassic) {
        Dough dough = doughs.get(id);
        if (dough != null) {
            if (isClassic != null && isClassic) {
                doughs.values().stream().filter(Dough::isClassic).filter(d -> !d.getId().equals(id)).findFirst().ifPresent(d -> d.setClassic(false));
            }
            if (isClassic != null && !isClassic) {
                Optional<Dough> classic = doughs.values().stream().filter(Dough::isClassic).findFirst();
                if (classic.isPresent() && !classic.get().getId().equals(id)) {
                    if (cost > classic.get().getCost() * 1.2) {
                        throw new IllegalArgumentException("Стоимость не может превышать 120% от стоимости классической основы");
                    }
                }
            }
            dough.setName(name);
            dough.setCost(cost);
            if (isClassic != null) {
                dough.setClassic(isClassic);
            }
        }
        return dough;
    }

    public void delete(UUID id) {
        Dough dough = doughs.get(id);
        if (dough != null && dough.isClassic()) {
            throw new IllegalStateException("Нельзя удалить классическую основу");
        }
        doughs.remove(id);
    }

    public Dough getClassicDough() {
        return doughs.values().stream().filter(Dough::isClassic).findFirst().orElse(null);
    }
}