package ru.practicum.shareit.itemtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTests {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void searchTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRepository.save(Item.builder().name("name").description("description").available(true).owner(user).build());
        Page<Item> items = itemRepository.search("desc", Pageable.ofSize(10));
        assertThat(items.stream().count(), equalTo(1L));
    }

    @Test
    void findAllByOwnerIdTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRepository.save(Item.builder().name("name").description("description").available(true).owner(user).build());
        Page<Item> items = itemRepository.findAllByOwnerId(user.getId(), Pageable.ofSize(10));
        assertThat(items.stream().count(), equalTo(1L));
    }

    @Test
    void findAllByRequestIdTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        User user2 = userRepository.save(User.builder().name("name2").email("email2@email.com").build());
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequest.builder().description("item request descr")
                .requestor(user2).created(LocalDateTime.now()).build());
        itemRepository.save(Item.builder().name("name").description("description").available(true)
                .owner(user).request(itemRequest).build());
        assertThat(itemRepository.findAllByRequestId(itemRequest.getId()).size(), equalTo(1));
    }

    @Test
    void findAllCommentByItemIdTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        User user2 = userRepository.save(User.builder().name("name2").email("email2@email.com").build());
        Item item = itemRepository.save(Item.builder().name("name").description("description")
                .available(true).owner(user).build());
        commentRepository.save(Comment.builder().text("text of comment").item(item).author(user2)
                .created(LocalDateTime.now()).build());
        assertThat(commentRepository.findAllByItemId(item.getId()).size(), equalTo(1));
    }
}
