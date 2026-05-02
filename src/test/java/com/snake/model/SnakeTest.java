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

        final var snake = snakeWith(start);

        assertThat(snake.head(), equalTo(start));
    }

    @Test
    @DisplayName("starts with one body segment at the starting position")
    void startsWithOneBodySegmentAtStartingPosition() {
        final var start = randomPosition();

        final var snake = snakeWith(start);

        assertThat(snake.body(), contains(start));
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
    @DisplayName("grow does not move the head")
    void growDoesNotMoveHead() {
        final var start = randomPosition();
        final var snake = snakeWith(start);

        snake.grow();

        assertThat(snake.head(), equalTo(start));
    }

    @Test
    @DisplayName("grow adds one segment at the tail")
    void growAddsOneSegmentAtTail() {
        final var start = randomPosition();
        final var snake = snakeWith(start);

        snake.grow();

        assertThat(snake.body(), contains(start, start));
    }

    @Test
    @DisplayName("each grow call adds exactly one segment")
    void eachGrowCallAddsExactlyOneSegment() {
        final var snake = randomSnake();

        snake.grow();
        assertThat(snake.size(), equalTo(2));

        snake.grow();
        assertThat(snake.size(), equalTo(3));
    }

    @Test
    @DisplayName("size is retained after grows then move")
    void sizeIsRetainedAfterGrowThenMove() {
        final var snake = randomSnake();

        snake.grow();
        snake.move(randomDirection());

        assertThat(snake.size(), equalTo(2));
    }

    @Test
    @DisplayName("body is in correct spatial order after grow then move")
    void bodyIsInCorrectSpatialOrderAfterGrowThenMove() {
        final var start = randomPosition();
        final var direction = randomDirection();
        final var snake = snakeWith(start);

        snake.grow();
        snake.move(direction);

        assertThat(snake.head(), equalTo(positionAfterStep(start, direction)));
        assertThat(snake.body().getLast(), equalTo(start));
    }
}
