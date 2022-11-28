package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ExceptionUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User add(User obj) {
        return userRepository.create(obj);
    }

    @Override
    public List<User> findAll() {
        return userRepository.readAll();
    }

    @Override
    public User find(long id) {
        return userRepository
                .read(id)
                .orElseThrow(() -> ExceptionUtils.getUserNotFoundException(id));
    }

    @Override
    public User change(User obj) {
        find(obj.getId());
        return userRepository.update(obj);
    }

    @Override
    public void wipe(long id) {
        find(id);
        userRepository.destroy(id);
    }

}
