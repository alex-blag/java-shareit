package ru.practicum.shareit.server.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        if (user != null) {
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
        }
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static List<UserDto> toUsersDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors
                        .toList());
    }

    public static User getUser() {
        return new User();
    }

}
