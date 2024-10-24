package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where upper(u.email) = upper(?1)")
    User findByEmailIgnoreCase(String email);
}