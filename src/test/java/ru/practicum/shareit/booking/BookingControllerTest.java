package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.TestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookingService bookingService;

    @Test
    void post_whenSavedBooking_expectCorrectBooking() throws Exception {
        BookingPostDto booking1PostDto = TestUtils.getBooking1PostDto();
        Booking booking1Saved = TestUtils.getBooking1();

        when(bookingService.save(any(Booking.class))).thenReturn(booking1Saved);

        mockMvc.perform(
                        post("/bookings")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(booking1PostDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking1Saved.getId()))
                .andExpect(jsonPath("$.item.id").value(booking1Saved.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(booking1Saved.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(booking1Saved.getStatus().name()));
    }

    @Test
    void patch_whenUpdatedBooking_expectCorrectBooking() throws Exception {
        Booking booking1Saved = TestUtils.getBooking1Approved();

        when(bookingService.update(any(Booking.class))).thenReturn(booking1Saved);

        mockMvc.perform(
                        patch("/bookings/{bookingId}", TestUtils.BOOKING1_ID)
                                .param("approved", "true")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking1Saved.getId()))
                .andExpect(jsonPath("$.item.id").value(booking1Saved.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(booking1Saved.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(booking1Saved.getStatus().name()));
    }

    @Test
    void getByIdAndOwnerIdOrBookerId() throws Exception {
        Booking booking1 = TestUtils.getBooking1();

        when(bookingService.findByIdAndOwnerIdOrBookerId(eq(TestUtils.BOOKING1_ID), eq(TestUtils.USER1_ID)))
                .thenReturn(booking1);

        mockMvc.perform(
                        get("/bookings/{bookingId}", TestUtils.BOOKING1_ID)
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking1.getId()))
                .andExpect(jsonPath("$.item.id").value(booking1.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(booking1.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(booking1.getStatus().name()));
    }

    @Test
    void getAllByBookerIdAndState_whenFoundBooking_expectCorrectBookings() throws Exception {
        Booking booking1 = TestUtils.getBooking1();

        when(bookingService.findAllByBookerIdAndState(anyLong(), any(State.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));

        mockMvc.perform(
                        get("/bookings")
                                .param("state", "ALL")
                                .param("from", "0")
                                .param("size", "1")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(booking1.getId()))
                .andExpect(jsonPath("$[0].item.id").value(booking1.getItem().getId()))
                .andExpect(jsonPath("$[0].booker.id").value(booking1.getBooker().getId()))
                .andExpect(jsonPath("$[0].status").value(booking1.getStatus().name()));
    }

    @Test
    void getAllByOwnerIdAndState_whenFoundBooking_expectCorrectBooking() throws Exception {
        Booking booking1 = TestUtils.getBooking1();

        when(bookingService.findAllByOwnerIdAndState(anyLong(), any(State.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));

        mockMvc.perform(
                        get("/bookings/owner")
                                .param("state", "ALL")
                                .param("from", "0")
                                .param("size", "1")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(booking1.getId()))
                .andExpect(jsonPath("$[0].item.id").value(booking1.getItem().getId()))
                .andExpect(jsonPath("$[0].booker.id").value(booking1.getBooker().getId()))
                .andExpect(jsonPath("$[0].status").value(booking1.getStatus().name()));
    }

}