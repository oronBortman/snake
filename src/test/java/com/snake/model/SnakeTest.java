package com.snake.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Snake")
class SnakeTest {

    @Test
    @DisplayName("starts with size 1")
    void startsWithSizeOne() {
        final var snake = new Snake(TestData.randomPosition(), Direction.RIGHT);
        assertThat(snake.size(), equalTo(1));
    }

    @Test
    @DisplayName("head is at the starting position")
    void headIsAtStartingPosition() {
        final var start = TestData.randomPosition();
        final var snake = new Snake(start, Direction.RIGHT);
        assertThat(snake.head(), equalTo(start));
    }

    @Test
    @DisplayName("reports the correct initial direction")
    void reportsCorrectInitialDirection() {
        final var snake = new Snake(TestData.randomPosition(), Direction.UP);
        assertThat(snake.direction(), equalTo(Direction.UP));
    }

    @Test
    @DisplayName("head moves one step in current direction after move")
    void headMovesOneStepInCurrentDirection() {
        final var start = TestData.randomPosition();
        final var snake = new Snake(start, Direction.RIGHT);
        snake.move();
        assertThat(snake.head(), equalTo(new Position(start.x() + 1, start.y())));
    }

    @Test
    @DisplayName("old tail is removed after move")
    void oldTailIsRemovedAfterMove() {
        final var start = TestData.randomPosition();
        final var snake = new Snake(start, Direction.RIGHT);
        snake.move();
        assertThat(snake.body(), not(hasItem(start)));
    }

    @Test
    @DisplayName("size is unchanged after move")
    void sizeIsUnchangedAfterMove() {
        final var snake = new Snake(TestData.randomPosition(), Direction.DOWN);
        snake.move();
        assertThat(snake.size(), equalTo(1));
    }

    @Test
    @DisplayName("size increases by 1 after grow then move")
    void sizeIncreasesByOneAfterGrowThenMove() {
        final var snake = new Snake(TestData.randomPosition(), Direction.RIGHT);
        snake.grow();
        snake.move();
        assertThat(snake.size(), equalTo(2));
    }

    @Test
    @DisplayName("each grow call adds exactly one segment on the next move")
    void eachGrowCallAddsExactlyOneSegment() {
        final var snake = new Snake(TestData.randomPosition(), Direction.RIGHT);
        snake.grow();
        snake.grow();
        snake.move();
        assertThat(snake.size(), equalTo(2));
        snake.move();
        assertThat(snake.size(), equalTo(3));
    }

    @Test
    @DisplayName("body segments are in correct spatial order after growth")
    void bodySegmentsAreInCorrectSpatialOrderAfterGrowth() {
        final var start = TestData.randomPosition();
        final var snake = new Snake(start, Direction.RIGHT);
        snake.grow();
        snake.move();
        assertThat(snake.head(), equalTo(new Position(start.x() + 1, start.y())));
        assertThat(snake.body().getLast(), equalTo(start));
    }

    @Test
    @DisplayName("adopts new direction when change is valid")
    void adoptsNewDirectionWhenValid() {
        final var snake = new Snake(TestData.randomPosition(), Direction.RIGHT);
        snake.changeDirection(Direction.UP);
        assertThat(snake.direction(), equalTo(Direction.UP));
    }

    @Test
    @DisplayName("ignores direction change that would reverse into itself")
    void ignoresReversalDirectionChange() {
        final var snake = new Snake(TestData.randomPosition(), Direction.RIGHT);
        snake.changeDirection(Direction.LEFT);
        assertThat(snake.direction(), equalTo(Direction.RIGHT));
    }

    @Test
    @DisplayName("direction is unchanged after an ignored input")
    void directionUnchangedAfterIgnoredInput() {
        final var snake = new Snake(TestData.randomPosition(), Direction.UP);
        snake.changeDirection(Direction.DOWN);
        assertThat(snake.direction(), equalTo(Direction.UP));
    }
}
