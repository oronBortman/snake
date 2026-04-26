package com.snake.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Position")
class PositionTest {

    private static final Position aRandomPosition = new Position(3, 7);

    @Test
    @DisplayName("stores the x coordinate")
    void storesTheXCoordinate() {
        assertThat(aRandomPosition.x(), equalTo(3));
    }

    @Test
    @DisplayName("stores the y coordinate")
    void storesTheYCoordinate() {
        assertThat(aRandomPosition.y(), equalTo(7));
    }

    @Test
    @DisplayName("two positions with the same coordinates are equal")
    void twoPositionsWithTheSameCoordinatesAreEqual() {
        final var first = new Position(4, 9);
        final var second = new Position(4, 9);

        assertThat(first, equalTo(second));
    }

    @Test
    @DisplayName("two positions with different x coordinates are not equal")
    void twoPositionsWithDifferentXCoordinatesAreNotEqual() {
        final var first = new Position(1, 5);
        final var second = new Position(2, 5);

        assertThat(first, not(equalTo(second)));
    }

    @Test
    @DisplayName("two positions with different y coordinates are not equal")
    void twoPositionsWithDifferentYCoordinatesAreNotEqual() {
        final var first = new Position(5, 1);
        final var second = new Position(5, 2);

        assertThat(first, not(equalTo(second)));
    }

    @Test
    @DisplayName("equality is symmetric")
    void equalityIsSymmetric() {
        final var a = new Position(6, 3);
        final var b = new Position(6, 3);

        assertThat(a.equals(b), equalTo(b.equals(a)));
    }

    @Test
    @DisplayName("equality is consistent across repeated calls")
    void equalityIsConsistentAcrossRepeatedCalls() {
        final var a = new Position(2, 8);
        final var b = new Position(2, 8);

        assertThat(a.equals(b), equalTo(a.equals(b)));
    }
}
