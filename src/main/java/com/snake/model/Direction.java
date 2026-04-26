package com.snake.model;

public enum Direction {
    UP {
        @Override
        public Position apply(final Position position) {
            return new Position(position.x(), position.y() - 1);
        }

        @Override
        public Direction opposite() {
            return DOWN;
        }
    },
    DOWN {
        @Override
        public Position apply(final Position position) {
            return new Position(position.x(), position.y() + 1);
        }

        @Override
        public Direction opposite() {
            return UP;
        }
    },
    LEFT {
        @Override
        public Position apply(final Position position) {
            return new Position(position.x() - 1, position.y());
        }

        @Override
        public Direction opposite() {
            return RIGHT;
        }
    },
    RIGHT {
        @Override
        public Position apply(final Position position) {
            return new Position(position.x() + 1, position.y());
        }

        @Override
        public Direction opposite() {
            return LEFT;
        }
    };

    public abstract Position apply(Position position);

    public abstract Direction opposite();
}
