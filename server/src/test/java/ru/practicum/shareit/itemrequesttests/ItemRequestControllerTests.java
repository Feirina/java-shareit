package ru.practicum.shareit.itemrequesttests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestControllerTests {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    private ItemRequestDto itemRequestDto;

    private UserDto userDto;

    @BeforeEach
    void init() {
        itemRequestDto = ItemRequestDto
                .builder()
                .description("item request description")
                .build();

        userDto = UserDto
                .builder()
                .name("name")
                .email("user@email.com")
                .build();
    }

    @Test
    void createTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(1L, itemRequestController.getById(itemRequest.getId(), user.getId()).getId());
    }

    @Test
    void createByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemRequestController.create(1L, itemRequestDto));
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(1, itemRequestController.getAllByUser(user.getId()).size());
    }

    @Test
    void getAllByUserWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemRequestController.getAllByUser(1L));
    }

    @Test
    void getAll() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(0, itemRequestController.getAll(user.getId(), 0, 10).size());
        UserDto user2 = userController.create(userDto.toBuilder().email("user1@email.com").build());
        assertEquals(1, itemRequestController.getAll(user2.getId(), 0, 10).size());
    }

    @Test
    void getAllByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemRequestController.getAll(1L, 0, 10));
    }
}
