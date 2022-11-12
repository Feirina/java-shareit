package ru.practicum.shareit.itemtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class ItemRepositoryTests {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void searchTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRepository.save(Item.builder().name("name").description("description").available(true).owner(user).build());
        Page<Item> items = itemRepository.search("desc", Pageable.ofSize(10));
        assertThat(items.stream().count(), equalTo(1L));
    }
}
