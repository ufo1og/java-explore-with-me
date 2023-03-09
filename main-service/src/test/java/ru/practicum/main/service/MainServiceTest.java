package ru.practicum.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.main.service.dto.NewCategoryDto;
import ru.practicum.main.service.dto.NewEventDto;
import ru.practicum.main.service.dto.NewUserRequest;
import ru.practicum.main.service.model.Location;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MainServiceTest {
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
    @DisplayName("Getting all Users with from = 1 and size = 5")
    public void test31() throws Exception {
        mvc.perform(get("/admin/users")
                        .param("from", "1")
                        .param("size", "5"))
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
    @DisplayName("Getting all Users with size = -1")
    public void test33() throws Exception {
        mvc.perform(get("/admin/users")
                        .param("size", "-1"))
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

    @Test
    @Order(6)
    @DisplayName("Test creating 5 Categories")
    public void test60() throws Exception {
        List<NewCategoryDto> categoryDtos = List.of(
                new NewCategoryDto("Dance"),
                new NewCategoryDto("Tourism"),
                new NewCategoryDto("Art"),
                new NewCategoryDto("History"),
                new NewCategoryDto("Chatting")
        );

        for (int i = 1; i <= categoryDtos.size(); i++) {
            NewCategoryDto categoryDto = categoryDtos.get(i - 1);
            mvc.perform(post("/admin/categories")
                            .content(mapper.writeValueAsString(categoryDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is((long) i), Long.class))
                    .andExpect(jsonPath("$.name", is(categoryDto.getName())));
        }
    }

    @Test
    @Order(7)
    @DisplayName("Creating Category with empty name")
    public void test70() throws Exception {
        NewCategoryDto newCategoryDto = new NewCategoryDto();
        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @DisplayName("Creating Category with blank name")
    public void test71() throws Exception {
        NewCategoryDto newCategoryDto = new NewCategoryDto("");
        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @DisplayName("Creating Category with conflict name")
    public void test72() throws Exception {
        NewCategoryDto newCategoryDto = new NewCategoryDto("Conflict");
        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated());
        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(8)
    @DisplayName("Updating Category")
    public void test80() throws Exception {
        NewCategoryDto newCategoryDto = new NewCategoryDto("Updated");
        mvc.perform(patch("/admin/categories/6")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newCategoryDto.getName())));
    }

    @Test
    @Order(8)
    @DisplayName("Updating Category that not exists")
    public void test81() throws Exception {
        NewCategoryDto newCategoryDto = new NewCategoryDto("Updated");
        mvc.perform(patch("/admin/categories/777")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    @DisplayName("Deleting Category")
    public void test90() throws Exception {
        mvc.perform(delete("/admin/categories/6"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(9)
    @DisplayName("Deleting Category that not exists")
    public void test91() throws Exception {
        mvc.perform(delete("/admin/categories/777"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    @DisplayName("Getting all Categories with default page parameters")
    public void test100() throws Exception {
        mvc.perform(get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(5)));
    }

    @Test
    @Order(10)
    @DisplayName("Getting all Categories with from = 1 and size = 2")
    public void test101() throws Exception {
        mvc.perform(get("/categories")
                        .param("from", "1")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Dance")));
    }

    @Test
    @Order(10)
    @DisplayName("Getting all Categories with from = -1")
    public void test102() throws Exception {
        mvc.perform(get("/categories")
                        .param("from", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    @DisplayName("Getting all Categories with size = -1")
    public void test103() throws Exception {
        mvc.perform(get("/categories")
                        .param("size", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    @DisplayName("Getting Category")
    public void test104() throws Exception {
        mvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    @Order(10)
    @DisplayName("Getting Category")
    public void test105() throws Exception {
        mvc.perform(get("/categories/777")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(999)
    @DisplayName("Creating 5 Events")
    public void test999() throws Exception {
        for (long i = 1L; i <= 5L; i++) {
            String title = "event" + i;
            String annotation = "This is annotation of event" + i;
            String description = "This is description of event" + i;
            String eventDate = LocalDateTime.now().plusDays(i).format(TIMESTAMP_FORMATTER);
            NewEventDto newEventDto = new NewEventDto(
                    annotation,
                    i,
                    description,
                    eventDate,
                    new Location(55.45, 65.33),
                    true,
                    5,
                    true,
                    title
                    );
            mvc.perform(post(String.format("/users/%s/events", i))
                            .content(mapper.writeValueAsString(newEventDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(i), Long.class))
                    .andExpect(jsonPath("$.title", is(newEventDto.getTitle())));
        }
    }
}
