package com.snake.model;

import java.util.ArrayDeque;

public class Snake {

    private final ArrayDeque<Position> body;
    private Direction direction;
    private int segmentsToAdd;

    public Snake(final Position start, final Direction direction) {
        this.body = new ArrayDeque<>();
        this.body.addFirst(start);
        this.direction = direction;
        this.segmentsToAdd = 0;
    }

    public void move() {
        body.addFirst(nextHead());
        if (segmentsToAdd > 0) {
            segmentsToAdd--;
        } else {
            body.removeLast();
        }
    }

    public void grow() {
        segmentsToAdd++;
    }

    private Position nextHead() {
        return new Position(
            body.getFirst().x() + direction.dx,
            body.getFirst().y() + direction.dy
        );
    }

    public void changeDirection(final Direction newDirection) {
        if (!newDirection.equals(direction.opposite())) {
            direction = newDirection;
        }
    }

    public Position head() {
        return body.getFirst();
    }

    public ArrayDeque<Position> body() {
        return body;
    }

    public Direction direction() {
        return direction;
    }

    public int size() {
        return body.size();
    }
}
