# Devlog — Blackjack in Java

## Index
- [Day 1 — Project Setup & First Java Files](#day-1)
- [Day 2 — Complete Data Model](#day-2)

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


