package com.snake.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Direction")
class DirectionTest {

    static Stream<Arguments> deltaValues() {
        return Stream.of(
            Arguments.of(Direction.UP, 0, -1),
            Arguments.of(Direction.DOWN, 0, 1),
            Arguments.of(Direction.LEFT, -1, 0),
            Arguments.of(Direction.RIGHT, 1, 0)
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
    @DisplayName("each direction has the correct movement delta")
    @MethodSource("deltaValues")
    void eachDirectionHasCorrectMovementDelta(final Direction direction, final int expectedDx, final int expectedDy) {
        assertThat(direction.dx, equalTo(expectedDx));
        assertThat(direction.dy, equalTo(expectedDy));
    }

    @ParameterizedTest
    @DisplayName("each direction returns the correct opposite")
    @MethodSource("oppositePairs")
    void eachDirectionReturnsCorrectOpposite(final Direction direction, final Direction expectedOpposite) {
        assertThat(direction.opposite(), equalTo(expectedOpposite));
    }

}
