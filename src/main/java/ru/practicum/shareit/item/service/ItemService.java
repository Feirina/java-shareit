package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll(Long userId);

    ItemDto getById(Long id);

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long id, Long userId);

    void delete(Long id);

    List<ItemDto> search(String text);
}
