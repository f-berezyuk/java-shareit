package ru.practicum.shareit.user.exception;

public class UserEmailDuplicateException extends IllegalArgumentException {
    public UserEmailDuplicateException(String s) {
        super(s);
    }
}
