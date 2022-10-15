package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userRepository.getAll()) {
            users.add(UserMapper.toUserDto(user));
        }
        return users;
    }

    @Override
    public UserDto getById(Long id) {
        if (userRepository.getById(id).isEmpty()) {
            throw new UserNotFoundException("Пользователя с данным id не найдено");
        }
        return UserMapper.toUserDto(userRepository.getById(id).get());
    }

    @Override
    public UserDto create(User user) {
        checkEmailUnique(user);
        return UserMapper.toUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(User user, Long id) {
        User updatedUser = userRepository.getById(id).get();
        if (updatedUser == null) {
            throw new UserNotFoundException("Невозможно обновить данные пользователя. Пользователя с данным id не найдено");
        }
        if (user.getEmail() != null) {
            checkEmailUnique(user);
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        return UserMapper.toUserDto(userRepository.update(updatedUser, id));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        userRepository.delete(id);
    }

    private void checkEmailUnique(User user) {
        for (User userCheck : userRepository.getAll()) {
            if (user.getEmail().equals(userCheck.getEmail())) {
                throw new UserEmailException("Пользователь с таким email уже зарегистрирован");
            }
        }
    }
}
