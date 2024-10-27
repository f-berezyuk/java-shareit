package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setName("User Name");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getName()).isEqualTo("User Name");
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setName("User One");

        User user3 = new User();
        user3.setId(2L);
        user3.setEmail("user3@example.com");
        user3.setName("User Three");

        // Users with different IDs should not be equal
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1).doesNotHaveSameHashCodeAs(user3);
    }
}