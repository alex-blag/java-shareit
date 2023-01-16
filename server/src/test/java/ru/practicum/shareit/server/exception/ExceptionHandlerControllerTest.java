package ru.practicum.shareit.server.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.booking.BookingController;
import ru.practicum.shareit.server.booking.dto.BookingPostDto;
import ru.practicum.shareit.server.common.TestUtils;
import ru.practicum.shareit.server.exception.ExceptionHandlerController;
import ru.practicum.shareit.server.exception.ExceptionUtils;
import ru.practicum.shareit.server.item.ItemController;
import ru.practicum.shareit.server.item.dto.CommentPostDto;
import ru.practicum.shareit.server.item.dto.ItemPostDto;
import ru.practicum.shareit.server.request.RequestController;
import ru.practicum.shareit.server.user.UserController;
import ru.practicum.shareit.server.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionHandlerController.class)
class ExceptionHandlerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookingController bookingController;

    @MockBean
    ItemController itemController;

    @MockBean
    RequestController requestController;

    @MockBean
    UserController userController;

    @Test
    void handle_givenUserNotFoundException_expectCorrect() throws Exception {
        when(userController.get(anyLong()))
                .thenThrow(ExceptionUtils.getUserNotFoundException(0));

        mockMvc.perform(get("/users/{userId}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void handle_givenEmailAlreadyExistsException_expectCorrect() throws Exception {
        UserDto dto = TestUtils.getUser1Dto();

        when(userController.post(any(UserDto.class)))
                .thenThrow(ExceptionUtils.getEmailAlreadyExistsException(dto.getEmail()));

        mockMvc.perform(
                        post("/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isConflict());
    }

    @Test
    void handle_givenUserNotOwnItemException_expectCorrect() throws Exception {
        ItemPostDto dto = TestUtils.getItem1PostDto();

        when(itemController.patch(anyLong(), anyLong(), any(ItemPostDto.class)))
                .thenThrow(ExceptionUtils.getUserNotOwnItemException(0, 0));

        mockMvc.perform(
                        patch("/items/{itemId}", 0)
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void handle_givenUserNotRelatedWithBookingException_expectCorrect() throws Exception {
        when(bookingController.getByIdAndOwnerIdOrBookerId(anyLong(), anyLong()))
                .thenThrow(ExceptionUtils.getUserNotRelatedWithBookingException(0, 0));

        mockMvc.perform(
                        get("/bookings/{bookingId}", 0)
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void handle_givenUserNotBookedItemException_expectCorrect() throws Exception {
        CommentPostDto dto = TestUtils.getComment1PostDto();

        when(itemController.postComment(anyLong(), anyLong(), any(CommentPostDto.class)))
                .thenThrow(ExceptionUtils.getUserNotBookedItemException(0, 0));

        mockMvc.perform(
                        post("/items/{itemId}/comment", 0)
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void handle_givenItemNotFoundException_expectCorrect() throws Exception {
        when(itemController.getByIdAndUserId(anyLong(), anyLong()))
                .thenThrow(ExceptionUtils.getItemNotFoundException(0));

        mockMvc.perform(
                        get("/items/{itemId}", 0)
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void handle_givenItemNotAvailableException_expectCorrect() throws Exception {
        BookingPostDto dto = TestUtils.getBooking1PostDto();

        when(bookingController.post(anyLong(), any(BookingPostDto.class)))
                .thenThrow(ExceptionUtils.getItemNotAvailableException(0));

        mockMvc.perform(
                        post("/bookings")
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void handle_givenBookingNotFoundException_expectCorrect() throws Exception {
        when(bookingController.getByIdAndOwnerIdOrBookerId(anyLong(), anyLong()))
                .thenThrow(ExceptionUtils.getBookingNotFoundException(0));

        mockMvc.perform(
                        get("/bookings/{bookingId}", 0)
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void handle_givenBookingAlreadyApprovedException_expectCorrect() throws Exception {
        when(bookingController.patch(anyLong(), anyBoolean(), anyLong()))
                .thenThrow(ExceptionUtils.getBookingAlreadyApprovedException(0));

        mockMvc.perform(
                        patch("/bookings/{bookingId}", 0)
                                .param("approved", "true")
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void handle_givenBookerOwnsItemException_expectCorrect() throws Exception {
        BookingPostDto dto = TestUtils.getBooking1PostDto();

        when(bookingController.post(anyLong(), any(BookingPostDto.class)))
                .thenThrow(ExceptionUtils.getBookerOwnsItemException(0, 0));

        mockMvc.perform(
                        post("/bookings")
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void handle_givenRequestNotFoundException_expectCorrect() throws Exception {
        when(requestController.getAllByUserId(anyInt(), anyInt(), anyLong()))
                .thenThrow(ExceptionUtils.getRequestNotFoundException(0));

        mockMvc.perform(
                        get("/requests/all")
                                .header(TestUtils.X_SHARER_USER_ID, 0)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

}