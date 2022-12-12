package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.validation.Patch;
import ru.practicum.shareit.common.validation.Post;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto post(
            @Validated(Post.class) @RequestBody UserDto userDto
    ) {
        User user = userService.save(toUser(userDto));
        return toUserDto(user);
    }

    @GetMapping
    public List<UserDto> getAll() {
        List<User> users = userService.findAll();
        return toUsersDto(users);
    }

    @GetMapping("/{userId}")
    public UserDto get(
            @PathVariable long userId
    ) {
        User user = userService.findById(userId);
        return toUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto patch(
            @PathVariable long userId,
            @Validated(Patch.class) @RequestBody UserDto userDto
    ) {
        userDto.setId(userId);
        User user = userService.update(toUser(userDto));
        return toUserDto(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(
            @PathVariable long userId
    ) {
        userService.deleteById(userId);
    }

    private User toUser(UserDto userDto) {
        return UserMapper.toUser(userDto);
    }

    private UserDto toUserDto(User user) {
        return UserMapper.toUserDto(user);
    }

    private List<UserDto> toUsersDto(List<User> users) {
        return UserMapper.toUsersDto(users);
    }

}
