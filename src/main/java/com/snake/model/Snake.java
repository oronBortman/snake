package com.snake.model;

import java.util.ArrayDeque;

public class Snake {

    private final ArrayDeque<Position> body;

    public Snake(final Position start) {
        this.body = new ArrayDeque<>();
        this.body.addFirst(start);
    }

    public void move(final Direction direction) {
        body.addFirst(nextHead(direction));
        body.removeLast();
    }

    public void eat(final Direction direction) {
        body.addFirst(nextHead(direction));
    }

    private Position nextHead(final Direction direction) {
        return new Position(
            body.getFirst().x() + direction.dx,
            body.getFirst().y() + direction.dy
        );
    }

    public Position head() {
        return body.getFirst();
    }

    public ArrayDeque<Position> body() {
        return body;
    }

    public int size() {
        return body.size();
    }
}
