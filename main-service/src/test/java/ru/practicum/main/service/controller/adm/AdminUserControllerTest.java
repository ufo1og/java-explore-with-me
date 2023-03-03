package ru.practicum.main.service.controller.adm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.main.service.dto.NewUserRequest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminUserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    @DisplayName("Test creating 10 new Users")
    public void test10() throws Exception {
        for (long i = 1; i <= 10; i++) {
            NewUserRequest userRequest = new NewUserRequest("user" + i + "@ya.ru", "user" + i);

            mvc.perform(post("/admin/users")
                            .content(mapper.writeValueAsString(userRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(i), Long.class))
                    .andExpect(jsonPath("$.email", is(userRequest.getEmail())))
                    .andExpect(jsonPath("$.name", is(userRequest.getName())));
        }
    }

    @Test
    @Order(2)
    @DisplayName("Test creating User with empty email")
    public void test20() throws Exception {
        NewUserRequest userRequest = new NewUserRequest(null, "name");

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    @DisplayName("Test creating User with empty name")
    public void test21() throws Exception {
        NewUserRequest userRequest = new NewUserRequest("user@ya.ru", null);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    @DisplayName("Test creating User with invalid email")
    public void test22() throws Exception {
        NewUserRequest userRequest = new NewUserRequest("email", "name");

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        userRequest.setEmail("ya.ru");

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        userRequest.setEmail("user@ya");

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("Getting all Users with default page params")
    public void test30() throws Exception {
        mvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(10)))
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].email", is("user1@ya.ru")))
                .andExpect(jsonPath("$[0].name", is("user1")));
    }

    @Test
    @Order(3)
    @DisplayName("Getting all Users with from = 1 and to = 5")
    public void test31() throws Exception {
        mvc.perform(get("/admin/users")
                        .param("from", "1")
                        .param("to", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(5)))
                .andExpect(jsonPath("$[0].id", is(6L), Long.class));
    }

    @Test
    @Order(3)
    @DisplayName("Getting all Users with from = -1")
    public void test32() throws Exception {
        mvc.perform(get("/admin/users")
                        .param("from", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("Getting all Users with to = -1")
    public void test33() throws Exception {
        mvc.perform(get("/admin/users")
                        .param("to", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("Getting Users with ids")
    public void test34() throws Exception {
        mvc.perform(get("/admin/users")
                        .param("ids", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(content().string(containsString("user1")))
                .andExpect(content().string(containsString("user1@ya.ru")))
                .andExpect(content().string(containsString("user2")))
                .andExpect(content().string(containsString("user2@ya.ru")));
    }

    @Test
    @Order(3)
    @DisplayName("Getting Users with ids that not exists")
    public void test35() throws Exception {
        mvc.perform(get("/admin/users")
                        .param("ids", "777", "888"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @Order(4)
    @DisplayName("Test deleting User that not exists")
    public void test40() throws Exception {
        mvc.perform(delete("/admin/users/777"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    @DisplayName("Test deleting User")
    public void test41() throws Exception {
        mvc.perform(delete("/admin/users/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    @DisplayName("Test getting all Users without user10")
    public void test50() throws Exception {
        mvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(9)));
    }
}
