package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.getAll()) {
            if (item.getOwner().getId().equals(userId)) {
                items.add(ItemMapper.toItemDto(item));
            }
        }
        return items;
    }

    @Override
    public ItemDto getById(Long id) {
        if (itemRepository.getById(id).isEmpty()) {
            throw new ItemNotFoundException("Вещи с данным id не найдено");
        }
        return ItemMapper.toItemDto(itemRepository.getById(id).get());
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        if (userRepository.getById(userId).isEmpty()) {
            throw new UserNotFoundException("Невозможно создать вещь - пользователя с таким id не существует");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.getById(userId).get());
        itemRepository.create(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long id, Long userId) {
        if (userRepository.getById(userId).isEmpty()) {
            throw new UserNotFoundException("Невозможно обновить вещь - пользователя с таким id не существует");
        }
        if (!itemRepository.getById(id).get().getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException("Невозможно обновить вещь - у данного пользователя нет такой вещи");
        }
        Item item = itemRepository.getById(id).get();
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.update(item, id));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> searchedItems = new ArrayList<>();
        if (text.isBlank()) {
            return searchedItems;
        }
        for (Item item : itemRepository.getAll()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase()) && item.getAvailable()) {
                searchedItems.add(ItemMapper.toItemDto(item));
            }
        }
        return searchedItems;
    }
}
