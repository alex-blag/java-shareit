package ru.practicum.shareit.server.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.exception.ExceptionUtils;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.repository.RequestRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final UserService userService;

    private final ItemService itemService;

    @Transactional
    @Override
    public Request save(Request entity) {
        long requesterId = entity.getRequester().getId();
        User requester = userService.findById(requesterId);
        entity.setRequester(requester);
        entity.setCreated(LocalDateTime.now());

        return requestRepository.save(entity);
    }

    @Override
    public List<Request> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request findById(long id) {
        return requestRepository
                .findById(id)
                .orElseThrow(() -> ExceptionUtils.getRequestNotFoundException(id));
    }

    @Override
    public Request update(Request entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Request> findAllByRequesterId(long requesterId, Pageable pageable) {
        userExistsOrThrow(requesterId);

        List<Request> requests = requestRepository.findAllByRequesterId(requesterId, pageable);
        addItemsToRequests(requests);
        return requests;
    }

    @Override
    public Request findByIdIfUserExists(long requestId, long userId) {
        userExistsOrThrow(userId);

        Request request = findById(requestId);
        addItemsToRequest(request);
        return request;
    }

    @Override
    public List<Request> findAllByUserId(long userId, Pageable pageable) {
        userExistsOrThrow(userId);

        List<Request> requests = requestRepository.findAllByRequesterIdNot(userId, pageable);
        addItemsToRequests(requests);
        return requests;
    }

    private void addItemsToRequest(Request request) {
        addItemsToRequests(List.of(request));
    }

    private void addItemsToRequests(List<Request> requests) {
        List<Long> requestIds = requests
                .stream()
                .map(Request::getId)
                .collect(toList());

        Map<Long, List<Item>> requestIdToItems = itemService.findAllByRequestIdIn(requestIds)
                .stream()
                .collect(groupingBy(Item::getRequestId, toList()));

        requests.forEach(request -> {
            List<Item> items = requestIdToItems.getOrDefault(request.getId(), List.of());
            request.setItems(items);
        });
    }

    private void userExistsOrThrow(long userId) {
        userService.existsByIdOrThrow(userId);
    }

}
