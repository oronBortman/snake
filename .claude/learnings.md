---
name: Snake Session Learnings
description: Concepts and design decisions learned during the Snake Phase 1 implementation session
originSessionId: 14da0b73-34c5-44eb-9897-ae12b5e2d7c0
---
# Snake Session Learnings

## Data Structures

### ArrayDeque as a Deque
- `Deque` = **D**ouble **E**nded **Queue** — add and remove from both ends in O(1)
- `ArrayDeque` is the implementation; `Deque` is the interface we expose publicly
- Unlike a `List`, you cannot access elements by index — only the two ends
- For Snake this is perfect: we only ever need the head (front) and tail (back)
- `ArrayList.add(0, element)` shifts every element — O(n). `ArrayDeque.addFirst()` is always O(1)

### Head and Tail Convention
- Head = first element (`getFirst()`), Tail = last element (`getLast()`)
- This is a convention, not a technical requirement — it could be flipped
- We chose front = head because it reads naturally
- Encapsulation hides this: the engine only calls `snake.head()` and never knows which end it came from

## Design Principles

### Single Responsibility
- Each method should do exactly one thing
- `grow(Direction)` was doing two things (move + extend) — that was wrong
- Splitting into `move(Direction)` + `grow()` gave each method a single responsibility

### What Does an Object Know?
- The most important question in the plan phase: "what does this object know about the world?"
- Snake knows only its own body — nothing about food, the board, direction history, or game rules
- This question, asked early, would have led directly to the right design:
  - No stored direction in Snake → direction comes from engine on each call
  - `grow()` needs no direction → growing is a body operation, not a movement
  - Collision detection out of Snake → Snake has no board knowledge

### Object Responsibility Boundaries
- Snake: body state only — `move(Direction)`, `grow()`, `head()`, `body()`, `size()`
- GameEngine: direction state, reversal guard, food collision, tick control
- CollisionDetector: wall collision, self collision
- Snake grows from the **tail** (not the head) — growing from the head would be a move

### grow() Design Journey
- First design: `grow(Direction)` — moved head AND kept tail (two responsibilities)
- Second insight: grow should be a pure body operation — head stays, tail extends
- Final design: `grow()` duplicates `getLast()` — no direction, no movement
- The engine always calls `move(Direction)` every tick, and additionally calls `grow()` on food collision

### Naming Reflects Perspective
- `eat(Direction)` was wrong — the snake doesn't know about food, so "eat" leaks the engine's concept into the snake
- `grow()` is correct — from the snake's perspective, it just gets longer

### Don't Over-Engineer
- `GameEntity` interface not added yet — only one character exists
- The right time to add an interface is when you have real requirements (two characters that need to be treated uniformly)
- Good design now = keeping Snake clean and isolated so the interface can be added later with one line

### Future-Proof Without Speculating
- If a `GameEntity` interface is added later, Snake would just implement `occupiedPositions()` delegating to `body()`
- No logic changes needed — encapsulation already hides the implementation

## Testing

### Test Context (@BeforeEach + @Nested) vs Helper Methods
- `@BeforeEach` + `@Nested` breaks the `final` rule — fields must be mutable instance variables
- CLAUDE.md: each test owns its inputs as local `final var`
- Helper methods (`snakeWith`, `randomSnake`, `snakeMoving`) give the same readability without shared mutable state
- Test context fits immutable objects that are expensive to construct — not Snake

### Overloaded Helpers
- `snakeWith(Position)` and `snakeWith(Direction)` — overloads communicate what the test cares about
- Test that cares about position: `snakeWith(start)` — direction is irrelevant
- Test that cares about direction: `snakeWith(direction)` — position is irrelevant
- `snakeAt()`/`snakeMoving()` were worse names — `snakeWith()` reads naturally

### Randomizing Test Data
- Hardcoded directions like `Direction.RIGHT` mean the test only proves behavior for that one case
- `randomDirection()` proves the behavior holds for any direction
- For `headMovesOneStepInGivenDirection`: use `direction.dx`/`direction.dy` to compute expected position dynamically
- Only fix a value when the test is specifically about that value (e.g. boundary tests)

### Removing Redundant Tests
- `sizeIncreasesByOneAfterGrow` was redundant — `bodySegmentsAreInCorrectSpatialOrderAfterGrow` already proved size=2 implicitly
- If spatial order is correct, size is correct by definition

### When NOT to Parameterize
- CLAUDE.md: parameterize when tests share the same assertion logic and differ only in inputs
- `eachGrowCallAddsExactlyOneSegment` has two sequential cycles on the same snake instance — cannot parameterize without changing the test's meaning
- The second grow tests behavior on an already-grown snake, which is distinct from the first

### Set-Act-Verify Structure
- Blank lines between phases make the test phases scannable at a glance
- When grow + move are together in the act phase, they express a single combined action

## Working With Claude Effectively

### Catch Design Problems in the Plan Phase
- The `grow(Direction)` redesign happened mid-implementation — it cost test rewrites, multiple commits, and back-and-forth
- The root question "does the snake know it ate food?" would have resolved it in seconds during planning
- **Rule: in the plan phase, for every method, ask Claude: "what does this object know, and does this parameter make sense from its perspective?"**
- This is cheaper than fixing a test suite

### Challenge the Semantics, Not Just the Structure
- It's easy to agree on structure (Snake has move, grow, direction) without questioning what each method means
- `eat(Direction)` passed a structure review — it only failed a semantics review: "the snake doesn't know about food"
- Push Claude to justify every parameter: "why does grow need a direction?"

### Name Things From the Object's Perspective
- If a name leaks another object's concept (food → eat, game over → die), that's a signal the responsibility is wrong
- Names reveal design problems before code does

### Brainstorm Before Implementing
- The `grow()` design had three iterations — all the reasoning could have happened in a 5-minute brainstorm
- Before implementing: ask Claude "show me pros and cons of both approaches" — it surfaces tradeoffs without writing a line of code

## Workflow

### Plan Phase Questions to Ask
- "What does this object know about the world?" → reveals wrong responsibilities before any code
- "Does this parameter make sense from this object's perspective?" → catches `grow(Direction)` before implementation
- Cheaper to fix a concept than a test suite

### GitHub Issues and Plan Accuracy
- Issues and plan must reflect current design — stale descriptions mislead future work
- Close issues only after the PR is merged, not when implementation is complete on the branch
- Review comments on issues should be deleted when they become inaccurate

---

## Pre-Task Design Protocol (Born From Phase 1 Mistakes)

The `eat(Direction)` → `grow(Direction)` → `grow()` journey cost test rewrites, multiple commits, and back-and-forth. All of it could have been resolved in the plan phase. Use this protocol before writing any code for a new class or method.

### Step 1 — Map Responsibility Boundaries

For every class involved in the task, write one sentence:
> "This class knows: ___ and is responsible for: ___"

If that sentence contains "and also", split the class. This is what would have caught `Snake` storing direction — the sentence becomes "Snake knows its body **and also the last direction the engine chose**", which immediately signals engine concern leaked into Snake.

### Step 2 — Interrogate Every Method Parameter

For each planned method, ask:
- **"Where does this value come from?"** — the object that owns it is the one that should pass it, not receive it
- **"Would this method make sense if the object didn't know about X?"** — if yes, X doesn't belong as a parameter
- **"Can this object compute this itself, or does it require outside knowledge?"** — if outside knowledge, the method is in the wrong place

This is the question that would have killed `grow(Direction)` immediately: the snake has no food or direction state, so why would grow need a direction?

### Step 3 — Name Before You Code

Write the method call in plain English before writing the body:
- `snake.eat(direction)` → "does the snake know about food?" → no → wrong name, wrong responsibility
- `snake.grow()` → "is growing a body operation?" → yes → correct

**Rule:** if a method name borrows a concept from another object (food → eat, board → collide, engine → reverse), that's a signal the responsibility is misplaced. Fix the name, and the design follows.

### Step 4 — Ask "What Would Have to Change?"

Pick one likely future requirement and trace the impact:
> "If we change how food is placed, which objects need to change?"

If the answer includes objects that logically shouldn't care (e.g., Snake changes because food logic changed), the design is leaky. Good boundaries mean each change ripples only within the object that owns that concern.

### Questions to Ask Claude at the Start of Each Task

Before writing any code:
1. "For each class in this task, what does it know and what is it responsible for — write one sentence per class."
2. "For each planned method, does every parameter make sense strictly from this object's perspective?"
3. "Are any of the method names borrowing a concept from another object?"
4. "If I change [one requirement], which objects would need to change — does that make sense?"
5. "Show me a minimal skeleton with signatures only — no bodies — and let's validate the design before implementing."

Resolving these in the plan phase costs minutes. Resolving them after tests are written costs hours.
