package ru.practicum.shareit.booking;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateReq;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addNewBooking(Long userId, BookingCreateReq bookingCreateReq) {
        LocalDateTime start = bookingCreateReq.getStart();
        LocalDateTime end = bookingCreateReq.getEnd();
        Long itemId = bookingCreateReq.getItemId();

        if (start.isAfter(end)) {
            throw new ValidationException("Дата начала бронирования должна быть после даты окончания.");
        }

        if (start.isEqual(end)) {
            throw new ValidationException("Дата начала бронирования не должна совпадать с датой окончания.");
        }

        if (itemRepository.existsByItemIdAndTimeOverlap(itemId,
                start,
                end)) {
            throw new ValidationException("Предмет уже забронирован на указанные даты.");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Предмет не найден."));

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не смог найти пользователя с id [" + userId + "]."));

        if (!item.getAvailable()) {
            throw new ValidationException("Предмет недоступен для бронирования.");
        }
        Timestamp startAt = Timestamp.valueOf(start);
        Timestamp endAt = Timestamp.valueOf(end);
        Booking booking = Booking.builder()
                .item(item)
                .booker(booker)
                .status(EBookingStatus.WAITING)
                .startAt(startAt)
                .endAt(endAt)
                .build();

        Booking saved = bookingRepository.save(booking);

        return BookingMapper.toDto(saved);
    }

    @Override
    public BookingDto updateStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = getOrThrow(bookingId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || !booking.getItem().getOwnerId().equals(user.get().getId())) {
            throw new UserNotAuthorizedException("Пользователь не является владельцем предмета.");
        }
        booking.setStatus(approved ? EBookingStatus.APPROVED : EBookingStatus.REJECTED);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = getOrThrow(bookingId);
        if (!booking.getItem().getOwnerId().equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new UserNotAuthorizedException("Пользователь не является владельцем предмета или его пользователем.");
        }
        return BookingMapper.toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBooking(Long userId) {
        return bookingRepository.findAllByBookerId(userId).stream().map(BookingMapper::toDto).toList();
    }

    private Booking getOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));
    }
}
