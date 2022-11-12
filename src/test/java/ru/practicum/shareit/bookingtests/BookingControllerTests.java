package ru.practicum.shareit.bookingtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTests {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDto itemDto = ItemDto.builder()
            .name("name")
            .description("description")
            .available(true)
            .build();

    private UserDto userDto = UserDto.builder()
            .name("name")
            .email("user@email.com")
            .build();

    private UserDto userDto1 = UserDto.builder()
            .name("name")
            .email("user1@email.com")
            .build();

    @Test
    void createTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(user.getId(), itemDto);
        UserDto user1 = userController.create(userDto1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(1L, bookingController.getById(booking.getId(), user1.getId()).getId());
    }

    @Test
    void approveTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(user.getId(), itemDto);
        UserDto user1 = userController.create(userDto1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(WAITING, bookingController.getById(booking.getId(), user1.getId()).getStatus());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(APPROVED, bookingController.getById(booking.getId(), user1.getId()).getStatus());
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(user.getId(), itemDto);
        UserDto user1 = userController.create(userDto1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2023, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), "WAITING", 0, 10).size());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(1, bookingController.getAllByOwner(user.getId(), "CURRENT", 0, 10).size());
    }
}
