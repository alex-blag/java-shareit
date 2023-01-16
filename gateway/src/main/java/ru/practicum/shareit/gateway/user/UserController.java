package ru.practicum.shareit.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.gateway.user.dto.UserPatchDto;
import ru.practicum.shareit.gateway.user.dto.UserPostDto;

import javax.validation.Valid;

import static ru.practicum.shareit.gateway.common.CommonUtils.USERS_RESOURCE;

@Slf4j
@Controller
@RequestMapping(path = USERS_RESOURCE)
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> post(
            @RequestBody @Valid UserPostDto userPostDto
    ) {
        log.info("post({})", userPostDto);

        return userClient.post(userPostDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("getAll()");

        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(
            @PathVariable long userId
    ) {
        log.info("getById(userId = {})", userId);

        return userClient.getById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchById(
            @PathVariable long userId,
            @RequestBody @Valid UserPatchDto userPatchDto
    ) {
        log.info("patchById(userId = {}, {})", userId, userPatchDto);

        return userClient.patchById(userId, userPatchDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(
            @PathVariable long userId
    ) {
        log.info("deleteById(userId = {})", userId);

        return userClient.deleteById(userId);
    }

}
