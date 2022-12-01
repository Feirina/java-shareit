package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll(Long userId, int from, int size);

    ItemDto getById(Long id, Long ownerId);

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long id, Long userId);

    void delete(Long id);

    List<ItemDto> search(String text, int from, int size);

    CommentDto createComment(Long itemId, Long userId, CommentDto commentDto);
}
