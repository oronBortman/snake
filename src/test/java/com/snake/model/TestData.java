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
}
