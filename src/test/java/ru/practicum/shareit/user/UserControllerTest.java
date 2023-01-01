package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.TestUtils;
import ru.practicum.shareit.exception.ExceptionUtils;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void post_whenSavedUser_expectUser() throws Exception {
        UserDto user1WithoutIdDto = TestUtils.getUser1WithoutIdDto();
        User user1Saved = TestUtils.getUser1();

        when(userService.save(any(User.class))).thenReturn(user1Saved);

        mockMvc.perform(
                        post("/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1WithoutIdDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1Saved.getId()))
                .andExpect(jsonPath("$.name").value(user1Saved.getName()))
                .andExpect(jsonPath("$.email").value(user1Saved.getEmail()));
    }

    @Test
    void getAll_whenFoundUsers_expectUsers() throws Exception {
        User user1Found = TestUtils.getUser1();

        when(userService.findAll()).thenReturn(List.of(user1Found));

        mockMvc.perform(
                        get("/users")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(user1Found.getId()))
                .andExpect(jsonPath("$[0].name").value(user1Found.getName()))
                .andExpect(jsonPath("$[0].email").value(user1Found.getEmail()));
    }

    @Test
    void get_whenFoundUserById_expectUser() throws Exception {
        User user1Found = TestUtils.getUser1();

        when(userService.findById(TestUtils.USER1_ID)).thenReturn(user1Found);

        mockMvc.perform(
                        get("/users/{userId}", TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1Found.getId()))
                .andExpect(jsonPath("$.name").value(user1Found.getName()))
                .andExpect(jsonPath("$.email").value(user1Found.getEmail()));
    }

    @Test
    void patch_whenUpdatedName_expectUser() throws Exception {
        UserDto user1NameUpdateDto = TestUtils.getUser1NameUpdateDto();
        User user1Updated = TestUtils.getUser1WithUpdatedName();

        when(userService.update(any(User.class))).thenReturn(user1Updated);

        mockMvc.perform(
                        patch("/users/{userId}", TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1NameUpdateDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1Updated.getId()))
                .andExpect(jsonPath("$.name").value(user1Updated.getName()))
                .andExpect(jsonPath("$.email").value(user1Updated.getEmail()));
    }

    @Test
    void delete_whenDeletedUserById_expectStatusOk() throws Exception {
        mockMvc.perform(
                        delete("/users/{userId}", TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(userService).deleteById(TestUtils.USER1_ID);
    }

    @Test
    void get_givenNonExistingUserId_expectException() throws Exception {
        long userId = 0L;

        when(userService.findById(userId))
                .thenThrow(ExceptionUtils.getUserNotFoundException(userId));

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isNotFound());
    }

}