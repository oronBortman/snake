# Snake Game with Spring Boot, WebFlux & AI Agents - Learning Plan

## Project Philosophy
**Control**: You drive architecture decisions and understand every line of code
**Learning**: Each phase builds on solid understanding from previous phases
**Incremental**: Start simple, add complexity gradually
**Best Practices**: Learn to work effectively with Claude Code throughout

---

## Phase 1: Foundation - Basic Snake Game (Plain Java)
**Goal**: Understand core game logic before adding frameworks — and establish the right habits for working with Claude throughout this project.

### Main Goals
**Technical**: Build a working Snake game in plain Java with full understanding of every line.

**Meta — Learning to work with Claude the right way**:
- Always ask Claude to explain a concept *before* any code is written
- You propose the design; Claude advises and implements based on your decisions
- Question every pattern: "Why did you use X here instead of Y?"
- Never accept code you can't explain — slow down and ask
- Use Claude as a tutor and pair programmer, not a code generator

### Learning Objectives
**Java & OOP:**
- Game loop and state management
- Collision detection algorithms
- Object-oriented design (single responsibility, encapsulation)
- Java collections: why `ArrayDeque` beats `LinkedList` for this use case

**Working with Claude:**
- How to ask for explanations before implementation
- How to drive architecture decisions yourself
- How to review generated code critically
- How to use `/commit` after each completed, tested feature
- Recognising when you're moving too fast (can't explain what you just built)

### Architecture Decisions (DECIDED)
| Decision | Choice | Reason |
|----------|--------|--------|
| Snake body | `ArrayDeque<Position>` | No extra Node wrapper objects per element — LinkedList allocates a Node + Position per segment, ArrayDeque only allocates the Position |
| Coordinate system | Top-left (0,0), Y increases downward | Matches HTML Canvas directly — no coordinate translation needed in Phase 3 |
| Direction reversal | Blocked | Cannot reverse into yourself — illegal move is silently ignored |

### Build Order
Build in dependency order — each class only depends on classes already written.

1. **`Position`** — x, y coordinates. No dependencies.
2. **`Direction`** — enum: UP, DOWN, LEFT, RIGHT. No dependencies.
3. **`Snake`** — body as `ArrayDeque<Position>`, current direction. Depends on `Position`, `Direction`.
4. **`Food`** — a position on the board. Depends on `Position`.
5. **`GameBoard`** — width, height, holds snake and food. Depends on `Position`, `Snake`, `Food`.
6. **`CollisionDetector`** — wall and self collision. Depends on `Position`, `Snake`, `GameBoard`.
7. **`GameEngine`** — game loop, movement, food placement, collision checks. Depends on everything above.
8. **`Main`** — entry point, wires everything together.

### Files to Create
```
src/main/java/com/snake/
├── model/
│   ├── Position.java           (x, y coordinates)
│   ├── Direction.java          (enum: UP, DOWN, LEFT, RIGHT)
│   ├── Snake.java              (body as ArrayDeque<Position>, direction)
│   ├── Food.java               (food position)
│   └── GameBoard.java          (board dimensions, snake, food)
├── engine/
│   ├── GameEngine.java         (game loop, movement, scoring)
│   └── CollisionDetector.java  (wall & self collision)
└── Main.java                   (entry point)
```
*Test files are defined per-task below — the subagent creates them following TDD.*

### Execution Strategy
**Skill**: `superpowers:subagent-driven-development`

Claude dispatches a fresh subagent per task → spec compliance review → code quality review → auto-fix → commit. Only escalates to you when genuinely blocked.

**You review at class boundaries, not between every task:**

| Review checkpoint | Tasks covered |
|-------------------|--------------|
| Review 1 | Task 0–2: setup, Position, Direction |
| Review 2 | Task 3–6: Snake (all behaviours) |
| Review 3 | Task 7–8: Food, GameBoard |
| Review 4 | Task 9–10: CollisionDetector |
| Review 5 | Task 11–14: GameEngine, Main |

**Stop conditions — Claude escalates to you when:**
- A task is BLOCKED (missing dependency, test won't compile)
- Acceptance criteria are ambiguous (two valid interpretations)
- A design decision requires your input (e.g. edge case not covered in plan)
- Any task fails spec compliance review after 2 fix attempts

**Continue autonomously when:**
- Tests pass (`mvn test` exits 0)
- Spec compliance reviewer approves
- Code quality reviewer approves

**Code quality reviewer must check against:**
- All rules in `~/.claude/CLAUDE.md` — no wildcards, no nulls, no ternary, no comments, specific exceptions, always final, Hamcrest assertions
- TDD anti-patterns (see superpowers testing-anti-patterns reference)
- General Java best practices

**At each review checkpoint, you check:**
1. Does the design match what was approved in the plan?
2. Any CLAUDE.md rule violations the reviewer missed?
3. Any security concerns (input validation, data exposure)?
4. Do tests verify real behaviour — not just size, not just null?
5. Can you explain every architectural decision without looking at the plan?

*For this learning project*: also read every line at each checkpoint — understanding generated code is part of the learning goal.

### TDD Workflow — What the Subagent Does Per Task
**Skill**: `superpowers:test-driven-development` — every implementer subagent follows this.

The iron law: **NO production code without a failing test first.**

1. Subagent reads the task's acceptance criteria (behaviour only — no method signatures)
2. Subagent **invents the API** by imagining the ideal caller interface, writes the test
3. Subagent runs `mvn test` → confirms it **fails for the right reason (Red)**
4. Subagent writes the **minimum code** to pass — nothing more
5. Subagent runs `mvn test` → **all green**
6. Subagent refactors if needed, keeps tests green
7. Subagent commits and reports back to orchestrator

**What YOU do at each review checkpoint:**
- Read every line of committed code
- Verify it matches what you approved in the plan
- Ask questions about anything unexpected
- Check CLAUDE.md rule compliance
- Only proceed to next group of tasks when you can explain everything

**Red flags — subagent stops and escalates:**
- Test passed immediately (testing existing behaviour, not new)
- Cannot determine the right API from acceptance criteria alone
- Code written before test (restart the task)

### Phase 1 Tasks

#### Task 0 — Project Setup
**Goal**: Compile and run a test before writing any game code.
**Test first**: A single dummy test that asserts `true` — just to prove the test runner works.
**Acceptance criteria**:
- [x] `pom.xml` exists with JUnit 5 + Hamcrest dependencies
- [x] Maven directory structure in place (`src/main/java`, `src/test/java`)
- [x] `mvn test` runs and passes

---

#### Task 1 — `Position`: value object
**Behaviour**: Represents a coordinate on the board. Two positions at the same location are equal.
**Acceptance criteria**:
- [x] Stores an x and a y coordinate
- [x] Two positions with the same x and y are considered equal
- [x] Two positions with different coordinates are not equal
- [x] Equality is symmetric and consistent (a.equals(b) == b.equals(a))

---

#### Task 2 — `Direction`: movement enum
**Behaviour**: Represents the four directions a snake can move. Each direction knows how it changes position. A direction knows its opposite.
**Acceptance criteria**:
- [x] Four directions exist: UP, DOWN, LEFT, RIGHT
- [x] Each direction produces a predictable position change (e.g. UP moves y by -1, DOWN by +1)
- [x] UP and DOWN are opposites; LEFT and RIGHT are opposites
- [x] No direction is its own opposite

---

#### Task 3 — `Snake`: initialization ✅
**Behaviour**: A new snake starts at a given position with a body of exactly one segment. The snake has no direction — the caller decides direction at move time.
**Acceptance criteria**:
- [x] Snake starts with one body segment at the given starting position
- [x] Snake's head is at the starting position

---

#### Task 4 — `Snake`: movement ✅
**Behaviour**: When told to move in a direction, the snake's head advances one step and its tail is removed. The body length stays the same.
**Acceptance criteria**:
- [x] Head moves one step in the given direction after move(Direction)
- [x] Old tail position is no longer part of the body after move(Direction)
- [x] Size is unchanged after move(Direction)
- [x] Tail of a multi-segment snake is removed after move(Direction)

---

#### Task 5 — `Snake`: growth ✅
**Behaviour**: When told to grow, the snake adds one segment at its tail. The head does not move. The snake has no awareness of food — the engine calls grow() when it determines food was consumed.
**Acceptance criteria**:
- [x] grow() does not move the head
- [x] grow() adds one segment at the tail
- [x] Each grow() call adds exactly one segment
- [x] Size is retained after grow() then move()
- [x] Body is in correct spatial order after grow() then move()

---

#### Task 6 — `GameEngine`: direction change
**Behaviour**: Direction change handling lives in the GameEngine. The engine tracks the current direction and rejects reversal attempts before passing a direction to Snake.move(). Originally scoped as a Snake responsibility — moved here because Snake is direction-agnostic.
**Acceptance criteria**:
- [ ] Engine adopts the new direction when the change is valid
- [ ] Engine ignores a direction change that would reverse into the current direction
- [ ] Direction is unchanged after an ignored input

---

#### Task 7 — `Food`: value object
**Behaviour**: Represents a piece of food at a position on the board. Two food items at the same position are equal.
**Acceptance criteria**:
- [ ] Food has a position on the board
- [ ] Two food items at the same position are equal
- [ ] Position is accessible after creation

---

#### Task 8 — `GameBoard`: dimensions and bounds
**Behaviour**: Represents the playable area. Knows its size and whether any given position falls within the boundaries.
**Acceptance criteria**:
- [ ] Board has a width and height
- [ ] A position inside the board is within bounds
- [ ] A position at x=0, y=0 (top-left) is within bounds
- [ ] A position at x=width or y=height (on or beyond edge) is out of bounds
- [ ] All four edges are correctly treated as out of bounds

---

#### Task 9 — `CollisionDetector`: wall collision
**Behaviour**: Detects when a position falls outside the board boundaries.
**Acceptance criteria**:
- [ ] Position beyond the left edge is a wall collision
- [ ] Position beyond the right edge is a wall collision
- [ ] Position beyond the top edge is a wall collision
- [ ] Position beyond the bottom edge is a wall collision
- [ ] Valid position inside the board is not a wall collision

---

#### Task 10 — `CollisionDetector`: self collision
**Behaviour**: Detects when the snake's head occupies the same position as any part of its own body.
**Acceptance criteria**:
- [ ] Head overlapping a body segment is a self collision
- [ ] Head not overlapping any body segment is not a self collision
- [ ] Test uses a snake with enough segments that self collision is geometrically possible

---

#### Task 11 — `GameEngine`: single tick — movement
**Behaviour**: One game tick advances the snake one step in its current direction.
**Acceptance criteria**:
- [ ] Snake's head is one step further in its direction after a tick
- [ ] Snake's size is unchanged if no food was eaten

---

#### Task 12 — `GameEngine`: food eaten
**Behaviour**: When the snake's head reaches the food, the snake grows, a new food is placed on an empty cell, and the score increases.
**Acceptance criteria**:
- [ ] Snake is one segment longer after eating food
- [ ] New food does not appear on any cell the snake occupies
- [ ] Score increases by 1 each time food is eaten

---

#### Task 13 — `GameEngine`: game over
**Behaviour**: The game ends when the snake hits a wall or itself. Once over, ticks have no effect.
**Acceptance criteria**:
- [ ] Game is over after a wall collision
- [ ] Game is over after a self collision
- [ ] Ticking a finished game changes nothing
- [ ] Score is preserved and accessible after game over

---

#### Task 14 — `Main`: console runner
**Goal**: Wire everything together and prove the game runs end-to-end.
**No test**: This is integration glue — manually verify it runs.
**Acceptance criteria**:
- [ ] Game starts with a snake in the centre of a 20×20 board
- [ ] Prints board state to console each tick
- [ ] Runs until collision then prints final score

### Acceptance Criteria

**Technical — the game works:**
- [ ] Snake moves in all four directions
- [ ] Snake grows by one segment when it eats food
- [ ] Game ends on wall collision
- [ ] Game ends on self collision
- [ ] Direction reversal is blocked (ignored silently)
- [ ] New food spawns in a random empty cell after eating
- [ ] Score increments on each food eaten
- [ ] All unit tests pass with meaningful assertions (no null-only or size-only checks)

**Understanding — you own the code:**
- [ ] You can explain what `ArrayDeque` is and why it was chosen
- [ ] You can trace a full game tick through the code (move → check collision → check food → update state)
- [ ] You can explain how wall collision is detected
- [ ] You can explain how self collision is detected
- [ ] You can add a new method to any class without Claude's help

**Process — right habits established:**
- [ ] Each class was designed (fields/methods discussed) before being implemented
- [ ] You asked at least one "why" question per class
- [ ] Each class was committed separately after its tests passed
- [ ] You never accepted code you couldn't explain

### Red Flags — Slow Down If:
- You can't explain what a class does after it's written
- A test is failing and you don't know why
- You're reading the code for the first time after it's already committed
- You're copy-pasting without understanding

---

## Phase 2: Spring Boot Integration
**Goal**: Transform plain Java game into Spring Boot application

### Learning Objectives
- Spring Boot project structure
- Dependency injection and IoC container
- Spring component model (@Service, @Component)
- Configuration management (@ConfigurationProperties)
- Spring Boot testing

### What You'll Build
- Spring Boot application structure
- Game configuration (board size, speed, etc.)
- Service layer for game management
- RESTful API for game control (start, pause, reset)
- Basic HTML page to display game state

### New Concepts to Learn
- **Dependency Injection**: GameEngine receives dependencies via constructor
- **Application Properties**: External configuration (application.yml)
- **REST Controllers**: Exposing game operations via HTTP
- **Spring Boot Auto-configuration**: Understanding what happens at startup

### Files to Create/Modify
```
pom.xml                         (Spring Boot dependencies)
src/main/resources/
├── application.yml             (configuration)
└── static/
    └── index.html              (basic UI)

src/main/java/com/snake/
├── SnakeGameApplication.java   (Spring Boot entry point)
├── config/
│   └── GameConfig.java         (@ConfigurationProperties)
├── service/
│   └── GameService.java        (@Service - wraps GameEngine)
└── controller/
    └── GameController.java     (REST endpoints)

src/test/java/com/snake/
├── service/
│   └── GameServiceTest.java    (unit tests)
└── controller/
    └── GameControllerTest.java (integration tests with @SpringBootTest)
```

### Architecture Decisions (YOU decide)
- Which classes become Spring beans vs POJOs?
- What configuration goes in application.yml vs code?
- Should GameEngine be singleton or prototype scope?
- REST API design (endpoints, request/response format)

### Claude's Role in Phase 2
- **Explain**: Spring Boot concepts before implementing
- **Guide**: Show Spring Boot patterns and best practices
- **Implement**: Based on YOUR architectural decisions
- **Review**: Ensure you understand dependency injection flow

### Verification Steps
1. Spring Boot application starts successfully
2. Can call REST API to start/pause/reset game
3. Configuration changes in application.yml work correctly
4. All tests pass (unit + integration)
5. You can explain the Spring bean lifecycle

### Exit Criteria
- Working Spring Boot application
- Understanding of dependency injection
- Comfortable with Spring testing patterns
- Can modify configuration without breaking the app

---

## Phase 3: Reactive Programming with WebFlux & WebSocket
**Goal**: Add real-time multiplayer using reactive streams

### Learning Objectives
- Reactive programming concepts (Flux, Mono)
- WebSocket for bidirectional communication
- Managing concurrent game state
- Server-Sent Events (SSE) vs WebSocket trade-offs
- Backpressure handling

### What You'll Build
- WebSocket endpoint for real-time game updates
- Multiple concurrent game sessions
- Browser-based UI with HTML5 Canvas
- Real-time game state broadcasting
- Player input handling via WebSocket

### New Concepts to Learn
- **Flux**: Stream of game state updates
- **Mono**: Single async operations (start game, move)
- **WebSocket**: Bidirectional real-time communication
- **Reactive Streams**: Publisher-Subscriber model
- **Schedulers**: Controlling game loop execution

### Files to Create/Modify
```
pom.xml                         (add spring-boot-starter-webflux)

src/main/java/com/snake/
├── websocket/
│   ├── GameWebSocketHandler.java    (WebSocket handler)
│   └── GameSessionManager.java      (manages multiple games)
├── service/
│   └── ReactiveGameService.java     (reactive game operations)
└── config/
    └── WebSocketConfig.java         (WebSocket configuration)

src/main/resources/static/
├── game.html                   (Canvas-based UI)
├── js/
│   ├── game-client.js          (WebSocket client)
│   └── renderer.js             (Canvas rendering)
└── css/
    └── game.css                (styling)

src/test/java/com/snake/
├── websocket/
│   └── GameWebSocketHandlerTest.java
└── service/
    └── ReactiveGameServiceTest.java
```

### Architecture Decisions (YOU decide)
- WebSocket message format (JSON structure)
- How to identify different game sessions?
- Flux vs observable for game state updates?
- How to handle player disconnections?
- Client-side prediction vs server authoritative?

### Claude's Role in Phase 3
- **Teach**: Explain reactive programming before coding
- **Clarify**: Answer questions about Flux/Mono operations
- **Show**: Demonstrate WebSocket patterns
- **Verify**: Ensure you understand backpressure concepts

### Verification Steps
1. Multiple browsers can connect simultaneously
2. Each browser sees its game state in real-time
3. Player input (arrow keys) controls snake via WebSocket
4. Game state updates at consistent frame rate
5. No memory leaks with long-running sessions
6. You can explain the Flux pipeline for game updates

### Exit Criteria
- Multiplayer working with real-time updates
- Solid understanding of reactive streams
- Can modify WebSocket message format
- Comfortable debugging async issues

---

## Phase 4: AI Opponents - Strategy Patterns
**Goal**: Add AI snakes with different personalities

### Learning Objectives
- Pathfinding algorithms (A*, BFS)
- Strategy pattern implementation
- AI decision-making systems
- Performance optimization for AI calculations
- Testing non-deterministic behavior

### What You'll Build
- Multiple AI strategies (Aggressive, Defensive, Random)
- Pathfinding service using A* algorithm
- AI decision engine that selects moves
- Game mode: 1 human vs 3 AI snakes
- AI difficulty levels

### AI Strategies to Implement
1. **Random**: Moves randomly (baseline)
2. **Greedy**: Always moves toward nearest food
3. **Smart**: Uses A* pathfinding, avoids collisions
4. **Aggressive**: Tries to trap other snakes
5. **Defensive**: Prioritizes survival over food

### Files to Create
```
src/main/java/com/snake/
├── ai/
│   ├── AiStrategy.java              (interface)
│   ├── RandomStrategy.java
│   ├── GreedyStrategy.java
│   ├── SmartStrategy.java
│   ├── AggressiveStrategy.java
│   └── DefensiveStrategy.java
├── pathfinding/
│   ├── PathFinder.java              (interface)
│   ├── AStarPathFinder.java
│   └── BreadthFirstPathFinder.java
├── service/
│   └── AiDecisionService.java       (selects AI moves)
└── model/
    └── AiSnake.java                 (extends Snake)

src/test/java/com/snake/
├── ai/
│   └── StrategyTests.java
└── pathfinding/
    └── AStarPathFinderTest.java
```

### Architecture Decisions (YOU decide)
- How often should AI recalculate path?
- Should AI have perfect information or limited visibility?
- How to balance AI performance vs game responsiveness?
- Strategy selection at game start or dynamic?
- How to make AI "feel" different to players?

### Claude's Role in Phase 4
- **Explain**: A* algorithm before implementing
- **Guide**: Show strategy pattern best practices
- **Collaborate**: Implement each strategy together
- **Challenge**: Ask you to predict AI behavior
- **Optimize**: Help profile and improve performance

### Verification Steps
1. Each AI strategy behaves distinctly
2. Smart AI can navigate to food without dying
3. Aggressive AI actively pursues other snakes
4. Performance test: 10 AI snakes run smoothly
5. You can explain how A* works
6. You can add a new strategy independently

### Exit Criteria
- 5 working AI strategies with distinct personalities
- Understanding of pathfinding algorithms
- Can tune AI difficulty
- Comfortable with strategy pattern

---

## Phase 5: Reinforcement Learning Agents (Advanced)
**Goal**: Add AI that learns and improves over time

### Learning Objectives
- Reinforcement learning basics (Q-learning, Deep Q-Networks)
- State representation for ML
- Reward function design
- Training vs inference modes
- Integration with existing game engine

### What You'll Build
- RL agent using DL4J or TensorFlow Java
- Training mode to let AI learn
- Trained model persistence and loading
- Performance comparison vs rule-based AI
- Metrics dashboard (win rate, avg score)

### New Concepts to Learn
- **State Representation**: How to encode game state for neural network
- **Reward Function**: Points for food, penalties for death
- **Exploration vs Exploitation**: Epsilon-greedy strategy
- **Neural Network Architecture**: Input layer, hidden layers, output
- **Training Process**: Episodes, batches, convergence

### Files to Create
```
pom.xml                         (add DL4J or TensorFlow)

src/main/java/com/snake/
├── ml/
│   ├── RLAgent.java                 (reinforcement learning agent)
│   ├── StateEncoder.java            (converts game state to input)
│   ├── RewardCalculator.java        (reward function)
│   ├── NeuralNetworkModel.java      (NN definition)
│   └── TrainingService.java         (training loop)
├── service/
│   └── MLGameService.java           (training mode manager)
└── controller/
    └── TrainingController.java      (training control API)

src/main/resources/
└── models/
    └── trained-snake-model.zip      (saved model)

src/test/java/com/snake/
└── ml/
    └── RLAgentTest.java
```

### Architecture Decisions (YOU decide)
- Which RL algorithm to use? (Q-learning vs DQN vs PPO)
- State representation format? (grid, coordinates, features)
- Reward function parameters?
- Network architecture (layers, neurons)?
- When to stop training (convergence criteria)?

### Claude's Role in Phase 5
- **Educate**: Explain RL concepts step-by-step
- **Research**: Help compare ML libraries for Java
- **Design**: Collaborate on state encoding
- **Debug**: Help troubleshoot training issues
- **Analyze**: Interpret training metrics together

### Verification Steps
1. RL agent can be trained in headless mode
2. Training progress visible (loss decreasing)
3. Trained agent performs better than random
4. Model can be saved and reloaded
5. Inference works in multiplayer game
6. You understand the reward function impact
7. You can modify network architecture

### Exit Criteria
- Working RL agent that learns to play
- Understanding of RL fundamentals
- Can experiment with hyperparameters
- Agent competes reasonably with rule-based AI

---

## Best Practices: Working with Claude Throughout

### For Each Feature
1. **Ask for explanation first**: "Explain [concept] before we implement"
2. **Review architecture together**: "What are the trade-offs of approach A vs B?"
3. **Understand each file**: "Walk me through what this class does"
4. **Question patterns**: "Why did you use Optional here instead of null?"
5. **Test understanding**: Try to modify code yourself before asking Claude

### Using Claude Code Effectively

**High-Control Interactions** (You lead):
```
You: "I want to add a new AI strategy that avoids the center.
      Let's design it together first."
Claude: [Explains approach, you discuss]
You: "Ok, let's implement it with that approach"
Claude: [Implements YOUR design]
```

**Collaborative Learning**:
```
You: "Show me 3 different ways to handle WebSocket disconnections"
Claude: [Presents options with pros/cons]
You: "I'll go with option 2. Explain why that's better for our use case"
Claude: [Explains reasoning]
```

**Agent Autonomy** (Later phases):
```
You: "Refactor all AI strategies to use the new pathfinding interface"
Claude: [Uses Task agent to handle multi-file refactor]
You: [Review changes, understand the pattern]
```

### When to Use Skills
- `/commit`: After each completed feature
- `/review-pr`: Before finalizing a phase
- `/learn-from-mr`: If you have MR comments to incorporate

### Signs You're Moving Too Fast
- You can't explain what a class does
- Tests are failing and you don't know why
- You're accepting code without understanding it
- You're confused about Spring Boot magic

**Solution**: Slow down, ask Claude to explain, or simplify the feature.

---

## Critical Files Overview

### Phase 1 (Plain Java)
- `Snake.java` - Core game entity
- `GameEngine.java` - Game loop and rules
- `CollisionDetector.java` - Collision logic

### Phase 2 (Spring Boot)
- `pom.xml` - Dependencies and build config
- `SnakeGameApplication.java` - Entry point
- `GameService.java` - Spring-managed game logic
- `application.yml` - Configuration

### Phase 3 (WebFlux)
- `GameWebSocketHandler.java` - WebSocket logic
- `ReactiveGameService.java` - Reactive game operations
- `game-client.js` - Frontend WebSocket client

### Phase 4 (AI)
- `AiStrategy.java` - Strategy interface
- `AStarPathFinder.java` - Pathfinding implementation
- Individual strategy implementations

### Phase 5 (ML)
- `RLAgent.java` - Reinforcement learning agent
- `StateEncoder.java` - Game state to ML input
- `NeuralNetworkModel.java` - Neural network definition

---

## Progressive Complexity Model

```
Phase 1: Plain Java
├─ Complexity: Low
├─ Concepts: 3-5
└─ Control: 100% You

Phase 2: Spring Boot
├─ Complexity: Medium
├─ Concepts: 5-8
└─ Control: 80% You, 20% Claude

Phase 3: WebFlux
├─ Complexity: Medium-High
├─ Concepts: 8-12
└─ Control: 70% You, 30% Claude

Phase 4: AI Strategies
├─ Complexity: High
├─ Concepts: 10-15
└─ Control: 60% You, 40% Claude

Phase 5: ML/RL
├─ Complexity: Very High
├─ Concepts: 15-20
└─ Control: 50% You, 50% Claude (collaborative)
```

---

## Success Metrics Per Phase

### You know you're ready to move to next phase when:
1. ✅ All tests pass
2. ✅ You can explain every class's purpose
3. ✅ You can modify code independently
4. ✅ You understand the key concepts
5. ✅ You're excited (not overwhelmed) about next phase

### Red Flags to Slow Down:
1. ❌ Copying code without understanding
2. ❌ Tests failing without knowing why
3. ❌ Can't explain architectural decisions
4. ❌ Feeling lost in Spring Boot "magic"
5. ❌ Accepting suggestions blindly

---

## Recommended Timeline

**Not time-based, milestone-based**:
- Phase 1: Until you fully understand game mechanics
- Phase 2: Until Spring Boot patterns click
- Phase 3: Until reactive programming makes sense
- Phase 4: Until you can design AI strategies yourself
- Phase 5: Until you grasp RL fundamentals

**Trust your understanding, not the clock.**

---

## Final Notes

This is YOUR learning project. Claude is:
- ✅ Your tutor (explains concepts)
- ✅ Your pair programmer (implements together)
- ✅ Your code reviewer (catches mistakes)
- ✅ Your research assistant (finds best practices)

Claude is NOT:
- ❌ A magic code generator
- ❌ A replacement for your understanding
- ❌ Responsible for your learning pace

**The goal is not to finish fast. The goal is to deeply understand Spring Boot, WebFlux, and AI agents through a fun project.**

Take your time. Ask questions. Experiment. Break things. Learn.
