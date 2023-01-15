package ru.practicum.shareit.gateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.gateway.request.dto.RequestPostDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.gateway.common.CommonUtils.ALL_RESOURCE;
import static ru.practicum.shareit.gateway.common.CommonUtils.MAX_SIZE;
import static ru.practicum.shareit.gateway.common.CommonUtils.REQUESTS_RESOURCE;
import static ru.practicum.shareit.gateway.common.CommonUtils.X_SHARER_USER_ID;

@Slf4j
@Controller
@RequestMapping(path = REQUESTS_RESOURCE)
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody @Valid RequestPostDto requestPostDto
    ) {
        log.info("post: userId = {}, {}", userId, requestPostDto);

        return requestClient.post(userId, requestPostDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequesterId(
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        log.info("getAllByRequesterId: userId = {}", userId);

        return requestClient.getAllByRequesterId(userId);
    }

    @GetMapping(ALL_RESOURCE)
    public ResponseEntity<Object> getAllByUserId(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size
    ) {
        log.info("getAllByUserId: userId = {}, from = {}, size = {}", userId, from, size);

        return requestClient.getAllByUserId(ALL_RESOURCE, userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @PathVariable long requestId
    ) {
        log.info("getById: userId = {}, requestId = {}", userId, requestId);

        return requestClient.getById(userId, requestId);
    }

}
