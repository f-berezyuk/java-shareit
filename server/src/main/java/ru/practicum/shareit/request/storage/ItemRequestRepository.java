package ru.practicum.shareit.request.storage;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByUserIdOrderByCreatedDesc(Long userId);

    Page<ItemRequest> findByUserIdNotOrderByCreatedDesc(Long userId, Pageable pageable);

}
