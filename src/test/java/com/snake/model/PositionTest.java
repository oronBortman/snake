package com.snake.model;

import static com.snake.model.TestData.randomCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Position")
class PositionTest {

    static Stream<Arguments> positionsWithDifferentCoordinates() {
        final var x = randomCoordinate();
        final var y = randomCoordinate();
        return Stream.of(
            Arguments.of(new Position(x, y), new Position(x + 1, y)),
            Arguments.of(new Position(x, y), new Position(x, y + 1))
        );
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
        assertThat(new Position(x, y), equalTo(new Position(x, y)));
    }

    @ParameterizedTest
    @DisplayName("positions with different coordinates are not equal")
    @MethodSource("positionsWithDifferentCoordinates")
    void positionsWithDifferentCoordinatesAreNotEqual(final Position first, final Position second) {
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
}
