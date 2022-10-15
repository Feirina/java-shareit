package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository{
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(Long id) {
        return users.get(id) == null ? Optional.empty() : Optional.of(users.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, Long id) {
        users.put(id, user);
        return getById(id).get();
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }
}
