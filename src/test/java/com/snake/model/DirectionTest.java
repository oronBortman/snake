package com.snake.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Direction")
class DirectionTest {

    @Test
    @DisplayName("UP moves y by -1 and keeps x unchanged")
    void upMovesYByMinusOne() {
        final var start = new Position(5, 5);

        final var result = Direction.UP.apply(start);

        assertThat(result, equalTo(new Position(5, 4)));
    }

    @Test
    @DisplayName("DOWN moves y by +1 and keeps x unchanged")
    void downMovesYByPlusOne() {
        final var start = new Position(5, 5);

        final var result = Direction.DOWN.apply(start);

        assertThat(result, equalTo(new Position(5, 6)));
    }

    @Test
    @DisplayName("LEFT moves x by -1 and keeps y unchanged")
    void leftMovesXByMinusOne() {
        final var start = new Position(5, 5);

        final var result = Direction.LEFT.apply(start);

        assertThat(result, equalTo(new Position(4, 5)));
    }

    @Test
    @DisplayName("RIGHT moves x by +1 and keeps y unchanged")
    void rightMovesXByPlusOne() {
        final var start = new Position(5, 5);

        final var result = Direction.RIGHT.apply(start);

        assertThat(result, equalTo(new Position(6, 5)));
    }

    @Test
    @DisplayName("UP and DOWN are opposites")
    void upAndDownAreOpposites() {
        assertThat(Direction.UP.opposite(), equalTo(Direction.DOWN));
        assertThat(Direction.DOWN.opposite(), equalTo(Direction.UP));
    }

    @Test
    @DisplayName("LEFT and RIGHT are opposites")
    void leftAndRightAreOpposites() {
        assertThat(Direction.LEFT.opposite(), equalTo(Direction.RIGHT));
        assertThat(Direction.RIGHT.opposite(), equalTo(Direction.LEFT));
    }

    @Test
    @DisplayName("no direction is its own opposite")
    void noDirectionIsItsOwnOpposite() {
        assertThat(Direction.UP.opposite(), not(equalTo(Direction.UP)));
        assertThat(Direction.DOWN.opposite(), not(equalTo(Direction.DOWN)));
        assertThat(Direction.LEFT.opposite(), not(equalTo(Direction.LEFT)));
        assertThat(Direction.RIGHT.opposite(), not(equalTo(Direction.RIGHT)));
    }
}
