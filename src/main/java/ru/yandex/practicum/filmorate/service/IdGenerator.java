package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    private long currentId;

    public long nextId() {
        return currentId++;
    }
}
