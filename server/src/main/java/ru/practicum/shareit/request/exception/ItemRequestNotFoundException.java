package ru.practicum.shareit.request.exception;

public class ItemRequestNotFoundException extends NullPointerException {
    public ItemRequestNotFoundException(String s) {
        super(s);
    }
}
