package ru.practicum.shareit.booking;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item testItem;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Создание пользователя и предмета как тестовых данных
        testUser = new User();
        testUser.setEmail("user@example.com");
        testUser.setName("Test User");
        testUser = userRepository.save(testUser);

        testItem = new Item();
        testItem.setName("Test Item");
        testItem.setDescription("A test item description");
        testItem.setAvailable(true);
        testItem.setOwnerId(testUser.getId());
        testItem = itemRepository.save(testItem);
    }

    @Test
    public void testFindAllByBookerId() {
        // Создание и сохранение бронирования
        Booking booking = Booking.builder()
                .item(testItem)
                .booker(testUser)
                .status(EBookingStatus.APPROVED)
                .startAt(Timestamp.from(Instant.now()))
                .endAt(Timestamp.from(Instant.now().plusSeconds(3600)))
                .build();

        bookingRepository.save(booking);

        // Вызов метода репозитория и проверка результата
        List<Booking> bookings = bookingRepository.findAllByBookerId(testUser.getId());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getBooker().getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testFindAllByBookerIdAndItemIdAndStatusAndEndAtBefore() {
        // Создание и сохранение бронирования
        Booking booking = Booking.builder()
                .item(testItem)
                .booker(testUser)
                .status(EBookingStatus.APPROVED)
                .startAt(Timestamp.from(Instant.now().minusSeconds(7200)))
                .endAt(Timestamp.from(Instant.now().minusSeconds(3600)))
                .build();

        bookingRepository.save(booking);

        // Вызов метода репозитория и проверка результата
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndAtBefore(
                testUser.getId(), testItem.getId(), EBookingStatus.APPROVED, Timestamp.from(Instant.now()));

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getBooker().getId()).isEqualTo(testUser.getId());
        assertThat(bookings.get(0).getItem().getId()).isEqualTo(testItem.getId());
        assertThat(bookings.get(0).getStatus()).isEqualTo(EBookingStatus.APPROVED);
    }
}