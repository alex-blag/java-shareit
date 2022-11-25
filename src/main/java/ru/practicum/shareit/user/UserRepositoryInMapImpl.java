package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        checkEmailExistence(obj.getEmail());

        long userId = generateUserId();
        obj.setId(userId);
        idToUser.put(userId, obj);

        return idToUser.get(userId);
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
        User user = new User(idToUser.get(userId));

        String newName = obj.getName();
        if (newName != null) {
            user.setName(newName);
        }

        String newEmail = obj.getEmail();
        if (newEmail != null) {
            checkEmailExistence(newEmail);
            user.setEmail(newEmail);
        }

        idToUser.put(userId, user);

        return idToUser.get(userId);
    }

    @Override
    public void destroy(long id) {
        idToUser.remove(id);
    }

    private void checkEmailExistence(String email) throws EmailAlreadyExistsException {
        if (isEmailAlreadyExists(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

    private boolean isEmailAlreadyExists(String email) {
        return idToUser.values().stream()
                .map(User::getEmail)
                .anyMatch(em -> em.equals(email));
    }

    private long generateUserId() {
        return id++;
    }

}
