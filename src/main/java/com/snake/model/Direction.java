package com.snake.model;

import java.util.Map;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    public final int dx;
    public final int dy;

    Direction(final int dx, final int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Direction opposite() {
        return OPPOSITES.get(this);
    }

    private static final Map<Direction, Direction> OPPOSITES = Map.of(
        UP, DOWN, DOWN, UP, LEFT, RIGHT, RIGHT, LEFT
    );
}
