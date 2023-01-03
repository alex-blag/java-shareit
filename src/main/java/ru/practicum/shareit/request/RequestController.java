package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.CommonUtils;
import ru.practicum.shareit.common.validation.Post;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestPostDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestPager;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.common.CommonUtils.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private static final String MAX_SIZE = CommonUtils.INTEGER_MAX_VALUE;

    private final RequestService requestService;

    @PostMapping
    public RequestDto post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Validated(Post.class) @RequestBody RequestPostDto requestPostDto
    ) {
        Request request = toRequest(requestPostDto);
        request.getRequester().setId(userId);

        Request savedRequest = requestService.save(request);
        return toRequestDto(savedRequest);
    }

    @GetMapping
    public List<RequestDto> getAllByRequesterId(
            @RequestHeader(X_SHARER_USER_ID) long requesterId
    ) {
        Pageable pageable = RequestPager.byCreatedDesc();
        List<Request> requests = requestService.findAllByRequesterId(requesterId, pageable);
        return toRequestsDto(requests);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllByUserId(
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Pageable pageable = RequestPager.byCreatedDesc(from / size, size);
        List<Request> requests = requestService.findAllByUserId(userId, pageable);
        return toRequestsDto(requests);
    }

    @GetMapping("/{requestId}")
    public RequestDto getById(
            @PathVariable long requestId,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Request request = requestService.findByIdIfUserExists(requestId, userId);
        return toRequestDto(request);
    }

    private RequestDto toRequestDto(Request request) {
        return RequestMapper.toRequestDto(request);
    }

    private List<RequestDto> toRequestsDto(List<Request> requests) {
        return RequestMapper.toRequestsDto(requests);
    }

    private Request toRequest(RequestPostDto requestPostDto) {
        return RequestMapper.toRequest(requestPostDto);
    }

}
