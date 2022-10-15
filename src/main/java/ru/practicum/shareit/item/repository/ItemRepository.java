package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAll();

    Optional<Item> getById(Long id);

    Item create(Item item);

    Item update(Item item, Long id);

    void delete(Long id);
}
