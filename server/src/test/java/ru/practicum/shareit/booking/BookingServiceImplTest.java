package ru.practicum.shareit.booking;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingCreateReq;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a BookingCreateReq for tests
    private BookingCreateReq createValidBookingRequest() {
        return new BookingCreateReq(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .build();
    }

    private Item createItem(User owner) {
        return Item.builder()
                .id(1L)
                .name("Item Name")
                .ownerId(owner.getId())
                .available(true)
                .build();
    }

    @Test
    void addNewBooking_Success() {
        BookingCreateReq bookingReq = createValidBookingRequest();
        User user = createUser();
        Item item = createItem(user);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.existsByItemIdAndTimeOverlap(anyLong(), any(), any())).thenReturn(false);
        when(bookingRepository.save(any())).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(1L);
            return booking;
        });

        BookingDto result = bookingService.addNewBooking(user.getId(), bookingReq);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void addNewBooking_ThrowsValidationException_WhenStartIsAfterEnd() {
        BookingCreateReq bookingReq = new BookingCreateReq(1L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> bookingService.addNewBooking(1L, bookingReq))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Дата начала бронирования должна быть после даты окончания.");
    }

    @Test
    void addNewBooking_ThrowsValidationException_WhenStartIsEqualToEnd() {
        LocalDateTime sameTime = LocalDateTime.now().plusDays(1);
        BookingCreateReq bookingReq = new BookingCreateReq(1L, sameTime, sameTime);

        assertThatThrownBy(() -> bookingService.addNewBooking(1L, bookingReq))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Дата начала бронирования не должна совпадать с датой окончания.");
    }

    @Test
    void
    addNewBooking_ThrowsValidationException_WhenItemAlreadyBooked() {
        BookingCreateReq bookingReq = createValidBookingRequest();

        when(itemRepository.existsByItemIdAndTimeOverlap(anyLong(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> bookingService.addNewBooking(1L, bookingReq))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Предмет уже забронирован на указанные даты.");
    }

    @Test
    void addNewBooking_ThrowsItemNotFoundException_WhenItemDoesNotExist() {
        BookingCreateReq bookingReq = createValidBookingRequest();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.addNewBooking(1L, bookingReq))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Предмет не найден.");
    }

    @Test
    void addNewBooking_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
        BookingCreateReq bookingReq = createValidBookingRequest();
        Item item = createItem(createUser());

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.addNewBooking(1L, bookingReq))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Не смог найти пользователя с id [1].");
    }

    @Test
    void addNewBooking_ThrowsValidationException_WhenItemIsNotAvailable() {
        BookingCreateReq bookingReq = createValidBookingRequest();
        User user = createUser();
        Item item = createItem(user);
        item.setAvailable(false);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> bookingService.addNewBooking(user.getId(), bookingReq))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Предмет недоступен для бронирования.");
    }

    @Test
    void updateStatus_Success() {
        User user = createUser();
        Item item = createItem(user);
        Booking booking = Booking.builder()
                .id(1L)
                .startAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 12, 0)))
                .endAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 13, 0)))
                .status(EBookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .build();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        BookingDto result = bookingService.updateStatus(user.getId(), 1L, true);

        assertThat(result.getStatus()).isEqualTo(EBookingStatus.APPROVED);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void updateStatus_ThrowsUserNotAuthorizedException_WhenUserIsNotOwner() {
        User user = createUser();
        User otherUser = User.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .build();
        Item item = createItem(user);
        Booking booking = Booking.builder()
                .id(1L)
                .startAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 12, 0)))
                .endAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 13, 0)))
                .status(EBookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .build();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> bookingService.updateStatus(otherUser.getId(), 1L, true))
                .isInstanceOf(UserNotAuthorizedException.class)
                .hasMessage("Пользователь не является владельцем предмета.");
    }

    @Test
    void getBooking_Success() {
        User user = createUser();
        Item item = createItem(user);
        Booking booking = Booking.builder()
                .id(1L)
                .startAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 12, 0)))
                .endAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 13, 0)))
                .status(EBookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .build();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBooking(user.getId(), 1L);

        assertThat(result).isNotNull();
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void getBooking_ThrowsUserNotAuthorizedException_WhenUserIsNeitherOwnerNorBooker() {
        User user = User.builder().id(1L).name("User One").email("one@example.com").build();
        User unauthorizedUser = User.builder().id(2L).name("User Two").email("two@example.com").build();
        Item item = createItem(user);
        Booking booking = Booking.builder()
                .id(1L)
                .startAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 12, 0)))
                .endAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 13, 0)))
                .status(EBookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .build();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.getBooking(unauthorizedUser.getId(), 1L))
                .isInstanceOf(UserNotAuthorizedException.class)
                .hasMessage("Пользователь не является владельцем предмета или его пользователем.");
    }

    @Test
    void getAllBooking_Success() {
        User user = createUser();
        Item item = createItem(User.builder().id(2L).name("User Two").email("two@example.com").build());

        Booking booking = Booking.builder()
                .id(1L)
                .startAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 12, 0)))
                .endAt(Timestamp.valueOf(LocalDateTime.of(2023, 10, 1, 13, 0)))
                .status(EBookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .build();

        when(bookingRepository.findAllByBookerId(anyLong())).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBooking(user.getId());

        assertThat(result).hasSize(1);
        verify(bookingRepository, times(1)).findAllByBookerId(user.getId());
    }
}
