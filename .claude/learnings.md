---
name: Snake Session Learnings
description: Concepts and design decisions learned during the Snake Phase 1 implementation session
originSessionId: 14da0b73-34c5-44eb-9897-ae12b5e2d7c0
---
# Snake Session Learnings

## Data Structures

### ArrayDeque as a Deque
- `Deque` = Double Ended Queue — add/remove from both ends in O(1)
- `ArrayDeque` is the implementation; `Deque` is the interface we expose
- `ArrayList.add(0, element)` is O(n); `ArrayDeque.addFirst()` is O(1)
- Head = `getFirst()`, Tail = `getLast()` — convention, not a technical requirement
- Encapsulation hides this: the engine calls `snake.head()` and never knows which end

## Design Principles

### Responsibility Boundaries
- Write one sentence per class: "This class knows ___ and is responsible for ___"
- If the sentence contains "and also", split the class
- Snake: body state only — `move(Direction)`, `grow()`, `head()`, `body()`, `size()`
- GameEngine: direction state, reversal guard, food collision, tick control
- CollisionDetector: wall and self collision

### grow() Design Journey
- First design: `grow(Direction)` — moved head AND kept tail (two responsibilities)
- Fix: grow is a pure body operation — tail extends, head stays, no direction needed
- Engine calls `move()` every tick and `grow()` additionally on food collision

### Names Reveal Design Problems
- `eat(Direction)` passed a structure review — failed a semantics review: "the snake doesn't know about food"
- `grow()` is correct — from the snake's perspective, it just gets longer
- If a name borrows another object's concept (food → eat, board → collide), the responsibility is misplaced

### Don't Over-Engineer
- Add interfaces only when two real things need to be treated uniformly
- Good encapsulation now means an interface can be added later with one line, no logic changes

## Testing

### Helper Methods Over @BeforeEach
- `@BeforeEach` + `@Nested` forces mutable fields — breaks the `final` rule
- Helper methods (`snakeWith`, `randomSnake`) give the same readability without shared mutable state
- `snakeWith(Position)` vs `snakeWith(Direction)` — overloads communicate what the test cares about

### Randomize Test Data
- `randomDirection()` proves behavior holds for any direction, not just `Direction.RIGHT`
- Use `direction.dx`/`direction.dy` to compute expected position dynamically
- Only fix a value when the test is specifically about that value (e.g. x=0 for wall-collision boundary)

### Removing Redundant Tests
- `sizeIncreasesByOneAfterGrow` was redundant — `bodySegmentsAreInCorrectSpatialOrderAfterGrow` already proved size=2
- If spatial order is correct, size is correct by definition

### When NOT to Parameterize
- `eachGrowCallAddsExactlyOneSegment` runs two sequential cycles on the same instance — parameterizing changes the meaning
- The second grow tests behavior on an already-grown snake, which is a distinct behavioral property

## Plan Phase Protocol

The `eat(Direction)` → `grow(Direction)` → `grow()` redesign cost test rewrites and multiple commits. Entirely preventable in plan mode.

### The One Question That Catches Most Mistakes

**"Does this object own this value?"** — ask it for every method parameter.

- Snake has no direction state → `grow(Direction)` is wrong → `grow()`
- Snake has no food knowledge → `eat()` is wrong → `grow()`

If the object doesn't own the value, the parameter doesn't belong there.

### In Plan Mode — Before Approving Any Design

1. **One sentence per class** — "This class knows ___ and is responsible for ___" — "and also" = split it
2. **Signatures only, no bodies** — ask Claude to show method signatures before any implementation
3. **For every parameter** — "where does this value come from? Does this class own it?"
4. **Read names from the object's perspective** — a name that borrows another object's concept signals wrong responsibility
5. **Trace one future change** — "if I change X, which objects need to change?" — wrong objects changing = leaky boundary

Never approve a design without asking: "does this object own every parameter it receives?"

### Questions to Ask Claude at the Start of Each Task

1. "For each class, one sentence: what it knows and what it's responsible for."
2. "Show me signatures only — no bodies."
3. "For each parameter: does this class own this value?"
4. "Are any names borrowing a concept from another object?"
5. "If I change [requirement], which objects change — does that make sense?"

## Workflow

### GitHub Issues and Plan Accuracy
- Issues and plan must reflect current design — stale descriptions mislead future work
- Close issues only after the PR is merged
- Delete review comments when they become inaccurate
