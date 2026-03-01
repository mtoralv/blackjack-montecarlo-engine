# Devlog — Blackjack in Java

## Index
- [Day 1 — Project Setup & First Java Files](#day-1)
- [Day 2 — Complete Data Model](#day-2)
- [Day 3 — Game Logic & Display](#day-3)
- [Day 4 — Polished Game and cleaned code](#day-4)

---

## Day 1: Project Setup & First Java Files <a name="day-1"></a>
**Date:** February 23, 2026

### What I built
- Created GitHub repository and set up project structure
- Created `Suit.java`; a clean enum representing the 4 card suits
- Created `Rank.java`; an enum where each rank carries its Blackjack value

### What I learned
- What enums are: a fixed set of named values, perfect when options never change
- What fields are; variables that belong to an object
- That Java enums are actually objects; they can hold data and constructors just like classes
- Java naming conventions: classes start Uppercase, fields and variables start lowercase
- Thinking of a field and constructor like a locker: the field is the locker, the constructor puts something inside it at creation
- Enum values act as a bijection; each name maps to exactly one value

### What's next (Really short-term)
- Build `Card.java`; first real class with fields, constructor, and methods
- Build `Deck.java` and `Hand.java`
- Configure the readme file [Temporal]

---

## Day 2: Complete Data Model <a name="day-2"></a>
**Date:** February 24, 2026

### What I built
- Completed `Rank.java`; enum with values, private field, constructor, and getter
- Completed `Suit.java`; added Unicode symbols (♥ ♦ ♣ ♠) with field, constructor, and getter
- Completed `Card.java`; two private fields, constructor, getValue(), and toString()
- Completed `Deck.java`; builds 52 cards, newDeck(), deal(), shuffle(), size()
- Completed `Hand.java`; addCard(), getTotal() with Ace logic, isBust(), toString()
- Started `Player.java`; balance field, getBalance(), changeBalance()

### What I learned
- Encapsulation: private fields with public getters
- Separation of concerns: Hand holds cards, Game coordinates between objects

### Decisions made
- Created Player class to hold balance and hand; cleaner for Monte Carlo later
- Game logic will be separated from input/output to support automated simulation
- Hi-Lo card counting planned as a comparison strategy for Monte Carlo, along others

### What's next
- Complete Player.java; hand initialisation and reset between rounds
- Build Game.java; the full game loop
- Eventually: Basic Strategy solver, Monte Carlo simulator, Hi-Lo comparison among other methods
- Configure the readme file [Temporal]

---

## Day 3: Game Logic & Display <a name="day-3"></a>
**Date:** February 25, 2026

### What I built
- Completed `Game.java`; full round logic with betting, player turn, dealer turn, winner detection
- Added `Colors.java`; ANSI color constants for terminal display
- Improved `clearScreen()`; now shows casino header, chips, and bet
- Added `currentState()`; redraws game state with hidden/revealed dealer card
- Added `getFirstCard()` to `Hand.java`  for hiding dealer's second card
- Added `isBlackjack()` to `Hand.java`; checks for exactly two cards totalling 21
- Added `resetHand()` to `Hand.java`; clears hand between rounds
- Completed `Main.java`; game loop with play again prompt

### What I learned
- Resource leaks — Scanner must be closed, like fclose() in C

### Decisions made
- `bet` defaults to 10% of balance — works for both human and Monte Carlo
- `silent` mode planned for Monte Carlo — skip all print statements when simulating

### What's next
- Add pause after round result so player can read outcome
- Polish display and test edge cases
- Polish game logic 
- Fix various errors (Dealer doesn't deal for themselves, among others)
- Plan basic strategy solver

---

## Day 4: Polished the final Game part and cleaned the code <a name="day-4"></a>
**Date:** February 26, 2026

### What I built
- Created a `run.bat` file to auto-compile and run the code so it's easier
- Created a `bin` dir to store the .class files
- Started to add color to the game
- Changed balance to int
- Moved another round logic
- Auto stand on 21

### What's next
- Basic Strategy solver, Monte Carlo simulator, Hi-Lo comparison among other methods
- Configure the readme file [Temporal] to support the Montecarlo simulator part
- Change project name to support the Montecarlo simulator part

---



