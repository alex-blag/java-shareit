package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static Request toRequest(RequestPostDto requestPostDto) {
        Request request = new Request();
        if (requestPostDto != null) {
            request.setDescription(requestPostDto.getDescription());
        }
        request.setRequester(UserMapper.getUser());
        return request;
    }

    public static RequestDto toRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        if (request != null) {
            requestDto.setId(request.getId());
            requestDto.setDescription(request.getDescription());
            requestDto.setRequester(UserMapper.toUserDto(request.getRequester()));
            requestDto.setCreated(request.getCreated());
            requestDto.setItems(ItemMapper.toItemsDto(request.getItems()));
        }
        return requestDto;
    }

    public static List<RequestDto> toRequestsDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors
                        .toList());
    }

}
