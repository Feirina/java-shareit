package ru.practicum.shareit.itemrequesttests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestControllerTests {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    private ItemRequestDto itemRequestDto = ItemRequestDto
            .builder()
            .description("item request description")
            .build();

    private UserDto userDto = UserDto
            .builder()
            .name("name")
            .email("user@email.com")
            .build();

    @Test
    void createTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(1L, itemRequestController.getById(itemRequest.getId(), user.getId()).getId());
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(1, itemRequestController.getAllByUser(user.getId()).size());
    }

    @Test
    void getAll() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        assertEquals(0, itemRequestController.getAll(0, 10, user.getId()).size());
        UserDto user2 = userController.create(userDto.toBuilder().email("user1@email.com").build());
        assertEquals(1, itemRequestController.getAll(0, 10, user2.getId()).size());
    }
}
