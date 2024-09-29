package ru.practicum.shareit.item.storage;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long userId);

    @Query("""
            select i from Item i
            where (upper(i.name) like upper(:search) or upper(i.description) like upper(:search))
            and i.available = true""")
    List<Item> findAllByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableIsTrue(@Param("search") String search);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.id = :itemId " +
            "AND (:startAt BETWEEN b.startAt AND b.endAt " +
            "OR :endAt BETWEEN b.startAt AND b.endAt " +
            "OR (b.startAt BETWEEN :startAt AND :endAt) " +
            "OR (b.endAt BETWEEN :startAt AND :endAt))")
    boolean existsByItemIdAndTimeOverlap(@Param("itemId") Long itemId,
                                         @Param("startAt") LocalDateTime startAt,
                                         @Param("endAt") LocalDateTime endAt);
}
