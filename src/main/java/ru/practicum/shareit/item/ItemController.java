package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Невозможно найти вещи - " +
                    "неккоректно переданы параметры поиска - индекс первого элемента не может быть меньше нуля, " +
                    "а размер страницы должен быть больше нуля");
        }
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getById(id, userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long id,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.update(itemDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Невозможно найти вещи - " +
                    "неккоректно переданы параметры поиска - индекс первого элемента не может быть меньше нуля, " +
                    "а размер страницы должен быть больше нуля");
        }
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
