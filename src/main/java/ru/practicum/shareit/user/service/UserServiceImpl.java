package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.CommonUtils;
import ru.practicum.shareit.exception.ExceptionUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> ExceptionUtils.getUserNotFoundException(id));
    }

    @Override
    public User update(User entity) {
        User user = findById(entity.getId());

        String newName = entity.getName();
        if (CommonUtils.isStringNotBlank(newName)) {
            user.setName(newName);
        }

        String newEmail = entity.getEmail();
        if (CommonUtils.isStringNotBlank(newEmail)) {
            user.setEmail(newEmail);
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteById(long id) {
        findById(id);
        userRepository.deleteById(id);
    }

}
