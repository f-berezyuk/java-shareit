package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private Long userId;
    private Long bookingId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        bookingId = 1L;
    }

    @Test
    void testGetBooking_ThrowsItemNotFoundException() throws Exception {
        doThrow(new ItemNotFoundException("Предмет не найден."))
                .when(bookingService).getBooking(userId, bookingId);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Предмет не существует."))
                .andExpect(jsonPath("$.description").value("Предмет не найден."));
    }

    @Test
    void testGetBooking_ThrowsBookingNotFoundException() throws Exception {
        doThrow(new BookingNotFoundException("Бронирование не найдено."))
                .when(bookingService).getBooking(userId, bookingId);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Бронирования не существует."));
    }

    @Test
    void testApproveBooking_ThrowsUserNotAuthorizedException() throws Exception {
        doThrow(new UserNotAuthorizedException("Пользователь не является владельцем предмета."))
                .when(bookingService).updateStatus(userId, bookingId, true);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Доступ запрещён."));
    }
}