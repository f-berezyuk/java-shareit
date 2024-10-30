package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        // This is required to initialize context. In real test, controller would be autowired.
    }

    @Test
    void testUserNotFoundException() throws Exception {
        mockMvc.perform(get("/test/user-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Пользователя не существует."));
    }

    @Test
    void testBookingNotFoundException() throws Exception {
        mockMvc.perform(get("/test/booking-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Бронирования не существует."));
    }

    @Test
    void testItemNotFoundException() throws Exception {
        mockMvc.perform(get("/test/item-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Предмет не существует."));
    }

    @Test
    void testUserNotAuthorizedException() throws Exception {
        mockMvc.perform(get("/test/user-not-authorized"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Доступ запрещён."));
    }

    @Test
    void testUserEmailDuplicateException() throws Exception {
        mockMvc.perform(get("/test/user-email-duplicate"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Ошибка регистрации: "));
    }

    @Test
    void testIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ошибка ввода: "));
    }

    @Test
    void testRuntimeException() throws Exception {
        mockMvc.perform(get("/test/runtime-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Ошибка сервера: "));
    }
}