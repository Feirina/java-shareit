package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> findAll();

    Optional<Item> findById(Long id);

    Item create(Item item);

    Item update(Item item);

    void delete(Long id);
}
