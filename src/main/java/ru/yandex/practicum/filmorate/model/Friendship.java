package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friendship {
    private long id;
    private long activeUserId;
    private long passiveUserId;
    private boolean isAccepted;
}
