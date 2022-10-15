package ru.practicum.shareit.inmemorytests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    private ItemDto itemDto = ItemDto.builder().name("name").description("description").available(true).build();
    private UserDto userDto = UserDto.builder().build().builder().name("name").email("user@email.com").build();

    @Test
    void createTest() {
        userController.create(UserMapper.toUser(userDto));
        ItemDto item = itemController.create(1L, itemDto);
        assertEquals(item.getId(), itemController.getById(item.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.create(UserMapper.toUser(userDto));
        itemController.create(1L, itemDto);
        ItemDto item = itemDto.toBuilder().description("updateDescription").build();
        itemController.update(item, 1L, 1L);
        assertEquals(item.getDescription(), itemController.getById(1L).getDescription());
    }

    @Test
    void deleteTest() {
        userController.create(UserMapper.toUser(userDto));
        itemController.create(1L, itemDto);
        assertEquals(1, itemController.getAll(1L).size());
        itemController.delete(1L);
        assertEquals(0, itemController.getAll(1L).size());
    }

    @Test
    void searchTest() {
        userController.create(UserMapper.toUser(userDto));
        itemController.create(1L, itemDto);
        assertEquals(1, itemController.search("Desc").size());
    }
}
