package ru.practicum.shareit.server.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.common.CommonUtils;
import ru.practicum.shareit.server.exception.ExceptionUtils;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
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

    @Transactional
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

        return user;
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        existsByIdOrThrow(id);
        userRepository.deleteById(id);
    }

    @Override
    public void existsByIdOrThrow(long id) {
        if (!userRepository.existsById(id)) {
            throw ExceptionUtils.getUserNotFoundException(id);
        }
    }

}
