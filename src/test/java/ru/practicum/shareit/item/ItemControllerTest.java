package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.TestUtils;
import ru.practicum.shareit.item.dto.CommentPostDto;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemService itemService;

    @Test
    void post_whenSavedItem_expectCorrectItem() throws Exception {
        ItemPostDto item1PostDto = TestUtils.getItem1PostDto();
        Item item1Saved = TestUtils.getItem1();

        when(itemService.save(any(Item.class))).thenReturn(item1Saved);

        mockMvc.perform(
                        post("/items")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(item1PostDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item1Saved.getId()))
                .andExpect(jsonPath("$.name").value(item1Saved.getName()))
                .andExpect(jsonPath("$.description").value(item1Saved.getDescription()))
                .andExpect(jsonPath("$.available").value(item1Saved.getAvailable()))
                .andExpect(jsonPath("$.ownerId").value(item1Saved.getOwnerId()));
    }

    @Test
    void patch_whenUpdatedItem_expectCorrectItem() throws Exception {
        ItemPostDto item1NameUpdatePostDto = TestUtils.getItem1NameUpdatePostDto();
        Item item1Updated = TestUtils.getItem1();

        when(itemService.update(any(Item.class))).thenReturn(item1Updated);

        mockMvc.perform(
                        patch("/items/{itemId}", TestUtils.ITEM1_ID)
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(item1NameUpdatePostDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item1Updated.getId()))
                .andExpect(jsonPath("$.name").value(item1Updated.getName()))
                .andExpect(jsonPath("$.description").value(item1Updated.getDescription()))
                .andExpect(jsonPath("$.available").value(item1Updated.getAvailable()))
                .andExpect(jsonPath("$.ownerId").value(item1Updated.getOwnerId()));
    }

    @Test
    void getByIdAndUserId_whenFoundItems_expectCorrectItems() throws Exception {
        Item item1Found = TestUtils.getItem1();

        when(itemService.findByIdAndUserId(TestUtils.ITEM1_ID, TestUtils.USER1_ID)).thenReturn(item1Found);

        mockMvc.perform(
                        get("/items/{itemId}", TestUtils.ITEM1_ID)
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item1Found.getId()))
                .andExpect(jsonPath("$.name").value(item1Found.getName()))
                .andExpect(jsonPath("$.description").value(item1Found.getDescription()))
                .andExpect(jsonPath("$.available").value(item1Found.getAvailable()))
                .andExpect(jsonPath("$.ownerId").value(item1Found.getOwnerId()));
    }

    @Test
    void getAllByUserId_whenFoundItems_expectCorrectItems() throws Exception {
        Item item1Found = TestUtils.getItem1();

        when(itemService.findAllByOwnerId(eq(TestUtils.USER1_ID), any(Pageable.class))).thenReturn(List.of(item1Found));

        mockMvc.perform(
                        get("/items")
                                .param("from", "0")
                                .param("size", "1")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(item1Found.getId()))
                .andExpect(jsonPath("$[0].name").value(item1Found.getName()))
                .andExpect(jsonPath("$[0].description").value(item1Found.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item1Found.getAvailable()))
                .andExpect(jsonPath("$[0].ownerId").value(item1Found.getOwnerId()));
    }

    @Test
    void getAllByNameOrDescriptionContaining_whenFoundItems_expectCorrectItems() throws Exception {
        Item item1Found = TestUtils.getItem1();

        when(
                itemService.findAllByNameOrDescriptionContaining(
                        eq(TestUtils.USER1_ID),
                        eq("ite"),
                        any(Pageable.class)
                )
        )
                .thenReturn(List.of(item1Found));

        mockMvc.perform(
                        get("/items/search")
                                .param("from", "0")
                                .param("size", "1")
                                .param("text", "ite")
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(item1Found.getId()))
                .andExpect(jsonPath("$[0].name").value(item1Found.getName()))
                .andExpect(jsonPath("$[0].description").value(item1Found.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item1Found.getAvailable()))
                .andExpect(jsonPath("$[0].ownerId").value(item1Found.getOwnerId()));
    }

    @Test
    void postComment_whenSavedComment_expectCorrectComment() throws Exception {
        CommentPostDto comment1PostDto = TestUtils.getComment1PostDto();
        Comment comment1Saved = TestUtils.getItem1Comment();

        when(itemService.save(any(Comment.class))).thenReturn(comment1Saved);

        mockMvc.perform(
                        post("/items/{itemId}/comment", TestUtils.ITEM1_ID)
                                .header(TestUtils.X_SHARER_USER_ID, TestUtils.USER1_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(comment1PostDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment1Saved.getId()))
                .andExpect(jsonPath("$.text").value(comment1Saved.getText()))
                .andExpect(jsonPath("$.authorName").value(comment1Saved.getAuthor().getName()));
    }

}