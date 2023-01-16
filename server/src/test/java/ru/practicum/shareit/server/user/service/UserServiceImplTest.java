package ru.practicum.shareit.server.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.common.TestUtils;
import ru.practicum.shareit.server.exception.UserNotFoundException;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void save_givenUser_expectCorrect() {
        User user1 = TestUtils.getUser1();

        when(userRepository.save(user1)).thenReturn(user1);

        User savedUser1 = userServiceImpl.save(user1);

        assertEquals(user1, savedUser1);
    }

    @Test
    void findAll_givenUsers_expectCorrectUsers() {
        List<User> users = List.of(TestUtils.getUser1());

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userServiceImpl.findAll();

        assertEquals(users, foundUsers);
    }

    @Test
    void findById_givenExistingUserId_expectActualUser() {
        User user = TestUtils.getUser1();
        long userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userServiceImpl.findById(userId);

        assertEquals(user, foundUser);
    }

    @Test
    void findById_givenNonExistingUserId_expectUserNotFoundException() {
        long userId = TestUtils.USER1_ID;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findById(userId));
    }

    @Test
    void update_givenExistingUserAndNameUpdate_expectUpdatedUserName() {
        User userNameUpdate = TestUtils.getUser1NameUpdate();
        long userId = userNameUpdate.getId();
        User foundUser = TestUtils.getUser1();

        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));

        User updatedUser = userServiceImpl.update(userNameUpdate);

        assertEquals(userNameUpdate.getName(), updatedUser.getName());
    }

    @Test
    void update_givenExistingUserAndEmailUpdate_expectUpdatedUserEmail() {
        User userEmailUpdate = TestUtils.getUser1EmailUpdate();
        long userId = userEmailUpdate.getId();
        User foundUser = TestUtils.getUser1();

        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));

        User updatedUser = userServiceImpl.update(userEmailUpdate);

        assertEquals(userEmailUpdate.getEmail(), updatedUser.getEmail());
    }

    @Test
    void update_givenNonExistingUser_expectUserNotFoundException() {
        User user = TestUtils.getUser1NameUpdate();
        long userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.update(user));
    }

    @Test
    void deleteById_givenExistingUser_expectCorrect() {
        long userId = TestUtils.USER1_ID;

        when(userRepository.existsById(userId)).thenReturn(true);

        userServiceImpl.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteById_givenNonExistingUser_expectUserNotFoundException() {
        long userId = TestUtils.USER1_ID;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.deleteById(userId));
    }

    @Test
    void existsByIdOrThrow_givenExistingUser_expectCorrect() {
        long userId = TestUtils.USER1_ID;

        when(userRepository.existsById(userId)).thenReturn(true);

        userServiceImpl.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void existsByIdOrThrow_givenNonExistingUser_expectUserNotFoundException() {
        long userId = TestUtils.USER1_ID;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.existsByIdOrThrow(userId));
    }

}