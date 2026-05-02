package com.snake.model;

import static com.snake.model.TestData.positionAfterStep;
import static com.snake.model.TestData.randomDirection;
import static com.snake.model.TestData.randomPosition;
import static com.snake.model.TestData.randomSnake;
import static com.snake.model.TestData.snakeWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Snake")
class SnakeTest {

    @Test
    @DisplayName("head is at the starting position")
    void headIsAtStartingPosition() {
        final var start = randomPosition();
        assertThat(snakeWith(start).head(), equalTo(start));
    }

    @Test
    @DisplayName("starts with one body segment at the starting position")
    void startsWithOneBodySegmentAtStartingPosition() {
        final var start = randomPosition();
        assertThat(snakeWith(start).body(), contains(start));
    }

    @Test
    @DisplayName("head moves one step in the given direction after move")
    void headMovesOneStepInGivenDirection() {
        final var start = randomPosition();
        final var direction = randomDirection();
        final var snake = snakeWith(start);
        snake.move(direction);
        assertThat(snake.head(), equalTo(positionAfterStep(start, direction)));
    }

    @Test
    @DisplayName("old tail is removed after move")
    void oldTailIsRemovedAfterMove() {
        final var start = randomPosition();
        final var snake = snakeWith(start);
        snake.move(randomDirection());
        assertThat(snake.body(), not(hasItem(start)));
    }

    @Test
    @DisplayName("size is unchanged after move")
    void sizeIsUnchangedAfterMove() {
        final var snake = randomSnake();
        snake.move(randomDirection());
        assertThat(snake.size(), equalTo(1));
    }

    @Test
    @DisplayName("size increases by 1 after grow")
    void sizeIncreasesByOneAfterGrow() {
        final var snake = randomSnake();
        snake.grow(randomDirection());
        assertThat(snake.size(), equalTo(2));
    }

    @Test
    @DisplayName("each grow call adds exactly one segment")
    void eachGrowCallAddsExactlyOneSegment() {
        final var snake = randomSnake();
        snake.grow(randomDirection());
        assertThat(snake.size(), equalTo(2));
        snake.grow(randomDirection());
        assertThat(snake.size(), equalTo(3));
    }

    @Test
    @DisplayName("body segments are in correct spatial order after grow")
    void bodySegmentsAreInCorrectSpatialOrderAfterGrow() {
        final var start = randomPosition();
        final var direction = randomDirection();
        final var snake = snakeWith(start);
        snake.grow(direction);
        assertThat(snake.head(), equalTo(positionAfterStep(start, direction)));
        assertThat(snake.body().getLast(), equalTo(start));
    }
}
