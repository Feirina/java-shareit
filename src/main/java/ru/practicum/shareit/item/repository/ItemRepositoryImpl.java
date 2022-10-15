package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository{
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Optional<Item> getById(Long id) {
        return items.get(id) != null ? Optional.of(items.get(id)) : Optional.empty();
    }

    @Override
    public Item create(Item item) {
        item.setId(id);
        id++;
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item, Long id) {
        items.put(id, item);
        return getById(id).get();
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }
}
