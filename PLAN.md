# Snake Game with Spring Boot, WebFlux & AI Agents - Learning Plan

## Project Philosophy
- **Control**: You drive architecture decisions and understand every line of code
- **Learning**: Each phase builds on solid understanding from previous phases
- **Incremental**: Start simple, add complexity gradually

---

## Phase 1: Foundation - Basic Snake Game (Plain Java)
**Goal**: Understand core game logic before adding frameworks

### What You'll Build
- `Position` — x/y coordinates value object
- `Direction` — enum with movement deltas and opposite detection
- `Snake` — body, direction, growth
- `Food` — food position
- `GameBoard` — board dimensions and boundaries
- `GameEngine` — game loop, movement, collision
- `CollisionDetector` — wall and self-collision

### Exit Criteria
- Working snake game with all core mechanics
- All unit tests pass
- You can explain how collision detection and the game loop work

---

## Phase 2: Spring Boot Integration
**Goal**: Transform plain Java game into a Spring Boot application

### What You'll Build
- Spring Boot app structure with dependency injection
- `GameConfig` — external configuration via `application.yml`
- `GameService` — Spring-managed game logic
- `GameController` — REST API (start, pause, reset)
- Basic HTML page displaying game state

### Exit Criteria
- Spring Boot application starts successfully
- REST API controls the game
- All tests pass (unit + integration)
- You understand the Spring bean lifecycle and dependency injection

---

## Phase 3: Reactive Programming with WebFlux & WebSocket
**Goal**: Add real-time multiplayer using reactive streams

### What You'll Build
- WebSocket endpoint for real-time game updates
- Multiple concurrent game sessions
- HTML5 Canvas frontend
- Real-time game state broadcasting via `Flux`

### Exit Criteria
- Multiple browsers can connect and play simultaneously
- You understand `Flux`, `Mono`, and backpressure
- Can modify the WebSocket message format

---

## Phase 4: AI Opponents - Strategy Patterns
**Goal**: Add AI snakes with distinct personalities

### What You'll Build
- `AiStrategy` interface
- `RandomStrategy`, `GreedyStrategy`, `SmartStrategy`, `AggressiveStrategy`, `DefensiveStrategy`
- A* and BFS pathfinding implementations
- Game mode: 1 human vs 3 AI snakes

### Exit Criteria
- 5 working AI strategies with distinct behaviors
- You understand A* pathfinding
- You can add a new strategy independently

---

## Phase 5: Reinforcement Learning Agents
**Goal**: Add an AI that learns and improves over time

### What You'll Build
- RL agent using DL4J or TensorFlow Java
- State encoder (game state → neural network input)
- Reward function
- Training loop with model persistence
- Metrics dashboard (win rate, average score)

### Exit Criteria
- RL agent trains and improves over time
- Trained agent performs better than random
- You understand the reward function's impact on behavior

---

## Progressive Complexity

| Phase | Concepts | Your Control |
|-------|----------|--------------|
| 1 — Plain Java | 3–5 | 100% |
| 2 — Spring Boot | 5–8 | 80% |
| 3 — WebFlux | 8–12 | 70% |
| 4 — AI Strategies | 10–15 | 60% |
| 5 — ML/RL | 15–20 | 50% |

**Move to the next phase only when you can explain every class in the current one.**
