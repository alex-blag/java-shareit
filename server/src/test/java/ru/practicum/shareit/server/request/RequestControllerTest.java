package ru.practicum.shareit.server.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.common.TestUtils;
import ru.practicum.shareit.server.request.dto.RequestPostDto;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.service.RequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RequestService requestService;

    @Test
    void post_givenRequestPostDto_expectCorrect() throws Exception {
        RequestPostDto requestPostDto = TestUtils.getRequestPostDto();
        Request savedRequest = TestUtils.getRequest1();

        when(requestService.save(any(Request.class))).thenReturn(savedRequest);

        mockMvc.perform(
                        post("/requests")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestPostDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRequest.getId()))
                .andExpect(jsonPath("$.description").value(savedRequest.getDescription()));
    }

    @Test
    void getAllByRequesterId_givenRequesterId_expectCorrect() throws Exception {
        Request foundRequest = TestUtils.getRequest1();
        List<Request> foundRequests = List.of(foundRequest);

        when(requestService.findAllByRequesterId(anyLong(), any(Pageable.class)))
                .thenReturn(foundRequests);

        mockMvc.perform(
                        get("/requests")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(foundRequests.get(0).getId()))
                .andExpect(jsonPath("$[0].description").value(foundRequests.get(0).getDescription()));
    }

    @Test
    void getAllByUserId_givenUserId_expectCorrect() throws Exception {
        Request foundRequest = TestUtils.getRequest1();
        List<Request> foundRequests = List.of(foundRequest);

        when(requestService.findAllByUserId(anyLong(), any(Pageable.class)))
                .thenReturn(foundRequests);

        mockMvc.perform(
                        get("/requests/all")
                                .param("from", "0")
                                .param("size", "1")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(foundRequests.get(0).getId()))
                .andExpect(jsonPath("$[0].description").value(foundRequests.get(0).getDescription()));
    }

    @Test
    void getById_givenRequestId_expectCorrect() throws Exception {
        Request foundRequest = TestUtils.getRequest1();

        when(requestService.findByIdIfUserExists(anyLong(), anyLong()))
                .thenReturn(foundRequest);

        mockMvc.perform(
                        get("/requests/{requestId}", TestUtils.REQUEST1_ID)
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(foundRequest.getId()))
                .andExpect(jsonPath("$.description").value(foundRequest.getDescription()));
    }

}
