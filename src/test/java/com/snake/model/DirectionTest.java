package com.snake.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Direction")
class DirectionTest {

    private static int randomCoordinate() {
        return ThreadLocalRandom.current().nextInt(1, 100);
    }

    static Stream<Arguments> movementCases() {
        final var x = randomCoordinate();
        final var y = randomCoordinate();
        final var base = new Position(x, y);
        return Stream.of(
            Arguments.of(Direction.UP, base, new Position(x, y - 1)),
            Arguments.of(Direction.DOWN, base, new Position(x, y + 1)),
            Arguments.of(Direction.LEFT, base, new Position(x - 1, y)),
            Arguments.of(Direction.RIGHT, base, new Position(x + 1, y))
        );
    }

    static Stream<Arguments> oppositePairs() {
        return Stream.of(
            Arguments.of(Direction.UP, Direction.DOWN),
            Arguments.of(Direction.DOWN, Direction.UP),
            Arguments.of(Direction.LEFT, Direction.RIGHT),
            Arguments.of(Direction.RIGHT, Direction.LEFT)
        );
    }

    @ParameterizedTest
    @DisplayName("each direction moves position by the correct delta")
    @MethodSource("movementCases")
    void eachDirectionMovesPositionByCorrectDelta(final Direction direction, final Position base, final Position expected) {
        final var result = direction.apply(base);

        assertThat(result, equalTo(expected));
    }

    @ParameterizedTest
    @DisplayName("each direction returns the correct opposite")
    @MethodSource("oppositePairs")
    void eachDirectionReturnsCorrectOpposite(final Direction direction, final Direction expectedOpposite) {
        assertThat(direction.opposite(), equalTo(expectedOpposite));
    }

    @ParameterizedTest
    @DisplayName("no direction is its own opposite")
    @EnumSource(Direction.class)
    void noDirectionIsItsOwnOpposite(final Direction direction) {
        assertThat(direction.opposite(), not(equalTo(direction)));
    }
}
