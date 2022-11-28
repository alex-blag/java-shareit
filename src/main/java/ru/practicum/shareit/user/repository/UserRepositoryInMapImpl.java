package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExceptionUtils;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.practicum.shareit.common.CommonUtils.isStringNotBlank;

@Repository
public class UserRepositoryInMapImpl implements UserRepository {

    private final Map<Long, User> idToUser;

    private long id;

    public UserRepositoryInMapImpl() {
        this.idToUser = new HashMap<>();
        this.id = 1;
    }

    @Override
    public User create(User obj) {
        checkUserEmailExistence(obj.getId(), obj.getEmail());

        long userId = generateId();
        obj.setId(userId);
        idToUser.put(userId, obj);

        return obj;
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(idToUser.values());
    }

    @Override
    public Optional<User> read(long id) {
        return Optional.ofNullable(idToUser.get(id));
    }

    @Override
    public User update(User obj) {
        long userId = obj.getId();
        User user = idToUser.get(userId);

        String newName = obj.getName();
        if (isStringNotBlank(newName)) {
            user.setName(newName);
        }

        String newEmail = obj.getEmail();
        if (isStringNotBlank(newEmail)) {
            checkUserEmailExistence(userId, newEmail);
            user.setEmail(newEmail);
        }

        return user;
    }

    @Override
    public void destroy(long id) {
        idToUser.remove(id);
    }

    private void checkUserEmailExistence(Long userId, String email) {
        if (isUserEmailAlreadyExists(userId, email)) {
            throw ExceptionUtils.getEmailAlreadyExistsException(email);
        }
    }

    private boolean isUserEmailAlreadyExists(Long userId, String email) {
        return idToUser.values().stream()
                .anyMatch(user
                        -> user.getEmail().equals(email)
                        && !user.getId().equals(userId)
                );
    }

    private long generateId() {
        return id++;
    }

}
