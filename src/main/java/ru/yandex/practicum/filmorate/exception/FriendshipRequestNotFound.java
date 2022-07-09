package ru.yandex.practicum.filmorate.exception;

public class FriendshipRequestNotFound extends Exception {
    public FriendshipRequestNotFound() {
    }

    public FriendshipRequestNotFound(String message) {
        super(message);
    }
}
