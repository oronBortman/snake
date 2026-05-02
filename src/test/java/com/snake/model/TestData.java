package com.snake.model;

import java.util.concurrent.ThreadLocalRandom;

final class TestData {

    private TestData() {}

    static int randomCoordinate() {
        return ThreadLocalRandom.current().nextInt(1, 100);
    }

    static Position randomPosition() {
        return new Position(randomCoordinate(), randomCoordinate());
    }

    static Direction randomDirection() {
        final var values = Direction.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    static Snake randomSnake() {
        return new Snake(randomPosition(), randomDirection());
    }

    static Snake snakeWith(final Position start) {
        return new Snake(start, randomDirection());
    }

    static Snake snakeWith(final Direction direction) {
        return new Snake(randomPosition(), direction);
    }
}
