package ru.practicum.shareit.booking.storage;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.EBookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.booker.id = ?1")
    List<Booking> findAllByBookerId(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2 and b.status = ?3 and b.endAt < ?4")
    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndAtBefore(Long bookerId, Long itemId, EBookingStatus status
            , Timestamp before);
}
