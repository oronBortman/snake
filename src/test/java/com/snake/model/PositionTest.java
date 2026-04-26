package com.snake.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Position")
class PositionTest {

    private static int randomCoordinate() {
        return ThreadLocalRandom.current().nextInt(0, 1000);
    }

    @Test
    @DisplayName("stores the x coordinate")
    void storesTheXCoordinate() {
        final var x = randomCoordinate();
        final var position = new Position(x, randomCoordinate());
        assertThat(position.x(), equalTo(x));
    }

    @Test
    @DisplayName("stores the y coordinate")
    void storesTheYCoordinate() {
        final var y = randomCoordinate();
        final var position = new Position(randomCoordinate(), y);
        assertThat(position.y(), equalTo(y));
    }

    @Test
    @DisplayName("two positions with the same coordinates are equal")
    void twoPositionsWithTheSameCoordinatesAreEqual() {
        final var x = randomCoordinate();
        final var y = randomCoordinate();
        final var first = new Position(x, y);
        final var second = new Position(x, y);

        assertThat(first, equalTo(second));
    }

    @Test
    @DisplayName("two positions with different x coordinates are not equal")
    void twoPositionsWithDifferentXCoordinatesAreNotEqual() {
        final var x = randomCoordinate();
        final var y = randomCoordinate();
        final var first = new Position(x, y);
        final var second = new Position(x + 1, y);

        assertThat(first, not(equalTo(second)));
    }

    @Test
    @DisplayName("two positions with different y coordinates are not equal")
    void twoPositionsWithDifferentYCoordinatesAreNotEqual() {
        final var x = randomCoordinate();
        final var y = randomCoordinate();
        final var first = new Position(x, y);
        final var second = new Position(x, y + 1);

        assertThat(first, not(equalTo(second)));
    }

    @Test
    @DisplayName("equality is symmetric")
    void equalityIsSymmetric() {
        final var x = randomCoordinate();
        final var y = randomCoordinate();
        final var a = new Position(x, y);
        final var b = new Position(x, y);

        assertThat(a.equals(b), equalTo(b.equals(a)));
    }

    @Test
    @DisplayName("equality is consistent across repeated calls")
    void equalityIsConsistentAcrossRepeatedCalls() {
        final var x = randomCoordinate();
        final var y = randomCoordinate();
        final var a = new Position(x, y);
        final var b = new Position(x, y);

        assertThat(a.equals(b), equalTo(a.equals(b)));
    }
}
