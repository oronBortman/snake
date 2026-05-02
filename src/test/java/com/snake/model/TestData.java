package com.snake.model;

import java.util.Arrays;
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

    static Position positionAfterStep(final Position from, final Direction direction) {
        return new Position(from.x() + direction.dx, from.y() + direction.dy);
    }

    static Direction randomValidChangeFrom(final Direction current) {
        final var valid = Arrays.stream(Direction.values())
            .filter(d -> !d.equals(current) && !d.equals(current.opposite()))
            .toList();
        return valid.get(ThreadLocalRandom.current().nextInt(valid.size()));
    }
}
