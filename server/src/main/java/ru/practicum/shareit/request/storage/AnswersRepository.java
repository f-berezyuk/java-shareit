package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Answer;

public interface AnswersRepository extends JpaRepository<Answer, Long> {
}