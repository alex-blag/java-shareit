package ru.practicum.shareit.server.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.server.common.TestUtils;
import ru.practicum.shareit.server.exception.RequestNotFoundException;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.model.RequestPager;
import ru.practicum.shareit.server.request.repository.RequestRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    RequestRepository requestRepository;

    @Mock
    UserService userService;

    @Mock
    ItemService itemService;

    @InjectMocks
    RequestServiceImpl requestServiceImpl;

    @Test
    void save_givenRequest_expectCorrect() {
        Request request1WithoutId = TestUtils.getRequest1WithoutId();

        User requester = request1WithoutId.getRequester();
        when(userService.findById(anyLong())).thenReturn(requester);

        Request request1 = TestUtils.getRequest1();
        when(requestRepository.save(any(Request.class))).thenReturn(request1);

        Request savedRequest = requestServiceImpl.save(request1WithoutId);
        assertEquals(request1, savedRequest);
    }

    @Test
    void findAll_expectUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> requestServiceImpl.findAll());
    }

    @Test
    void findById_givenExistingRequestId_expectCorrect() {
        Request request = TestUtils.getRequest1();
        long requestId = request.getId();

        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        Request foundRequest = requestServiceImpl.findById(requestId);
        assertEquals(request, foundRequest);
    }

    @Test
    void findById_givenNonExistingRequestId_expectRequestNotFoundException() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> requestServiceImpl.findById(0L));
    }

    @Test
    void update_expectUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> requestServiceImpl.update(null));
    }

    @Test
    void deleteById_expectUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> requestServiceImpl.deleteById(0L));
    }

    @Test
    void findAllByRequesterId_givenRequesterId_expectCorrect() {
        Request request = TestUtils.getRequest1();
        List<Item> items = List.of(TestUtils.getRequest1Item());
        request.setItems(items);
        List<Request> requests = List.of(request);

        long requesterId = request.getRequester().getId();

        doNothing().when(userService).existsByIdOrThrow(requesterId);

        when(requestRepository.findAllByRequesterId(anyLong(), any(Pageable.class))).thenReturn(requests);

        when(itemService.findAllByRequestIdIn(anyList())).thenReturn(items);

        List<Request> foundRequests = requestServiceImpl.findAllByRequesterId(requesterId, RequestPager.byCreatedDesc());
        assertEquals(requests, foundRequests);
    }

    @Test
    void findByIdIfUserExists_givenRequestIdAndUserId_expectCorrect() {
        Request request = TestUtils.getRequest1();
        List<Item> items = List.of(TestUtils.getRequest1Item());
        request.setItems(items);

        long requestId = request.getId();
        long userId = request.getRequester().getId();

        doNothing().when(userService).existsByIdOrThrow(userId);

        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        when(itemService.findAllByRequestIdIn(anyList())).thenReturn(items);

        Request foundRequest = requestServiceImpl.findByIdIfUserExists(requestId, userId);
        assertEquals(request, foundRequest);
    }

    @Test
    void findAllByUserId() {
        Request request = TestUtils.getRequest1();
        List<Item> items = List.of(TestUtils.getRequest1Item());
        request.setItems(items);
        List<Request> requests = List.of(request);

        long userId = request.getRequester().getId();

        doNothing().when(userService).existsByIdOrThrow(userId);

        when(requestRepository.findAllByRequesterIdNot(anyLong(), any(Pageable.class))).thenReturn(requests);

        when(itemService.findAllByRequestIdIn(anyList())).thenReturn(items);

        List<Request> foundRequests = requestServiceImpl.findAllByUserId(userId, RequestPager.byCreatedDesc());
        assertEquals(requests, foundRequests);
    }

}