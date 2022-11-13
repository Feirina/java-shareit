package ru.practicum.shareit.itemtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemRequestController itemRequestController;

    private ItemDto itemDto;

    private UserDto userDto;

    private ItemRequestDto itemRequestDto;

    private CommentDto comment;

    @BeforeEach
    void init() {
        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        userDto = UserDto.builder()
                .name("name")
                .email("user@email.com")
                .build();

        itemRequestDto = ItemRequestDto
                .builder()
                .description("item request description")
                .build();

        comment = CommentDto
                .builder()
                .text("first comment")
                .build();
    }

    @Test
    void createTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(1L, itemDto);
        assertEquals(item.getId(), itemController.getById(item.getId(), user.getId()).getId());
    }

    @Test
    void createWithRequestTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(user.getId(), itemRequestDto);
        itemDto.setRequestId(1L);
        UserDto user2 = userController.create(userDto.toBuilder().email("user2@email.com").build());
        ItemDto item = itemController.create(2L, itemDto);
        assertEquals(item, itemController.getById(1L, 2L));
    }

    @Test
    void createByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.create(1L, itemDto));
    }

    @Test
    void createWithWrongItemRequest() {
        itemDto.setRequestId(10L);
        UserDto user = userController.create(userDto);
        assertThrows(NotFoundException.class, () -> itemController.create(1L, itemDto));
    }

    @Test
    void updateTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        ItemDto item = itemDto.toBuilder().name("new name").description("updateDescription").available(false).build();
        itemController.update(item, 1L, 1L);
        assertEquals(item.getDescription(), itemController.getById(1L, 1L).getDescription());
    }

    @Test
    void updateForWrongItemTest() {
        assertThrows(NotFoundException.class, () -> itemController.update(itemDto, 1L, 1L));
    }

    @Test
    void updateByWrongUserTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertThrows(NotFoundException.class, () -> itemController.update(itemDto.toBuilder()
                .name("new name").build(), 1L, 10L));
    }

    @Test
    void deleteTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertEquals(1, itemController.getAll(1L, 0, 10).size());
        itemController.delete(1L);
        assertEquals(0, itemController.getAll(1L, 0, 10).size());
    }

    @Test
    void searchTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertEquals(1, itemController.search("Desc", 0, 10).size());
    }

    @Test
    void searchEmptyTextTest() {
        userController.create(userDto);
        itemController.create(1L, itemDto);
        assertEquals(new ArrayList<ItemDto>(), itemController.search("", 0, 10));
    }

    @Test
    void searchWithWrongFrom() {
        assertThrows(BadRequestException.class, () -> itemController.search("t", -1, 10));
    }

    @Test
    void createCommentTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(1L, itemDto);
        UserDto user2 = userController.create(userDto.toBuilder().email("email2@mail.com").build());
        bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 20, 12, 15))
                .end(LocalDateTime.of(2022, 10, 27, 12, 15))
                .itemId(item.getId()).build(), user2.getId());
        bookingController.approve(1L, 1L, true);
        itemController.createComment(item.getId(), user2.getId(), comment);
        assertEquals(1, itemController.getById(1L, 1L).getComments().size());
    }

    @Test
    void createCommentByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.createComment(1L, 1L, comment));
    }

    @Test
    void createCommentToWrongItem() {
        UserDto user = userController.create(userDto);
        assertThrows(NotFoundException.class, () -> itemController.createComment(1L, 1L, comment));
        ItemDto item = itemController.create(1L, itemDto);
        assertThrows(BadRequestException.class, () -> itemController.createComment(1L, 1L, comment));
    }

    @Test
    void getAllWithWrongFrom() {
        assertThrows(BadRequestException.class, () -> itemController.getAll(1L, -1, 10));
    }
}
