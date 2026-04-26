package com.snake.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Direction")
class DirectionTest {

    private static final Position aRandomPosition = new Position(5, 5);

    static Stream<Arguments> movementCases() {
        return Stream.of(
            Arguments.of(Direction.UP, new Position(5, 4)),
            Arguments.of(Direction.DOWN, new Position(5, 6)),
            Arguments.of(Direction.LEFT, new Position(4, 5)),
            Arguments.of(Direction.RIGHT, new Position(6, 5))
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
    void eachDirectionMovesPositionByCorrectDelta(final Direction direction, final Position expected) {
        final var result = direction.apply(aRandomPosition);

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
